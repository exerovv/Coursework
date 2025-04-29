package com.example.coursework.ui.movie.viewmodels.states;
public class SearchMovieState {
    int lastPos = 0;
    String lastQuery = null;

    public int getLastPos() {
        return lastPos;
    }

    public void setLastPos(int lastPos) {
        this.lastPos = lastPos;
    }

    public String getLastQuery() {
        return lastQuery;
    }

    public void setLastQuery(String lastQuery) {
        this.lastQuery = lastQuery;
    }
}

