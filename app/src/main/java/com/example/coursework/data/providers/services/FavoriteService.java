package com.example.coursework.data.providers.services;

import com.example.coursework.data.model.FavoriteReceive;
import com.example.coursework.data.model.MoviesDTO;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoriteService {
    @POST("add")
    Completable insertFavorite(
            @Body FavoriteReceive data
    );

    @GET("{userId}")
    Single<MoviesDTO> fetchFavorites(
            @Path("userId") String userId
    );

    @DELETE("{userId}/{movieId}")
    Completable deleteMovieFromFavorites(
            @Path("userId") String userId,
            @Path("movieId") int movieId
    );
}
