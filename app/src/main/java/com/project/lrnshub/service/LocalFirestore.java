package com.project.lrnshub.service;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.project.lrnshub.hashmaps.CommonMaps;
import com.project.lrnshub.interfaces.SimpleListener;
import com.project.lrnshub.models.LocalApps;
import com.project.lrnshub.models.Users;

import java.util.ArrayList;
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
        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e);
            }
        });
    }

    public void storeLocalApps(LocalApps local, SimpleListener listener) {
        final Map<String, Object> reqMap = CommonMaps.convertLocalAppToMap(local);
        db.collection("apps")
                .document(local.getUserID())
                .set(reqMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e);
                    }
                });
    }

    public void getLocalAppByUser(String userID, SimpleListener listener) {

        db.collection("apps")
                .document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            LocalApps localApps = documentSnapshot.toObject(LocalApps.class);
                            listener.onSuccessLocalApps(localApps);
                        } else {
                            listener.onError(new Exception("empty"));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e);
                    }
                });
    }

    public void getOnDeleteApps(String userID, SimpleListener listener) {
        try {
            db.collection("deletable")
                    .document(userID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
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
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onError(e);
                        }
                    });
        } catch (Exception e) {
            listener.onError(e);
        }
    }

    public void addDeletableApp(LocalApps apps, SimpleListener listener) {
        Map<String, Object> finalMap = CommonMaps.convertLocalAppToMap(apps);
        db.collection("deletable")
                .document(apps.getUserID())
                .set(finalMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e);
                    }
                });
    }

    public void addInstallableApp(LocalApps apps, SimpleListener listener) {
        Map<String, Object> finalMap = CommonMaps.convertLocalAppToMap(apps);
        db.collection("installable")
                .document(apps.getUserID())
                .set(finalMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e);
                    }
                });
    }

    public void getOnInstallableApps(String userID, SimpleListener listener) {
        try {
            db.collection("installable")
                    .document(userID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
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
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onError(e);
                        }
                    });
        } catch (Exception e) {
            listener.onError(e);
        }
    }
}
