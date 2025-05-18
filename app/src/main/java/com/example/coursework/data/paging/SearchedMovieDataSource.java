package com.example.coursework.data.paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.coursework.data.mapper.MovieMapper;
import com.example.coursework.data.model.MovieDTO;
import com.example.coursework.domain.model.Movie;
import com.example.coursework.data.providers.implementations.MovieServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
//Класс - DataSource для paging, определяет откуда и как будут получаться данные для страницы поиска
public class SearchedMovieDataSource extends RxPagingSource<Integer, Movie> {
    //Экземпляр репозитория
    private final MovieServiceImpl movieService = new MovieServiceImpl();
    //Переменная, которая хранит позицию в спиннере, отвечающая за язык приложения
    private final int spinnerPos;
    private final String query;

    private final String TAG = "SEARCH_PAGING";

    public SearchedMovieDataSource(int spinnerPos, String query) {
        this.spinnerPos = spinnerPos;
        this.query = query;
    }

    //Метод для получения RefreshKey на случай, если мы, например, пролистали несколько страниц и
    //paging необходимо получить актуальный ключ
    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        //Последняя загруженная позиция
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) return null;
        //Ближайшая к последней загруженной странице страница
        LoadResult.Page<Integer, Movie> page = pagingState.closestPageToPosition(anchorPosition);
        if (page == null) return null;
        //Получаем корректную текущую страницу
        Integer currentPage = null;
        if (page.getPrevKey() != null) {
            currentPage = page.getPrevKey() + 1;
        } else {
            if (page.getNextKey() != null) {
                currentPage = page.getNextKey() - 1;
            }
        }
        return currentPage;
    }

    //Метод для непосредственной загрузки данных
    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        //Текущая страница
        int currentPage = loadParams.getKey() != null ? loadParams.getKey() : 1;
        String language = spinnerPos == 0 ? "en-US" : "ru-RU";
        Log.d(TAG, "Loading page: " + currentPage);

        //Возврщает либо список фильмов, либо ошибку
        return movieService.fetchMovies(query, currentPage, language)
                .subscribeOn(Schedulers.io())
                .map(movieResponse -> {
                            List<MovieDTO> dtoList = movieResponse.getMovies();
                            List<Movie> movies = MovieMapper.toMovieList(dtoList);
                            int totalPages = movieResponse.getTotalPages();
                            Log.d(TAG, "Movies count: " + movies.size());

                            Integer prevKey = currentPage == 1 ? null : currentPage - 1;
                            Integer nextKet = currentPage < totalPages ? currentPage + 1 : null;

                            if (movies.isEmpty()) {
                                Log.w(TAG, "Movie list is empty!");
                            }

                            return (LoadResult<Integer, Movie>) new LoadResult.Page<>(
                                    Objects.requireNonNullElse(movies, Collections.emptyList()),
                                    prevKey,
                                    nextKet
                            );
                        }
                ).onErrorReturn(error -> {
                    Log.e(TAG, "Error loading page: " + currentPage, error);
                    return new LoadResult.Error<>(error);
                });
    }
}
