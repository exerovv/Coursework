package com.example.coursework.ui.movie.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.coursework.data.implementations.MovieServiceImpl;
import com.example.coursework.domain.model.Movie;
import com.example.coursework.ui.movie.viewmodels.states.SingleMovieState;

import io.reactivex.rxjava3.core.Single;
public class DetailViewModel extends ViewModel {
    private final MovieServiceImpl movieService = new MovieServiceImpl();
    private final SingleMovieState singleMovieState = new SingleMovieState();

    private Single<Movie> singleMovieData;

    public Single<Movie> getSingleMovieData(int id, int pos){
        if (singleMovieData == null || pos != singleMovieState.getLastPos()){
            fetchSingleMovie(id, pos);
            singleMovieState.setLastPos(pos);
        }
        return singleMovieData;
    }
    private void fetchSingleMovie(int id, int pos){
        singleMovieData = movieService.fetchSingleMovie(id, pos);
    }
}