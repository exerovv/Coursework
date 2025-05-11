package com.example.coursework.ui.movie.viewmodels.states;

import com.example.coursework.domain.model.Movie;

public interface SingleMovieUiState {
    class Loading implements SingleMovieUiState{}
    class Success implements SingleMovieUiState {
        public Movie data;

        public Success(Movie data) {
            this.data = data;
        }
    }

    class Error implements SingleMovieUiState {}
    class Default implements SingleMovieUiState {}
}
