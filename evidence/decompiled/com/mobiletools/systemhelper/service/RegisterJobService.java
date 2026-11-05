package com.mobiletools.systemhelper.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;
import c.d.a.b.h;

/* JADX INFO: loaded from: classes.dex */
public class RegisterJobService extends JobService {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f1427b = RegisterJobService.class.getSimpleName();

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        h.a(f1427b, "Service created");
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        h.a(f1427b, "Service destroyed");
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        h.a(f1427b, "onStartCommand");
        return 2;
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        h.a(f1427b, "on start jobId: " + jobParameters.getJobId());
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(f1427b, "on stop job: " + jobParameters.getJobId());
        return false;
    }
}
