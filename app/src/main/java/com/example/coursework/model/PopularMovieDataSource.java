package com.example.coursework.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.coursework.model.service.impl.MovieServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PopularMovieDataSource extends RxPagingSource<Integer, Movie> {
    private final MovieServiceImpl movieRepository = new MovieServiceImpl();
    private final int spinnerPos;

    public PopularMovieDataSource(int spinnerPos) {
        this.spinnerPos = spinnerPos;
    }

    private final String TAG = "PAGING";

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        int currentPage = loadParams.getKey() != null ? loadParams.getKey() : 1;
        Log.d(TAG, "Loading page: " + currentPage);

        String language = spinnerPos == 0 ? "en-US" : "ru-RU";

        return movieRepository.fetchPopularMovies(currentPage, language)
                .subscribeOn(Schedulers.io())
                .map(response -> {
                            List<Movie> movies = response.getMovies();
                            int totalPages = response.getTotalPages();
                            Log.d(TAG, "Movies count: " + (movies != null ? movies.size() : 0));

                            Integer prevKey = currentPage == 1 ? null : currentPage - 1;
                            Integer nextKey = currentPage >= totalPages ? null : currentPage + 1;


                            if (movies == null || movies.isEmpty()){
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

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }
        LoadResult.Page<Integer, Movie> page = pagingState.closestPageToPosition(anchorPosition);
        if (page == null) {
            return null;
        }
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
