package com.example.coursework.utils;

import com.example.coursework.model.Movie;

import java.util.List;

public class MovieState {
    public final List<Movie> movies;
    public final String error;
    public final boolean isLoading;

    public MovieState(List<Movie> movies, String error, boolean isLoading) {
        this.movies = movies;
        this.error = error;
        this.isLoading = isLoading;
    }
}
