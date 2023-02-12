package com.project.lrnshub;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.project.lrnshub.ui.account.AdminAccountFragment;
import com.project.lrnshub.ui.admin.AdminHomeFragment;

public class AdminActivity extends AppCompatActivity {

    Fragment fragment;
    FragmentTransaction ft;
    FragmentManager fm;

    NavigationBarView btnNav;
    int navIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initViews();
        initListener();
    }

    private void initListener() {
        btnNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new AdminHomeFragment(AdminActivity.this);
                        fm = getSupportFragmentManager();
                        ft = fm.beginTransaction();
                        ft.replace(R.id.adminFragment, fragment, null);
                        ft.commit();
                        navIndex = 0;

                        return true;
                    case R.id.navigation_account:
                        fragment = new AdminAccountFragment();
                        fm = getSupportFragmentManager();
                        ft = fm.beginTransaction();
                        ft.replace(R.id.adminFragment, fragment, null);
                        ft.commit();
                        navIndex = 1;
                        return true;
                }
                return false;
            }
        });
    }

    private void initViews() {
        btnNav = findViewById(R.id.nav_view);
        setTitle("Admin");
        fragment = new AdminHomeFragment(AdminActivity.this);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.adminFragment, fragment, null);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (navIndex) {
            case 0:
                getMenuInflater().inflate(R.menu.options_nav_menu, menu);
                return true;
            default:
                return super.onCreateOptionsMenu(menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_delete:
                startActivity(new Intent(AdminActivity.this, DeleteActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(AdminActivity.this).toBundle());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
