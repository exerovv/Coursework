package com.example.coursework.ui.viewModels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.coursework.model.PopularMovieDataSource;
import com.example.coursework.model.Movie;
import com.example.coursework.model.SearchedMovieDataSource;
import com.example.coursework.utils.PopularMovieState;
import com.example.coursework.utils.SearchMovieState;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class MovieViewModel extends ViewModel {

    private final PopularMovieState popularMovieState = new PopularMovieState();
    private final SearchMovieState searchMovieState = new SearchMovieState();
    private final MutableLiveData<String> queryLiveData = new MutableLiveData<>(null);

    public LiveData<String> getQueryLiveData() {
        return queryLiveData;
    }

    public void setQuery(String query) {
        if (query != null && !query.equals(getQueryLiveData().getValue()))
            queryLiveData.setValue(query);
    }

    private Flowable<PagingData<Movie>> popularMoviesPagingData;

    public Flowable<PagingData<Movie>> getPopularMoviesPagingData(int pos) {
        if (popularMoviesPagingData == null || pos != popularMovieState.getLastPos()) {
            popularMovieState.setLastPos(pos);
            fetchPopularMovies(pos);
        }
        return popularMoviesPagingData;
    }

    private Flowable<PagingData<Movie>> searchedMoviesPagingData;

    public Flowable<PagingData<Movie>> getSearchedMoviesPagingData(int pos, String query) {
        if (searchedMoviesPagingData == null ||
                pos != searchMovieState.getLastPos() ||
                !query.equals(searchMovieState.getLastQuery())) {
            searchMovieState.setLastPos(pos);
            searchMovieState.setLastQuery(query);
            searchMovies(pos, query);
        }
        return searchedMoviesPagingData;
    }


    public void fetchPopularMovies(int position) {
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

    public void searchMovies(int position, String query) {
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Movie> pager = new Pager<>(
                new PagingConfig(
                        20,
                        2,
                        false,
                        20,
                        20 * 2
                ),
                () -> new SearchedMovieDataSource(position, query)
        );
        searchedMoviesPagingData = PagingRx.cachedIn(PagingRx.getFlowable(pager), viewModelScope);
    }
}