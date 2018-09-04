package com.briatka.pavol.skymoviesproject.jobservices;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

public class DatabaseMaintenanceSchedule {

    private static final String JOB_TAG = "database_maintenance";
    private static final int INTERVAL_START_SECONDS = 600;
    private static final int INTERVAL_END_SECONDS = 610;

    private static Boolean sInitialized = false;

    synchronized public static void scheduleMaintenance(@NonNull final Context context) {
        if (sInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(driver);
        Job maintenanceJob = jobDispatcher.newJobBuilder()
                .setService(MovieDatabaseJobService.class)
                .setTag(JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(INTERVAL_START_SECONDS,
                        INTERVAL_END_SECONDS))
                .setReplaceCurrent(true)
                .build();

        jobDispatcher.mustSchedule(maintenanceJob);
        sInitialized = true;
    }
}
