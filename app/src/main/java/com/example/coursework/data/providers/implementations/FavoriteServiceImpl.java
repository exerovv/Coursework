package com.example.coursework.data.providers.implementations;

import android.util.Log;

import com.example.coursework.data.mapper.MovieMapper;
import com.example.coursework.data.model.FavoriteReceive;
import com.example.coursework.data.providers.services.FavoriteService;
import com.example.coursework.domain.model.Movie;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//Репозиторий для избранных
public class FavoriteServiceImpl {
    private FavoriteService favoriteService;
    private final String TAG = FavoriteServiceImpl.class.getSimpleName();

    //    private static final String BASE_URL = "http://172.20.10.4:8080/favorites/";
    private static final String BASE_URL = "http://10.0.2.2:8080/favorites/";

    public FavoriteServiceImpl() {
        if (favoriteService == null){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .addInterceptor(logging)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
            favoriteService = retrofit.create(FavoriteService.class);
        }
    }

    //Вставка в избранное
    public Completable insertFavorite(String userId, int movieId, int language, String title, String poster){
        return favoriteService.insertFavorite(new FavoriteReceive(userId, movieId, language, title, poster))
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteFavorite(String userId, int movieId){
        return favoriteService.deleteMovieFromFavorites(userId, movieId)
                .subscribeOn(Schedulers.io());
    }

    //Метод для загрузки избранных для отображения
    public Single<List<Movie>> fetchFavorites(String userId, int  position){
        String language = position == 0 ? "en-US" : "ru-RU";
        return favoriteService.fetchFavorites(userId, language)
                .subscribeOn(Schedulers.io())
                .map(data -> MovieMapper.favoriteToMovieList(data.favorites))
                .onErrorResumeNext(error -> {
                    Log.e(TAG, "Error while fetching favorites", error);
                    return null;
                });
    }

    public Single<List<Integer>> fetchFavoritesIds(String userId){
        return favoriteService.fetchFavoritesIds(userId)
                .subscribeOn(Schedulers.io())
                .map(data -> data.idsList)
                .onErrorResumeNext(error -> {
                    Log.e(TAG, "Error while fetching favorites", error);
                    return null;
                });
    }

}
