package com.briatka.pavol.skymoviesproject.roomdatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.briatka.pavol.skymoviesproject.customobjects.MovieObject;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<MovieObject>> loadAllMovies();

    @Query("SELECT * FROM movies WHERE title LIKE :query OR genre LIKE :query")
    LiveData<List<MovieObject>> searchForQueryResults(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieObject movie);

    @Query("DELETE FROM movies")
    void deleteAll();

}
