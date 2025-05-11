package com.example.coursework.ui.movie.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.providers.implementations.MovieServiceImpl;
import com.example.coursework.ui.movie.viewmodels.states.SingleMovieState;
import com.example.coursework.ui.movie.viewmodels.states.SingleMovieUiState;

import io.reactivex.rxjava3.disposables.Disposable;

public class DetailViewModel extends ViewModel {
    private final String TAG = DetailViewModel.class.getCanonicalName();
    private final MovieServiceImpl movieService = new MovieServiceImpl();
    private final MutableLiveData<SingleMovieUiState> singleMovieLiveData = new MutableLiveData<>(new SingleMovieUiState.Default());
    public LiveData<SingleMovieUiState> getSingleMovieLiveData(int id, int pos){
        if ((singleMovieLiveData.getValue() instanceof SingleMovieUiState.Default)
                || pos != SingleMovieState.getLastPos()){
            if (!(singleMovieLiveData.getValue() instanceof SingleMovieUiState.Default)) subscribe.dispose();
            fetchSingleMovie(id, pos);
            SingleMovieState.setLastPos(pos);
        }
        return singleMovieLiveData;
    }
    Disposable subscribe;
    public void fetchSingleMovie(int id, int pos){
        singleMovieLiveData.postValue(new SingleMovieUiState.Loading());
        subscribe = movieService.fetchSingleMovie(id, pos)
                .subscribe(
                        movie -> singleMovieLiveData.postValue(new SingleMovieUiState.Success(movie)),
                        error -> {
                            Log.d(TAG, "error");
                            singleMovieLiveData.postValue(new SingleMovieUiState.Error());
                        }
                );
    }
}