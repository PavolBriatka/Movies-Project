package com.briatka.pavol.skymoviesproject.roomdatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.briatka.pavol.skymoviesproject.customobjects.MovieObject;

@Database(entities = MovieObject.class, version = 2, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "SkyProjectDatabase";
    private static MoviesDatabase dbInstance;

    public static MoviesDatabase getInstance(Context context) {
        if (dbInstance == null) {
            synchronized (LOCK) {
                dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MoviesDatabase.class, MoviesDatabase.DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        return dbInstance;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS 'movies' ('id' INTEGER NOT NULL, "
                    + "'title' TEXT, "
                    + "'year' TEXT, "
                    + "'genre' TEXT, "
                    + "'poster' TEXT, PRIMARY KEY('id'))");

        }
    };


    public abstract MovieDao movieDao();

}
