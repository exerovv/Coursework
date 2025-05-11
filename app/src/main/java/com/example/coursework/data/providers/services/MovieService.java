package com.example.coursework.data.providers.services;

import com.example.coursework.data.model.MovieDTO;
import com.example.coursework.data.model.MoviesDTO;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/popular")
    Single<MoviesDTO> fetchPopularMovies(
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("search/movie")
    Single<MoviesDTO> fetchMovies(
            @Query("query") String query,
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("movie/{id}")
    Single<MovieDTO> fetchSingleMovie(
            @Path("id") int id,
            @Query("language") String language
    );

}
