package com.example.coursework;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.api.MovieRepository;
import com.example.coursework.utils.MovieState;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieViewModel extends ViewModel {
    private final MovieRepository movieRepository = new MovieRepository();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<MovieState> _state = new MutableLiveData<>();
    public final LiveData<MovieState> state = _state;

    public void fetchPopularMovies(int page) {
        _state.setValue(new MovieState(null, null, true));
        disposable.add(movieRepository.fetchPopularMovies(page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movies -> _state.setValue(new MovieState(movies, null, false)),
                        errors -> _state.setValue(new MovieState(null, "Error: " + errors.getMessage(), false))
                )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
