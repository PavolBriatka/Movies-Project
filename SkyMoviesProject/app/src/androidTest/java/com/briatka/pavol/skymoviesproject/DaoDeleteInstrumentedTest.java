package com.briatka.pavol.skymoviesproject;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.briatka.pavol.skymoviesproject.customobjects.MovieObject;
import com.briatka.pavol.skymoviesproject.roomdatabase.MovieDao;
import com.briatka.pavol.skymoviesproject.roomdatabase.MoviesDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class DaoDeleteInstrumentedTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private MoviesDatabase database;
    private MovieDao dao;


    @Mock
    private android.arch.lifecycle.Observer<List<MovieObject>> observer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Context context = InstrumentationRegistry.getTargetContext();
        database = Room.inMemoryDatabaseBuilder(context, MoviesDatabase.class)
                .allowMainThreadQueries().build();
        dao = database.movieDao();
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

    @Test
    public void delete() throws  Exception{


        dao.loadAllMovies().observeForever(observer);
        dao.deleteAll();
        verify(observer).onChanged(Collections.<MovieObject>emptyList());

    }
}
