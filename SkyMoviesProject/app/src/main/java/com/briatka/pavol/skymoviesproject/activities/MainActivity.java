package com.briatka.pavol.skymoviesproject.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.briatka.pavol.skymoviesproject.R;
import com.briatka.pavol.skymoviesproject.adapters.MovieAdapter;
import com.briatka.pavol.skymoviesproject.clients.MovieDataClient;
import com.briatka.pavol.skymoviesproject.customobjects.MovieArrayObject;
import com.briatka.pavol.skymoviesproject.customobjects.MovieObject;
import com.briatka.pavol.skymoviesproject.executors.AppExecutors;
import com.briatka.pavol.skymoviesproject.jobservices.DatabaseMaintenanceSchedule;
import com.briatka.pavol.skymoviesproject.roomdatabase.MoviesDatabase;
import com.briatka.pavol.skymoviesproject.viewmodels.MainMoviesViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.options)
    RadioGroup optionsGroup;
    @BindView(R.id.title_option)
    RadioButton titleOption;
    @BindView(R.id.genre_option)
    RadioButton genreOption;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private MovieAdapter adapter;

    private static final String BASE_URL = "https://movies-sample.herokuapp.com";

    private MoviesDatabase database;
    private RecyclerView.LayoutManager layoutManager;
    private int searchOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        searchOption = optionsGroup.getCheckedRadioButtonId();
        database = MoviesDatabase.getInstance(getApplicationContext());

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new GridLayoutManager(this, 5);
        }

        recyclerView.setLayoutManager(layoutManager);

        adapter = new MovieAdapter(null);
        recyclerView.setAdapter(adapter);

        MainMoviesViewModel viewModel = ViewModelProviders.of(this).get(MainMoviesViewModel.class);
        final LiveData<List<MovieObject>> movieList = viewModel.getMovieList();
        movieList.observe(this, new Observer<List<MovieObject>>() {
            @Override
            public void onChanged(@Nullable List<MovieObject> movieObjects) {
                if (movieObjects != null && movieObjects.size() > 0) {
                    adapter.setData(movieObjects);
                    recyclerView.setAdapter(adapter);
                    enableSearching(movieObjects);
                } else {

                    progressBar.setVisibility(View.VISIBLE);
                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create());

                    Retrofit retrofit = builder.build();

                    MovieDataClient client = retrofit.create(MovieDataClient.class);
                    Call<MovieArrayObject> call = client.getMovieData();

                    call.enqueue(new Callback<MovieArrayObject>() {
                        @Override
                        public void onResponse(Call<MovieArrayObject> call, Response<MovieArrayObject> response) {
                            List<MovieObject> dataList = response.body().getData();
                            progressBar.setVisibility(View.GONE);
                            insertMovies(dataList);
                            adapter.setData(dataList);
                            recyclerView.setAdapter(adapter);
                            enableSearching(dataList);
                        }

                        @Override
                        public void onFailure(Call<MovieArrayObject> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("Error:", t.getMessage());

                        }
                    });
                }
            }
        });


        DatabaseMaintenanceSchedule.scheduleMaintenance(this);

    }

    private void enableSearching(final List<MovieObject> data) {

        optionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                searchOption = id;
                String query = searchView.getQuery().toString();
                if (!query.isEmpty()) {
                    adapter.setData(handleSearch(query, data));
                    recyclerView.setAdapter(adapter);
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    adapter.setData(handleSearch(newText, data));
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.setData(data);
                    recyclerView.setAdapter(adapter);
                }
                return true;
            }
        });

    }

    private List<MovieObject> handleSearch(String queryString, List<MovieObject> list) {
        List<MovieObject> results = new ArrayList<>();
        for (MovieObject item : list) {
            if (searchOption == titleOption.getId()) {
                if (item.getTitle().toLowerCase().contains(queryString.toLowerCase())) {
                    results.add(item);
                }
            } else {
                if (item.getGenre().toLowerCase().contains(queryString.toLowerCase())) {
                    results.add(item);
                }
            }
        }

        return results;
    }

    private void insertMovies(final List<MovieObject> data) {
        for (int i = 0; i < data.size(); i++) {
            final MovieObject movie = data.get(i);
            movie.setId(i + 1);
            AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    database.movieDao().insertMovie(movie);
                }
            });
        }
    }
}
