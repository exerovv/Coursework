package com.example.coursework.ui.favorites.viewmodels.states;

public interface FavoriteSnackBarState {
    class Success implements FavoriteSnackBarState {
        public String operation;

        public Success(String operation) {
            this.operation = operation;
        }
    }
    class Error implements FavoriteSnackBarState {
        public String operation;

        public Error(String operation) {
            this.operation = operation;
        }
    }
}
