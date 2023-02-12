package com.project.lrnshub.service;

import static com.project.lrnshub.constants.Constants.ADMIN_ID_FOR_DELETE;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.lrnshub.hashmaps.CommonMaps;
import com.project.lrnshub.interfaces.SimpleListener;
import com.project.lrnshub.models.LocalApps;
import com.project.lrnshub.models.Users;
import com.project.lrnshub.ui.home.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LocalFirestore {
    Context context;
    FirebaseFirestore db;

    public LocalFirestore(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }


    public void getAllUsers(SimpleListener listener) {
        List<Users> usersList = new ArrayList<>();
        db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                listener.onError(new Exception("empty"));
            } else {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        Users users = documentSnapshot.toObject(Users.class);

                        if (users != null) {
                            users.setDocID(documentSnapshot.getId());
                            usersList.add(users);
                        }
                    } else {
                        listener.onError(new Exception("non existent"));
                    }
                }
                listener.onSuccessUserList(usersList);
            }
        }).addOnFailureListener(e -> listener.onError(e));
    }

    public void storeLocalApps(LocalApps local, SimpleListener listener) {
        final Map<String, Object> reqMap = CommonMaps.convertLocalAppToMap(local);
        db.collection("apps")
                .document(local.getUserID())
                .set(reqMap, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                            listener.onSuccess();
                            addAllApps(local);
                        }
                )
                .addOnFailureListener(e -> listener.onError(e));

    }

    private void addAllApps(LocalApps localApp) {
        List<App> apps = new ArrayList<>();
        List<App> finalApps = new ArrayList<>();


        db.collection("allapp").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {

            } else {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        LocalApps tmpApp = documentSnapshot.toObject(LocalApps.class);
                        List<App> appList = new Gson().fromJson(tmpApp.getRawApp(), new TypeToken<List<App>>() {
                        }.getType());
                        if (appList != null) {
                            for (App sapp : appList) {
                                if (apps.size() == 0) {
                                    apps.add(sapp);
                                } else {
                                    boolean isContinue = false;
                                    for (App napp : apps) {
                                        if (napp.getPackageN().equals(sapp.getPackageN())) {
                                            isContinue = true;
                                            break;
                                        }
                                    }
                                    if (!isContinue) {
                                        apps.add(sapp);
                                    }
                                }
                            }

                        }
                    }
                }

            }
        });
        Collections.copy(finalApps, apps);
        List<App> localList = new Gson().fromJson(localApp.getRawApp(), new TypeToken<List<App>>() {
        }.getType());
        for (App tApp : localList) {
            if (apps.size() == 0) {
                finalApps.add(tApp);
            } else {
                for (App sApp : apps) {
                    if (tApp.getPackageN().equals(sApp.getPackageN())) {
                        continue;
                    } else {
                        finalApps.add(tApp);
                    }
                }
            }
        }

        LocalApps finalLocalApp = new LocalApps();
        finalLocalApp.setUserID(ADMIN_ID_FOR_DELETE);
        finalLocalApp.setRawApp(new Gson().toJson(finalApps));
        Map<String, Object> reqMap = CommonMaps.convertLocalAppToMap(finalLocalApp);
        db.collection("allapp")
                .document(ADMIN_ID_FOR_DELETE)
                .set(reqMap, SetOptions.merge())
                .addOnSuccessListener(unused -> Log.e("SUCCESSFUL_ADD", "YEHEY"))
                .addOnFailureListener(e -> Log.e("ERROR_ADD", e.getMessage()));
    }


    public void getLocalAppByUser(String userID, SimpleListener listener) {

        db.collection("apps")
                .document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        LocalApps localApps = documentSnapshot.toObject(LocalApps.class);
                        listener.onSuccessLocalApps(localApps);
                    } else {
                        listener.onError(new Exception("empty"));
                    }
                })
                .addOnFailureListener(e -> listener.onError(e));
    }

    public void getOnDeleteApps(String userID, SimpleListener listener) {
        try {
            db.collection("deletable")
                    .document(userID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {

                            LocalApps apps = documentSnapshot.toObject(LocalApps.class);
                            if (apps != null) {
                                listener.onSuccessLocalApps(apps);
                            } else {
                                listener.onError(new Exception("empty"));
                            }
                        } else {
                            listener.onError(new Exception("empty"));
                        }
                    })
                    .addOnFailureListener(e -> listener.onError(e));
        } catch (Exception e) {
            listener.onError(e);
        }
    }

    public void addDeletableApp(LocalApps apps, SimpleListener listener) {
        Map<String, Object> finalMap = CommonMaps.convertLocalAppToMap(apps);
        db.collection("deletable")
                .document(apps.getUserID())
                .set(finalMap, SetOptions.merge())
                .addOnSuccessListener(unused -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e));
    }

    public void deleteDeletableApp(String userID, SimpleListener listener) {
        db.collection("deletable")
                .document(userID)
                .delete()
                .addOnSuccessListener(unused -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e));
    }

    public void addInstallableApp(LocalApps apps, SimpleListener listener) {
        Map<String, Object> finalMap = CommonMaps.convertLocalAppToMap(apps);
        db.collection("installable")
                .document(apps.getUserID())
                .set(finalMap, SetOptions.merge())
                .addOnSuccessListener(unused -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e));
    }

    public void getOnInstallableApps(String userID, SimpleListener listener) {
        try {
            db.collection("installable")
                    .document(userID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {

                            LocalApps apps = documentSnapshot.toObject(LocalApps.class);
                            if (apps != null) {
                                listener.onSuccessLocalApps(apps);
                            } else {
                                listener.onError(new Exception("empty"));
                            }
                        } else {
                            listener.onError(new Exception("empty"));
                        }
                    })
                    .addOnFailureListener(e -> listener.onError(e));
        } catch (Exception e) {
            listener.onError(e);
        }
    }

    public void addDeletableForAll(LocalApps apps, SimpleListener listener) {
        getAllUsers(new SimpleListener() {
            @Override
            public void onSuccessUserList(List<Users> usersList) {
                for (Users users : usersList) {
                    apps.setUserID(users.getDocID());
                    addDeletableApp(apps, new SimpleListener() {
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
                listener.onSuccess();
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }
        });
//
//        Map<String, Object> finalMap = CommonMaps.convertLocalAppToMap(apps);
////        db.collection("forall")
////                        .document()
//        db.collection("forall")
//                .document(ADMIN_ID_FOR_DELETE)
//                .set(finalMap, SetOptions.merge())
//                .addOnSuccessListener(unused -> listener.onSuccess())
//                .addOnFailureListener(e -> listener.onError(e));
    }

    public void getAllApps(SimpleListener listener) {
        db.collection("allapp")
                .document(ADMIN_ID_FOR_DELETE)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        LocalApps apps = documentSnapshot.toObject(LocalApps.class);
                        if (apps != null) {
                            listener.onSuccessLocalApps(apps);
                        } else {
                            listener.onError(new Exception("empty"));
                        }
                    } else {
                        listener.onError(new Exception("empty"));
                    }
                })
                .addOnFailureListener(e -> listener.onError(e));
    }
}
