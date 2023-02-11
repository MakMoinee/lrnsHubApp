package com.project.lrnshub.service;

import android.content.Context;

import androidx.work.BackoffPolicy;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

public class LocalWorkManager {

    Context mContext;
    WorkManager wm;
    WorkRequest deleteRequest, installRequest;
    Operation op;
    public static WorkManager staticWM;

    public LocalWorkManager(Context mContext) {
        this.mContext = mContext;
        wm = WorkManager.getInstance(this.mContext);
        deleteRequest = new PeriodicWorkRequest.Builder(DeleteAppWorker.class, 15, TimeUnit.SECONDS)
//                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
                .build();

        installRequest = new PeriodicWorkRequest.Builder(InstallAppWorker.class, 15, TimeUnit.SECONDS)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
                .build();
        staticWM = wm;
    }

    public LocalWorkManager(WorkManager wm) {
        this.wm = wm;
        deleteRequest = new PeriodicWorkRequest.Builder(DeleteAppWorker.class, 15, TimeUnit.SECONDS)
                .build();
    }

    public void runDeleteWorker() {
        wm.enqueue(deleteRequest);
    }

    public void runInstallWorker() {
        wm.enqueue(installRequest);
    }

    public void stopWorker() {
        wm.cancelAllWork();
    }

    public static void stopAllWorkers(){
        staticWM.cancelAllWork();
    }


}
