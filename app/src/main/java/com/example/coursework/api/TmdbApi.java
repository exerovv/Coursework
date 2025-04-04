package com.example.coursework.api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbApi {
    @GET("movie/popular")
    Call<MovieResponse> loadMovies(@Query("api_key") String apiKey);

}
