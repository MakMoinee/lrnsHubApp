package com.project.lrnshub.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.project.lrnshub.interfaces.SimpleListener;
import com.project.lrnshub.models.LocalApps;
import com.project.lrnshub.models.Users;
import com.project.lrnshub.preference.UserPreference;

import java.util.List;

public class InstallAppWorker extends Worker {

    LocalFirestore fs;
    Context mContext;

    public InstallAppWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        fs = new LocalFirestore(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Users users = new UserPreference(mContext).getUser();
        fs.getOnInstallableApps(users.getDocID(), new SimpleListener() {
            @Override
            public void onSuccessLocalApps(LocalApps apps) {
                for (String str : apps.getPackages()) {
                    boolean isInstalled = checkIfInstalled(str);
                    if (!isInstalled) {
                        Intent intent = new Intent("com.example.TRIGGER_INSTALL");
                        intent.putExtra("pkg", str);
                        mContext.sendBroadcast(intent);
                    }
                }
                Result.success();
            }

            @Override
            public void onError(Exception e) {
                if (e != null) {
                    Log.e("ERROR_INSTALL", e.getMessage());
                }
                Result.retry();
            }
        });
        return Result.success();
    }

    private boolean checkIfInstalled(String pkg) {
        boolean isInstalled = false;
        try {
            PackageManager packageManager = mContext.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

            for (ResolveInfo resolveInfo : resolveInfoList) {
                if (pkg.equals(resolveInfo.activityInfo.packageName)) {
                    isInstalled = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isInstalled;
    }
}
