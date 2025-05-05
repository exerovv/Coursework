package com.example.coursework.ui.movie.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.coursework.data.paging.PopularMovieDataSource;
import com.example.coursework.domain.model.Movie;
import com.example.coursework.ui.movie.viewmodels.states.PopularMovieState;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class HomeViewModel extends ViewModel {
    private final PopularMovieState popularMovieState = new PopularMovieState();
    private Flowable<PagingData<Movie>> popularMoviesPagingData;

    public Flowable<PagingData<Movie>> getPopularMoviesPagingData(int pos) {
        if (popularMoviesPagingData == null || pos != popularMovieState.getLastPos()) {
            popularMovieState.setLastPos(pos);
            fetchPopularMovies(pos);
        }
        return popularMoviesPagingData;
    }

    private void fetchPopularMovies(int position) {
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Movie> pager = new Pager<>(
                new PagingConfig(
                        20,
                        4,
                        false,
                        20 * 3,
                        20 * 5
                ),
                () -> new PopularMovieDataSource(position)
        );
        popularMoviesPagingData = PagingRx.cachedIn(PagingRx.getFlowable(pager), viewModelScope);
    }
}
