package com.project.lrnshub.service;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.project.lrnshub.constants.Constants;
import com.project.lrnshub.interfaces.SimpleListener;
import com.project.lrnshub.models.LocalApps;
import com.project.lrnshub.models.Users;
import com.project.lrnshub.preference.UserPreference;

import java.util.ArrayList;
import java.util.List;

public class DeleteAppWorker extends Worker {
    public static volatile boolean shouldStop = false;
    LocalFirestore fs;
    Context mContext;
    String userID = "";

    public DeleteAppWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        fs = new LocalFirestore(context);
        this.mContext = context;
        userID = Constants.UserID;
    }

    @NonNull
    @Override
    public Result doWork() {
        deleteApp(new SimpleListener() {
            @Override
            public void onSuccessLocalApps(LocalApps apps) {
                Log.e("LOCALAPPS", apps.getPackages().toString());
                Context tContext = Constants.mContext;
                for (String pkg : apps.getPackages()) {
                    boolean isDeleted = checkIfDeleted(pkg);
                    if (!isDeleted) {
                        Intent intent = new Intent(Intent.ACTION_DELETE);
                        intent.setData(Uri.parse("package:" + pkg));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    Intent intent1 = new Intent("com.example.TRIGGER_DELETE");
//                    intent1.setData(Uri.parse(pkg));
//                    intent1.putExtra("pkg",pkg);
                        mContext.startActivity(intent);
                    }

//                    try {
//                        wait();
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
                }

                LocalApps newApp = iteratePackageList(apps);
                if (newApp.getPackages().size() > 0) {
                    fs.addDeletableApp(newApp, new SimpleListener() {
                        @Override
                        public void onSuccess() {
                            SimpleListener.super.onSuccess();
                        }

                        @Override
                        public void onError(Exception e) {
                            try {
                                if (e != null) {
                                    Log.e("WORKER_ERROR", e.getMessage());
                                }
                            } catch (Exception e2) {

                            }
                        }

                    });
                }else{
                    fs.deleteDeletableApp(userID, new SimpleListener() {
                        @Override
                        public void onSuccess() {
                            SimpleListener.super.onSuccess();
                        }

                        @Override
                        public void onError(Exception e) {
                            SimpleListener.super.onError(e);
                        }
                    });
                }

                Result.success();
            }

            @Override
            public void onError(Exception e) {
                SimpleListener.super.onError(e);
                Result.failure();
            }
        });
        return Result.success();
    }

    private void deleteApp(SimpleListener listener) {
        Users users = new UserPreference(mContext).getUser();
        Log.e("userID", users.getDocID());
        fs.getOnDeleteApps(users.getDocID(), new SimpleListener() {
            @Override
            public void onSuccessLocalApps(LocalApps apps) {


//                LocalApps newApp = iteratePackageList(apps);
//                if (newApp.getPackages().size() > 0) {
//                    fs.addDeletableApp(newApp, new SimpleListener() {
//                        @Override
//                        public void onSuccess() {
//                            SimpleListener.super.onSuccess();
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            try {
//                                if (e != null) {
//                                    Log.e("WORKER_ERROR", e.getMessage());
//                                }
//                            } catch (Exception e2) {
//
//                            }
//                        }
//                    });
//                }
                listener.onSuccessLocalApps(apps);
            }

            @Override
            public void onError(Exception e) {
                try {
                    if (e != null) {
                        Log.e("WORKER_ERRORSS", e.getMessage());
                    }
                } catch (Exception e2) {

                }
                listener.onError(e);
            }
        });

    }

    private LocalApps iteratePackageList(LocalApps apps) {
        List<String> newPkgs = new ArrayList<>();
        for (String str : apps.getPackages()) {
            boolean isDeleted = checkIfDeleted(str);
            if (!isDeleted) {
                newPkgs.add(str);
            }
        }
        apps.setPackages(newPkgs);

        return apps;
    }

    private boolean checkIfDeleted(String pkg) {
        boolean isDeleted = true;
        try {
            PackageManager packageManager = mContext.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

            for (ResolveInfo resolveInfo : resolveInfoList) {
                if (pkg.equals(resolveInfo.activityInfo.packageName)) {
                    isDeleted = false;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDeleted;
    }
}
