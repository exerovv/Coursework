package com.example.coursework.data.providers.services;

import com.example.coursework.data.model.FavoriteReceive;
import com.example.coursework.data.model.FavoritesDTO;
import com.example.coursework.data.model.MovieIDsDTO;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

//Ретрофит интерфейс для работы с избарнными, в котором описание тип запроса и необходимые параметры
public interface FavoriteService {
    @POST("add")
    Completable insertFavorite(
            @Body FavoriteReceive data
    );

    @GET("{userId}")
    Single<FavoritesDTO> fetchFavorites(
            @Path("userId") String userId,
            @Query("language") String language
    );

    @GET("ids/{userId}")
    Single<MovieIDsDTO> fetchFavoritesIds(
            @Path("userId") String userId
    );


    @DELETE("{userId}/{movieId}")
    Completable deleteMovieFromFavorites(
            @Path("userId") String userId,
            @Path("movieId") int movieId
    );
}
