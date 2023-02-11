package com.project.lrnshub.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class LocalWorkerService extends IntentService {
    LocalWorkManager wm;


    public LocalWorkerService() {
        super(LocalWorkerService.class.getSimpleName());
    }

    public static volatile boolean shouldStop = false;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        wm = new LocalWorkManager(getApplicationContext());
        wm.runDeleteWorker();
        if (shouldStop) {
            stopSelf();
            wm.stopWorker();
            return;
        }
    }
}
