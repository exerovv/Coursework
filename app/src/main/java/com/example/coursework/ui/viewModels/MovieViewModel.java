package com.example.coursework.ui.viewModels;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;
import com.example.coursework.model.MovieDataSource;
import com.example.coursework.model.Movie;
import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class MovieViewModel extends ViewModel {
    private Flowable<PagingData<Movie>> pagingData;
    private int lastPos;
    public Flowable<PagingData<Movie>> getPagingData(int pos) {
        if (pagingData == null || pos != lastPos){
            lastPos = pos;
            fetchPopularMovies(pos);
        }
        return pagingData;
    }

    public void fetchPopularMovies(int position) {
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Movie> pager = new Pager<>(
                new PagingConfig(
                        20,
                        4,
                        true,
                        20 * 3,
                        20 * 5
                ),
                () -> new MovieDataSource(position)
        );
        pagingData = PagingRx.cachedIn(PagingRx.getFlowable(pager), viewModelScope);
    }
}