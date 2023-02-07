package com.project.lrnshub;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.project.lrnshub.ui.admin.AdminHomeFragment;

public class AdminActivity extends AppCompatActivity {

    Fragment fragment;
    FragmentTransaction ft;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initViews();
    }

    private void initViews() {
        fragment = new AdminHomeFragment(AdminActivity.this);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.adminFragment, fragment, null);
        ft.commit();
    }
}
