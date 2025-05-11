package com.example.coursework.ui.movie.viewmodels.states;

public class SingleMovieState {
    public static int lastPos = 0;

    public static int getLastPos() {
        return lastPos;
    }

    public static void setLastPos(int lastPos) {
        SingleMovieState.lastPos = lastPos;
    }
}


