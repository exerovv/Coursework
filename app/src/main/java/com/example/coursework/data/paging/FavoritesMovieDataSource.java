//package com.example.coursework.data.paging;
//
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.paging.PagingSource;
//import androidx.paging.PagingState;
//
//import com.example.coursework.data.mapper.MovieMapper;
//import com.example.coursework.data.model.MovieDTO;
//import com.example.coursework.data.providers.implementations.FavoriteServiceImpl;
//import com.example.coursework.domain.model.Movie;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//
//import io.reactivex.rxjava3.schedulers.Schedulers;
//import kotlin.coroutines.Continuation;
//
//public class FavoritesMovieDataSource extends PagingSource<Integer, Movie> {
//    private final String TAG = FavoritesMovieDataSource.class.getSimpleName();
//    public final FavoriteServiceImpl favoriteService = new FavoriteServiceImpl();
//    private final int spinnerPos;
//    private final String user;
//
//    public FavoritesMovieDataSource(int spinnerPos, String user) {
//        this.spinnerPos = spinnerPos;
//        this.user = user;
//    }
//
//    @Nullable
//    @Override
//    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
//        Integer anchorPosition = pagingState.getAnchorPosition();
//        if (anchorPosition == null) {
//            return null;
//        }
//        LoadResult.Page<Integer, Movie> page = pagingState.closestPageToPosition(anchorPosition);
//        if (page == null) {
//            return null;
//        }
//        Integer currentPage = null;
//        if (page.getPrevKey() != null) {
//            currentPage = page.getPrevKey() + 1;
//        } else {
//            if (page.getNextKey() != null) {
//                currentPage = page.getNextKey() - 1;
//            }
//        }
//        return currentPage;
//    }
//
//    @Nullable
//    @Override
//    public Object load(@NonNull LoadParams<Integer> loadParams, @NonNull Continuation<? super LoadResult<Integer, Movie>> continuation) {
//        int currentPage = loadParams.getKey() != null ? loadParams.getKey() : 1;
//        String language = spinnerPos == 0 ? "en-US" : "ru-RU";
//        return favoriteService.fetchFavorites(user, language)
//                .subscribeOn(Schedulers.io())
//                .map(response -> {
//                            List<Movie> movie = MovieMapper.toMovieFavoriteList(response);
//                            int totalPages = response.getTotalPages();
//
//                            Integer prevKey = currentPage == 1 ? null : currentPage - 1;
//                            Integer nextKey = currentPage >= totalPages ? null : currentPage + 1;
//
//                            if (movieList.isEmpty()) {
//                                Log.w(TAG, "Movie list is empty!");
//                            }
//
//                            return (LoadResult<Integer, Movie>) new LoadResult.Page<>(
//                                    Objects.requireNonNullElse(movieList, Collections.emptyList()),
//                                    prevKey,
//                                    nextKey
//                            );
//                        }
//                ).onErrorReturn(e -> {
//                    Log.e(TAG, "Error loading page: " + currentPage, e);
//                    return new LoadResult.Error<>(e);
//                });
//    }
//}
