package com.project.lrnshub;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.lrnshub.databinding.ActivityMainBinding;
import com.project.lrnshub.service.DeletePublisher;
import com.project.lrnshub.service.InstallAppReceiver;

public class MainActivity extends AppCompatActivity implements FragmentToActivity {

    private ActivityMainBinding binding;
    private Boolean isFocus = false, onClickApp = true, checkFocusMode;
    private int count = 0;
    NavController navController;
    public static Context sContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (getUsageStatsPermissionsStatus(this) != PermissionStatus.GRANTED) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }


        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_setting, R.id.navigation_account)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        sContext = MainActivity.this;
        BroadcastReceiver br = new DeletePublisher(MainActivity.this);
        IntentFilter intentFilter = new IntentFilter("com.example.TRIGGER_DELETE");
        registerReceiver(br, intentFilter);

//        LocalWorkerService.shouldStop = true;
//        LocalWorkerService.shouldStop = false;
//        startService(new Intent(MainActivity.this, LocalWorkerService.class));
        registerReceiver(new InstallAppReceiver(), new IntentFilter("com.example.TRIGGER_INSTALL"));
    }

    public static PermissionStatus getUsageStatsPermissionsStatus(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return PermissionStatus.CANNOT_BE_GRANTED;
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        final int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_DEFAULT ?
                (context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED)
                : (mode == AppOpsManager.MODE_ALLOWED);
        return granted ? PermissionStatus.GRANTED : PermissionStatus.DENIED;
    }

    public enum PermissionStatus {
        GRANTED, DENIED, CANNOT_BE_GRANTED
    }

    @Override
    public void check(Boolean isFocusMode) {
        isFocus = isFocusMode;
    }

    @Override
    public void checkApp(Boolean onClickApp) {
        Log.d("hceck", "check");
        this.onClickApp = onClickApp;
        count = 0;
        checkFocusMode = onClickApp;
        isFocus = !onClickApp;

    }

    private void back() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure to exit the app?");
//            builder.setMessage("Message to be displayed in the alert dialog");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        if (!navController.navigateUp()) {
            if (!isFocus) {
                back();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("hceck", "post" + count);
        if (count == 1) {
            if (checkFocusMode != null) {
                isFocus = checkFocusMode;
                checkFocusMode = null;
            }
        }
        count++;
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

        Log.d("hceck", isFocus + "check app");
        if (isFocus) {
            activityManager.moveTaskToFront(getTaskId(), 0);
        }
    }

    public static void openDeleteApp(String pkg) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse(pkg));
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        sContext.startActivity(intent);
    }
}