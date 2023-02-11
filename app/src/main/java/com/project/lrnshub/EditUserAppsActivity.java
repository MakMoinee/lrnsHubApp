package com.project.lrnshub;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.lrnshub.adapters.LocalAppAdapter;
import com.project.lrnshub.interfaces.AdapterListener;
import com.project.lrnshub.interfaces.SimpleListener;
import com.project.lrnshub.models.LocalApps;
import com.project.lrnshub.service.LocalFirestore;
import com.project.lrnshub.ui.home.App;

import java.util.ArrayList;
import java.util.List;

public class EditUserAppsActivity extends AppCompatActivity {

    RecyclerView installedRecycler, notInstalledRecycler;
    LocalFirestore fs;
    String userID = "", name = "";
    LocalAppAdapter adapter1;
    LocalAppAdapter adapter2;
    List<App> appList, installedApps, notInstalledApps, selectedList;
    TextView txtName;
    Button btnPreInstall;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_app);
        initializeList();
        initViews();
        initListeners();

    }

    private void initListeners() {
        btnPreInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditUserAppsActivity.this);
                DialogInterface.OnClickListener dListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                LocalApps apps = new LocalApps();
                                List<String> pkgs = new ArrayList<>();
                                for (App tApp : selectedList) {
                                    pkgs.add(tApp.getPackageN());
                                }
                                apps.setPackages(pkgs);
                                apps.setUserID(userID);
                                fs.addInstallableApp(apps, new SimpleListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(EditUserAppsActivity.this, "Successfully Submit Apps to Pre Install", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        SimpleListener.super.onError(e);
                                    }
                                });
                                break;
                            case DialogInterface.BUTTON_POSITIVE:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                };
                mBuilder.setMessage("Are You Sure You Want To Pre Install This/Theses App/s?")
                        .setNegativeButton("Yes", dListener)
                        .setPositiveButton("No", dListener)
                        .setCancelable(false)
                        .show();
            }
        });
    }

    private void addItem(List<App> appList, Integer drawable, String name, String packageN) {
        appList.add(new App(ContextCompat.getDrawable(EditUserAppsActivity.this, drawable), name, packageN));
    }

    private void initializeList() {
        appList = new ArrayList<>();
        addItem(appList, R.drawable.icon1, "Merriam webster", "com.merriamwebster");
        addItem(appList, R.drawable.icon2, "Google Docs", "com.google.android.apps.docs.editors.docs");
        addItem(appList, R.drawable.icon3, "Google Excel", "com.google.android.apps.docs.editors.sheets");
        addItem(appList, R.drawable.icon4, "Evernote", "com.evernote");
        addItem(appList, R.drawable.icon5, "Google Classroom", "com.google.android.apps.classroom");
        addItem(appList, R.drawable.icon6, "Photomath", "com.microblink.photomath");
        addItem(appList, R.drawable.icon7, "WPS", "cn.wps.moffice_eng");
        addItem(appList, R.drawable.icon9, "Google Drive", "com.google.android.apps.docs");
        addItem(appList, R.drawable.brainly, "Brainly", "co.brainly");
        addItem(appList, R.drawable.canvas, "Canvas", "com.instructure.candroid");
        addItem(appList, R.drawable.classcraft, "Classcraft", "com.classcraft.android");
        addItem(appList, R.drawable.classdojo, "ClassDojo", "com.classdojo.android");
        addItem(appList, R.drawable.dropbox, "Dropbox", "com.dropbox.android");
        addItem(appList, R.drawable.duolingo, "Duolingo", "com.duolingo");
        addItem(appList, R.drawable.edpuzzle, "Edpuzzle", "com.edpuzzle.student");
        addItem(appList, R.drawable.facebook, "Facebook", "com.facebook.katana");
        addItem(appList, R.drawable.falou, "Falou", "com.moymer.falou");
        addItem(appList, R.drawable.hangouts, "Google Chats", "com.google.android.apps.dynamite");
        addItem(appList, R.drawable.kahoot, "Kahoot", "no.mobitroll.kahoot.android");
        addItem(appList, R.drawable.learnkorea, "Learnkorea", "com.breboucas.coreanoparaviaja");
        addItem(appList, R.drawable.lingomate, "Lingomate", "com.deer.cc");
        addItem(appList, R.drawable.mathpid, "Mathpid", "com.wjthinkbig.mathpid");
        addItem(appList, R.drawable.mentalmath, "MentalMath", "com.monsterschool.colorcal");
        addItem(appList, R.drawable.messenger, "Messenger", "com.facebook.orca");
        addItem(appList, R.drawable.peak, "Peak", "com.brainbow.peak.app");
        addItem(appList, R.drawable.plickers, "Plickers", "com.plickers.client.android");
        addItem(appList, R.drawable.prodigy, "Prodigy", "com.prodigygame.prodigy");
        addItem(appList, R.drawable.quizalize, "Quazalize", "com.zzish.quizalizescan");
        addItem(appList, R.drawable.quizizz, "Quizizz", "com.quizizz_mobile");
        addItem(appList, R.drawable.quizlet, "Quizlet", "com.quizlet.quizletandroid");
        addItem(appList, R.drawable.seesaw, "Seesaw", "seesaw.shadowpuppet.co.classroom");
        addItem(appList, R.drawable.trello, "Trello", "com.trello");
        addItem(appList, R.drawable.yeaetalk, "Yeaetalk", "com.imback.yeetalk");

    }

    private void sortList() {
//        installedApps = new ArrayList<>();
        notInstalledApps = new ArrayList<>();
//        installedAllApps = new ArrayList<>();
        for (App item : appList) {
            if (isPackageInstalled(item.getPackageN())) {
//                installedApps.add(item);
//                installedAllApps.add(item);
            } else {
                notInstalledApps.add(item);
            }
        }


        adapter2 = new LocalAppAdapter(EditUserAppsActivity.this, notInstalledApps, new AdapterListener() {
            @Override
            public void onItemWithSelectClickListener(int position, App app, boolean isSelected) {
                if (isSelected) {
                    selectedList.add(new App(null, app.getTitle(), app.getPackageN()));
                } else {
                    for (App nApp : selectedList) {
                        if (nApp.getPackageN().equals(app.getPackageN())) {
                            selectedList.remove(nApp);
                        }
                    }

                }
            }

            @Override
            public void onEnableButton(boolean isEnable) {
                btnPreInstall.setEnabled(isEnable);
            }
        });
        notInstalledRecycler.setLayoutManager(new GridLayoutManager(EditUserAppsActivity.this, 4));
        notInstalledRecycler.setAdapter(adapter2);
    }

    private boolean isPackageInstalled(String packageName) {
        boolean isInstalled = false;
        for (App tmpApp : installedApps) {
            if (packageName.equals(tmpApp.getPackageN())) {
                isInstalled = true;
                break;
            }
        }
        return isInstalled;
    }


    private void initViews() {
        selectedList = new ArrayList<>();
        installedApps = new ArrayList<>();
        installedRecycler = findViewById(R.id.recyclerInstalled);
        notInstalledRecycler = findViewById(R.id.recyclerNotInstalled);
        btnPreInstall = findViewById(R.id.btnPreinstall);
        txtName = findViewById(R.id.txtName);
        userID = getIntent().getStringExtra("userID");
        name = getIntent().getStringExtra("name");
        txtName.setText(name);
        setTitle("Edit User Apps");
        fs = new LocalFirestore(EditUserAppsActivity.this);
        if (userID != "") {
            fs.getLocalAppByUser(userID, new SimpleListener() {
                @Override
                public void onSuccessLocalApps(LocalApps apps) {
                    if (apps != null) {
                        for (String str : apps.getOriginalPackages()) {
                            for (App tmpApp : appList) {
                                if (str.equals(tmpApp.getPackageN())) {
                                    installedApps.add(tmpApp);
                                }
                            }
                        }

                        sortList();

                        adapter1 = new LocalAppAdapter(EditUserAppsActivity.this, installedApps, new AdapterListener() {
                            @Override
                            public void onItemClickListener(int position) {
                                AdapterListener.super.onItemClickListener(position);
                            }
                        });
                        installedRecycler.setLayoutManager(new GridLayoutManager(EditUserAppsActivity.this, 4));
                        installedRecycler.setAdapter(adapter1);
                    }
                }

                @Override
                public void onError(Exception e) {
                    SimpleListener.super.onError(e);
                }
            });
        }
    }
}
