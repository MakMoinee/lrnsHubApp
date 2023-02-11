package com.project.lrnshub;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.lrnshub.adapters.LocalAppAdapter;
import com.project.lrnshub.interfaces.AdapterListener;
import com.project.lrnshub.interfaces.SimpleListener;
import com.project.lrnshub.models.LocalApps;
import com.project.lrnshub.service.LocalFirestore;
import com.project.lrnshub.ui.home.App;

import java.util.ArrayList;
import java.util.List;

public class DeleteUserAppsActivity extends AppCompatActivity {

    String name = "", userID = "";
    TextView txtName;
    LocalFirestore fs;
    RecyclerView recyclerDeletable;
    LocalAppAdapter adapter;
    Button btnUninstall;

    List<App> selectedList;
    List<App> tmpList2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user_app);
        initViews();
        initListeners();
    }

    private void initListeners() {
        btnUninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DeleteUserAppsActivity.this);
                DialogInterface.OnClickListener dListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialogInterface.dismiss();
                                LocalApps mApp = new LocalApps();
                                mApp.setUserID(userID);
                                List<String> mPkgs = new ArrayList<>();
                                Log.e("SELECTED_LIST",selectedList.toString());
                                for (App tApp : selectedList) {
                                    mPkgs.add(tApp.getPackageN());
                                }
                                mApp.setPackages(mPkgs);
                                fs.addDeletableApp(mApp, new SimpleListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(DeleteUserAppsActivity.this, "Successfully Submit Apps To Be Deleted", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        if (e != null) {
                                            Log.e("ERROR_ADD_DELETABLE", e.getMessage());
                                        }
                                        SimpleListener.super.onError(e);
                                    }
                                });

                                break;
                            default:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                };
                mBuilder.setMessage("Are You Sure You Want To Delete This/These App/s?")
                        .setNegativeButton("Yes", dListener)
                        .setPositiveButton("No", dListener)
                        .setCancelable(false)
                        .show();
            }
        });
    }

    private void initViews() {
        name = getIntent().getStringExtra("name");
        userID = getIntent().getStringExtra("userID");
        tmpList2 = new ArrayList<>();
        recyclerDeletable = findViewById(R.id.recyclerInstalled);
        btnUninstall = findViewById(R.id.btnUninstall);
        selectedList = new ArrayList<>();
        fs = new LocalFirestore(DeleteUserAppsActivity.this);
        txtName = findViewById(R.id.txtName);
        txtName.setText(name);
        setTitle("Delete User Apps");
        fs.getLocalAppByUser(userID, new SimpleListener() {
            @Override
            public void onSuccessLocalApps(LocalApps apps) {
                if (apps != null) {
                    List<App> appList = new Gson().fromJson(apps.getRawApp(), new TypeToken<List<App>>() {
                    }.getType());
                    List<App> tmpList = new ArrayList<>();
                    for (App app : appList) {
                        if (app.getPackageN().contains("com.google.android")) {
                            continue;
                        }
                        app.setImage(ContextCompat.getDrawable(DeleteUserAppsActivity.this, R.drawable.ic_uninstall));
                        tmpList.add(app);
                        tmpList2.add(app);
                    }

                    adapter = new LocalAppAdapter(DeleteUserAppsActivity.this, tmpList, new AdapterListener() {
                        @Override
                        public void onItemWithSelectClickListener(App app, boolean isSelected) {

                            if (isSelected) {
                                selectedList.add(new App(null,app.getTitle(),app.getPackageN()));
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
                            btnUninstall.setEnabled(isEnable);
                        }
                    });

                    recyclerDeletable.setLayoutManager(new GridLayoutManager(DeleteUserAppsActivity.this, 4));
                    recyclerDeletable.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Exception e) {
                SimpleListener.super.onError(e);
            }
        });
    }
}
