package com.briatka.pavol.skymoviesproject.clients;

import com.briatka.pavol.skymoviesproject.customobjects.MovieArrayObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieDataClient {

    @GET("/api/movies")
    Call<MovieArrayObject> getMovieData();
}
