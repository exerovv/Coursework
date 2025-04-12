package com.example.coursework.model.service;

import com.example.coursework.model.MovieResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/popular")
    Single<MovieResponse> fetchPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

}
