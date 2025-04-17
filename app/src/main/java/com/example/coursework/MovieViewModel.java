package com.example.coursework;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;
import com.example.coursework.model.MovieDataSource;
import com.example.coursework.model.service.impl.MovieServiceImpl;
import com.example.coursework.model.Movie;
import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class MovieViewModel extends ViewModel {
    private final MovieServiceImpl movieRepository = new MovieServiceImpl();
    private Flowable<PagingData<Movie>> pagingData;
    public Flowable<PagingData<Movie>> getPagingData() {
        if (pagingData == null){
            fetchPopularMovies();
        }
        return pagingData;
    }

    public void fetchPopularMovies() {
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Movie> pager = new Pager<>(
                new PagingConfig(
                        20,
                        4,
                        true,
                        20 * 3,
                        20 * 5
                ),
                () -> new MovieDataSource(movieRepository)
        );
        pagingData = PagingRx.cachedIn(PagingRx.getFlowable(pager), viewModelScope);
    }
}