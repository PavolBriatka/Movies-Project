package com.briatka.pavol.skymoviesproject.jobservices;

import android.os.AsyncTask;

import com.briatka.pavol.skymoviesproject.roomdatabase.MoviesDatabase;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MovieDatabaseJobService extends JobService {

    private MoviesDatabase database;
    private AsyncTask backgroundTask;
    private Boolean isRunning = false;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        database = MoviesDatabase.getInstance(getApplicationContext());
        backgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                database.movieDao().deleteAll();
                isRunning = true;
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                isRunning = false;
                jobFinished(jobParameters, false);
            }
        };

        backgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (isRunning) backgroundTask.cancel(true);
        return true;
    }
}
