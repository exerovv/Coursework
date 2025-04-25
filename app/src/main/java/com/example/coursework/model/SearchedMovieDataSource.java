package com.example.coursework.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;

import com.example.coursework.model.service.impl.MovieServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.coroutines.Continuation;

public class SearchedMovieDataSource extends PagingSource<Integer, Movie> {
    private final MovieServiceImpl movieService = new MovieServiceImpl();
    private final int spinnerPos;
    private final String query;

    private final String TAG = "PAGING";

    public SearchedMovieDataSource(int spinnerPos, String query) {
        this.spinnerPos = spinnerPos;
        this.query = query;
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) return null;
        LoadResult.Page<Integer, Movie> page = pagingState.closestPageToPosition(anchorPosition);
        if (page == null) return null;
        Integer currentPage = null;
        if (page.getPrevKey() != null){
            currentPage = page.getPrevKey() + 1;
        }
        else{
            if (page.getNextKey() != null) {
                currentPage = page.getNextKey() - 1;
            }
        }
        return currentPage;
    }

    @Nullable
    @Override
    public Object load(@NonNull LoadParams<Integer> loadParams, @NonNull Continuation<? super LoadResult<Integer, Movie>> continuation) {
        int currentPage = loadParams.getKey() != null ? loadParams.getKey() : 1;
        String language = spinnerPos == 0 ? "en-US" : "ru-RU";

        return movieService.fetchMovies(query, currentPage, language)
                .subscribeOn(Schedulers.io())
                .map(movieResponse -> {
                    List<Movie> movieList = movieResponse.getMovies();
                    int totalPages = movieResponse.getTotalPages();

                    Integer prevKey = currentPage == 1 ? null : currentPage - 1;
                    Integer nextKet = currentPage < totalPages ? currentPage + 1 : null;

                    if (movieList == null || movieList.isEmpty()){
                        Log.w(TAG, "Movie list is empty!");
                    }

                    return (LoadResult<Integer, Movie>) new LoadResult.Page<>(
                            Objects.requireNonNullElse(movieList, Collections.emptyList()),
                            prevKey,
                            nextKet
                    );
                }).onErrorReturn(error -> {
                    Log.e(TAG, "Error loading page: " + currentPage, error);
                    return new LoadResult.Error<>(error);
                });

    }
}
