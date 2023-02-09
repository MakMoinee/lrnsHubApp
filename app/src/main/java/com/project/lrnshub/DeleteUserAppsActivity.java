package com.project.lrnshub;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user_app);
        initViews();
    }

    private void initViews() {
        name = getIntent().getStringExtra("name");
        userID = getIntent().getStringExtra("userID");
        recyclerDeletable = findViewById(R.id.recyclerInstalled);
        btnUninstall = findViewById(R.id.btnUninstall);
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
                    }

                    adapter = new LocalAppAdapter(DeleteUserAppsActivity.this, tmpList, new AdapterListener() {
                        @Override
                        public void onItemClickListener(int position) {
                            AdapterListener.super.onItemClickListener(position);
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
