package com.example.coursework.domain.repositories;

import com.example.coursework.data.model.MovieResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/popular")
    Single<MovieResponse> fetchPopularMovies(
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("search/movie")
    Single<MovieResponse> fetchMovies(
            @Query("query") String query,
            @Query("page") int page,
            @Query("language") String language
    );

}
