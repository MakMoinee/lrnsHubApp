package com.project.lrnshub;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.lrnshub.service.DeletePublisher;
import com.project.lrnshub.service.LocalWorkManager;
import com.project.lrnshub.service.LocalWorkerService;

public class DeleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        String pkg = getIntent().getStringExtra("pkg");
        LocalWorkerService.shouldStop = true;
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent intent1 = new Intent(Intent.ACTION_DELETE);
               intent1.setData(Uri.parse("package:"+pkg));
               intent1.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent1);
               finish();
           }
       },2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startService(new Intent(DeleteActivity.this,LocalWorkerService.class));
    }
}
