package com.example.coursework.data.implementations;

import com.example.coursework.data.implementations.interceptors.MovieInterceptor;
import com.example.coursework.data.model.MovieResponse;
import com.example.coursework.domain.repositories.MovieService;

import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieServiceImpl {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static MovieService tmdbApi;

    public MovieServiceImpl(){
        if (tmdbApi == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new MovieInterceptor())
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
            tmdbApi = retrofit.create(MovieService.class);
        }
    }

    public Single<MovieResponse> fetchPopularMovies(int page, String language){
        return tmdbApi.fetchPopularMovies(page, language)
                .onErrorResumeNext(Single::error);
    }

    public Single<MovieResponse> fetchMovies(String query, int page, String language){
        return tmdbApi.fetchMovies(query, page, language)
                .onErrorResumeNext(Single::error);
    }
}
