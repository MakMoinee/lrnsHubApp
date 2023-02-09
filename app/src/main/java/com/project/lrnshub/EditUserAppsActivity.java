package com.project.lrnshub;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.project.lrnshub.interfaces.SimpleListener;
import com.project.lrnshub.models.LocalApps;
import com.project.lrnshub.service.LocalFirestore;

public class EditUserAppsActivity extends AppCompatActivity {

    RecyclerView installedRecycler, notInstalledRecycler;
    LocalFirestore fs;
    String userID = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_app);
        initViews();
    }

    private void initViews() {
        installedRecycler = findViewById(R.id.recyclerInstalled);
        notInstalledRecycler = findViewById(R.id.recyclerNotInstalled);
        userID = getIntent().getStringExtra("userID");
        fs = new LocalFirestore(EditUserAppsActivity.this);
        if (userID != "") {
            fs.getLocalAppByUser(userID, new SimpleListener() {
                @Override
                public void onSuccessLocalApps(LocalApps apps) {
                    if (apps != null) {

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
