package com.project.lrnshub;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.lrnshub.adapters.LocalAppAdapter;
import com.project.lrnshub.databinding.ActivityDeleteUserAppBinding;
import com.project.lrnshub.interfaces.AdapterListener;
import com.project.lrnshub.interfaces.SimpleListener;
import com.project.lrnshub.models.LocalApps;
import com.project.lrnshub.service.LocalFirestore;
import com.project.lrnshub.ui.home.App;

import java.util.ArrayList;
import java.util.List;

public class DeleteActivity extends AppCompatActivity {

    ActivityDeleteUserAppBinding binding;
    LocalFirestore fs;
    LocalAppAdapter appAdapter;
    List<App> selectedList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteUserAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setValues();
        loadAll();
        initListeners();
    }

    private void initListeners() {
        binding.btnUninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DeleteActivity.this);
                DialogInterface.OnClickListener dListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialogInterface.dismiss();
                                LocalApps mApp = new LocalApps();
                                List<String> mPkgs = new ArrayList<>();
                                Log.e("SELECTED_LIST", selectedList.toString());
                                for (App tApp : selectedList) {
                                    mPkgs.add(tApp.getPackageN());
                                }
                                mApp.setPackages(mPkgs);
                                fs.addDeletableForAll(mApp, new SimpleListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(DeleteActivity.this, "Successfully Submit Apps To Be Deleted", Toast.LENGTH_SHORT).show();
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

    private void setValues() {
        binding.lblName.setVisibility(View.GONE);
        binding.txtName.setVisibility(View.GONE);
        fs = new LocalFirestore(DeleteActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadAll() {
        fs.getAllApps(new SimpleListener() {
            @Override
            public void onSuccessLocalApps(LocalApps apps) {
                List<App> appList = new Gson().fromJson(apps.getRawApp(), new TypeToken<List<App>>() {
                }.getType());
                List<App> list = new ArrayList<>();
                for (App app : appList) {
                    if (app.getPackageN().contains("com.google.android")) {
                        continue;
                    }
                    app.setImage(ContextCompat.getDrawable(DeleteActivity.this, R.drawable.ic_uninstall));
                    list.add(app);
                }
                appAdapter = new LocalAppAdapter(DeleteActivity.this, list, new AdapterListener() {
                    @Override
                    public void onItemWithSelectClickListener(App app, boolean isSelected) {
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
                        binding.btnUninstall.setEnabled(isEnable);
                    }
                });

                binding.recyclerInstalled.setLayoutManager(new GridLayoutManager(DeleteActivity.this, 4));
                binding.recyclerInstalled.setAdapter(appAdapter);
            }

            @Override
            public void onError(Exception e) {
                SimpleListener.super.onError(e);
            }
        });

    }
}
