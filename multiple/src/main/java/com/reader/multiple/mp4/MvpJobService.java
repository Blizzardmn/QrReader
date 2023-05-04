package com.reader.multiple.mp4;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;

@TargetApi(21)
public class MvpJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //Log.e("DaemonLog", "AegisJobService onStartJob");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        //Log.e("DaemonLog", "AegisJobService onStopJob");
        return false;
    }
}