package com.example.coursework.model.service.impl;

import com.example.coursework.model.MovieResponse;
import com.example.coursework.model.service.MovieService;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieServiceImpl {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "4b104e9ab7bbf8b901f150ea9dd1eeee";
    private static MovieService tmdbApi;

    public MovieServiceImpl(){
        if (tmdbApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
            tmdbApi = retrofit.create(MovieService.class);
        }
    }

    public Single<MovieResponse> fetchPopularMovies(int page, String language){
        return tmdbApi.fetchPopularMovies(API_KEY, page, language)
                .onErrorResumeNext(Single::error);
    }

    public Single<MovieResponse> fetchMovies(String query, int page, String language){
        return tmdbApi.fetchMovies(API_KEY, query, page, language)
                .onErrorResumeNext(Single::error);
    }
}
