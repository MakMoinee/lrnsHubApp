package com.project.lrnshub;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.lrnshub.service.LocalWorkManager;
import com.project.lrnshub.ui.account.AccountFragment;
import com.project.lrnshub.ui.admin.AdminHomeFragment;

public class AdminActivity extends AppCompatActivity {

    Fragment fragment;
    FragmentTransaction ft;
    FragmentManager fm;

    BottomNavigationView btnNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initViews();
        initListener();
    }

    private void initListener() {
        btnNav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new AdminHomeFragment(AdminActivity.this);
                        fm = getSupportFragmentManager();
                        ft = fm.beginTransaction();
                        ft.replace(R.id.adminFragment, fragment, null);
                        ft.commit();
                        break;
                    case R.id.navigation_account:
                        fragment = new AccountFragment();
                        fm = getSupportFragmentManager();
                        ft = fm.beginTransaction();
                        ft.replace(R.id.adminFragment, fragment, null);
                        ft.commit();
                        break;
                }
            }
        });
    }

    private void initViews() {
        btnNav = findViewById(R.id.nav_view);
        fragment = new AdminHomeFragment(AdminActivity.this);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.adminFragment, fragment, null);
        ft.commit();
    }
}
