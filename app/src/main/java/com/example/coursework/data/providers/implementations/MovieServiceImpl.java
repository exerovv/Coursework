package com.example.coursework.data.providers.implementations;

import android.util.Log;

import com.example.coursework.data.providers.implementations.interceptors.MovieInterceptor;
import com.example.coursework.data.mapper.MovieMapper;
import com.example.coursework.data.model.MoviesDTO;
import com.example.coursework.domain.model.Movie;
import com.example.coursework.data.providers.services.MovieService;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//Репозиторий для получения фильмов для главной, детальной и страницы поиска
public class MovieServiceImpl{
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private final String TAG = MovieServiceImpl.class.getSimpleName();

    private static MovieService movieService;

    public MovieServiceImpl(){
        if (movieService == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new MovieInterceptor())
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
            movieService = retrofit.create(MovieService.class);
        }
    }
    //Метод для запроса фильмов для главной страницы
    public Single<MoviesDTO> fetchPopularMovies(int page, String language){
        return movieService.fetchPopularMovies(page, language)
                .onErrorResumeNext(Single::error);
    }
    //Метод для получение фильмов по поисковому запросу
    public Single<MoviesDTO> fetchMovies(String query, int page, String language){
        return movieService.fetchMovies(query, page, language)
                .onErrorResumeNext(Single::error);
    }
    //Метод для получения конкретного фильма для детальной страницы
    public Single<Movie> fetchSingleMovie(int id, int pos) {
        String language = pos == 0 ? "en-US" : "ru-RU";
        return movieService.fetchSingleMovie(id, language)
                .subscribeOn(Schedulers.io())
                .map(MovieMapper::toMovie)
                .onErrorResumeNext(throwable -> {
                    Log.e(TAG, "Error while fetching the film", throwable);
                    return null;
                });
    }
}
