package com.example.coursework.data.providers.implementations;

import com.example.coursework.data.model.FavoriteReceive;
import com.example.coursework.data.providers.services.FavoriteService;

import io.reactivex.rxjava3.core.Completable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoriteServiceImpl {
    private FavoriteService favoriteService;

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

    public Completable insertFavorite(String userId, int movieId){
        return favoriteService.insertFavorite(new FavoriteReceive(userId, movieId));
    }
}
