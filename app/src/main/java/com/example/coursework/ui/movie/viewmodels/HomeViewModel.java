package com.example.coursework.ui.movie.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.coursework.data.paging.PopularMovieDataSource;
import com.example.coursework.domain.model.Movie;
import com.example.coursework.ui.movie.viewmodels.states.PopularMovieState;

import io.reactivex.rxjava3.disposables.Disposable;
import kotlinx.coroutines.CoroutineScope;

public class HomeViewModel extends ViewModel {
    private final PopularMovieState popularMovieState = new PopularMovieState();
    private final MutableLiveData<Boolean> isError = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsError() {
        return isError;
    }

    public void setIsError(boolean flag){
        if (flag != Boolean.TRUE.equals(isError.getValue())){
            isError.setValue(flag);
        }
    }

    private Disposable subscribe;
    private final MutableLiveData<PagingData<Movie>> popularMoviesPagingData = new MutableLiveData<>();
    public LiveData<PagingData<Movie>> getPopularMoviesPagingData(int pos) {
        if (popularMoviesPagingData.getValue() == null || pos != popularMovieState.getLastPos()) {
            if (popularMoviesPagingData.getValue() != null) subscribe.dispose();
            popularMovieState.setLastPos(pos);
            fetchPopularMovies(pos);
        }
        return popularMoviesPagingData;
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
        subscribe = PagingRx.cachedIn(PagingRx.getFlowable(pager), viewModelScope)
                .subscribe(popularMoviesPagingData::postValue);
    }
}
