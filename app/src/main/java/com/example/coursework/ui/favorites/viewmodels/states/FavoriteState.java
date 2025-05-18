package com.example.coursework.ui.favorites.viewmodels.states;

public interface FavoriteState {
    class Success implements FavoriteState{
    }

    class Error implements FavoriteState{}

    class Loading implements FavoriteState {}

    class Default implements FavoriteState {}
}
