package com.briatka.pavol.skymoviesproject.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.briatka.pavol.skymoviesproject.customobjects.MovieObject;
import com.briatka.pavol.skymoviesproject.roomdatabase.MoviesDatabase;

import java.util.List;

public class MainMoviesViewModel extends AndroidViewModel {

    private LiveData<List<MovieObject>> movieList;

    public MainMoviesViewModel(@NonNull Application application) {
        super(application);
        MoviesDatabase database = MoviesDatabase.getInstance(this.getApplication());
        movieList = database.movieDao().loadAllMovies();
    }

    public LiveData<List<MovieObject>> getMovieList() {
        return movieList;
    }

}
