package com.example.coursework.data.paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.coursework.data.mapper.MovieMapper;
import com.example.coursework.domain.model.Movie;
import com.example.coursework.data.providers.implementations.MovieServiceImpl;
import com.example.coursework.data.model.MovieDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

//Класс - DataSource для paging, определяет откуда и как будут получаться данные для главной страницы
public class PopularMovieDataSource extends RxPagingSource<Integer, Movie> {
    //Экземпляр репозитория
    private final MovieServiceImpl movieRepository = new MovieServiceImpl();
    //Переменная, которая хранит позицию в спиннере, отвечающая за язык приложения
    private final int spinnerPos;

    public PopularMovieDataSource(int spinnerPos) {
        this.spinnerPos = spinnerPos;
    }

    private final String TAG = PopularMovieDataSource.class.getSimpleName();

    //Метод для непосредственной загрузки данных
    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        //Текущая страница
        int currentPage = loadParams.getKey() != null ? loadParams.getKey() : 1;
        Log.d(TAG, "Loading page: " + currentPage);

        String language = spinnerPos == 0 ? "en-US" : "ru-RU";

        //Возврщает либо список фильмов, либо ошибку
        return movieRepository.fetchPopularMovies(currentPage, language)
                .subscribeOn(Schedulers.io())
                .map(response -> {
                            List<MovieDTO> dtoList = response.getMovies();
                            List<Movie> movies = MovieMapper.toMovieList(dtoList);
                            int totalPages = response.getTotalPages();
                            Log.d(TAG, "Movies count: " + movies.size());

                            Integer prevKey = currentPage == 1 ? null : currentPage - 1;
                            Integer nextKey = currentPage >= totalPages ? null : currentPage + 1;


                            if (movies.isEmpty()) {
                                Log.w(TAG, "Movie list is empty!");
                            }

                            return (LoadResult<Integer, Movie>) new LoadResult.Page<>(
                                    Objects.requireNonNullElse(movies, Collections.emptyList()),
                                    prevKey,
                                    nextKey
                            );
                        }
                ).onErrorReturn(e -> {
                    Log.e(TAG, "Error loading page: " + currentPage, e);
                    return new LoadResult.Error<>(e);
                });

    }

    //Метод для получения RefreshKey на случай, если мы, например, пролистали несколько страниц и
    //paging необходимо получить актуальный ключ
    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        //Последняя загруженная позиция
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }
        //Ближайшая к последней загруженной странице страница
        LoadResult.Page<Integer, Movie> page = pagingState.closestPageToPosition(anchorPosition);
        if (page == null) {
            return null;
        }
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
}
