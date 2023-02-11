package com.project.lrnshub.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.project.lrnshub.DeleteActivity;

public class DeletePublisher extends BroadcastReceiver {
    Context mContext;

    public DeletePublisher(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("DELETE_PUBLISHER", "Triggered");


        String pkg = intent.getStringExtra("pkg");

        if (intent.getAction().equals("com.example.TRIGGER_DELETE")) {
            Log.e("DELETE_PUBLISHER", intent.getAction());
            try {
                Intent intent1 = new Intent(context,DeleteActivity.class);
                intent1.putExtra("pkg", pkg);
                context.startActivity(intent1);
            } catch (Exception e) {
                Toast.makeText(context, "DELETE_PUBLISHER TRIGGER", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
