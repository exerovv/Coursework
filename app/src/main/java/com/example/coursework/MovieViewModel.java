package com.example.coursework;

import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;
import com.example.coursework.model.MovieDataSource;
import com.example.coursework.model.service.impl.MovieServiceImpl;
import com.example.coursework.model.Movie;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieViewModel extends ViewModel {
    private final MovieServiceImpl movieRepository = new MovieServiceImpl();
    private Flowable<PagingData<Movie>> pagingData;
    public Flowable<PagingData<Movie>> getPagingData() {
        return pagingData.observeOn(AndroidSchedulers.mainThread());
    }

    public MovieViewModel(){
        fetchPopularMovies();
    }

    public void fetchPopularMovies() {
        Pager<Integer, Movie> pager = new Pager<>(
                new PagingConfig(
                        20,
                        4,
                        true,
                        20 * 2,
                        20 * 3
                ),
                () -> new MovieDataSource(movieRepository)
        );

        pagingData = PagingRx
                .getFlowable(pager)
                .subscribeOn(Schedulers.io())
                .replay(1, true)
                .refCount();
    }
}