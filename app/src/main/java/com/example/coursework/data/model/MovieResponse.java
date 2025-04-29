package com.example.coursework.data.model;

import com.example.coursework.domain.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    @SerializedName("results")
    private List<Movie> movies;
    @SerializedName("total_pages")
    private int totalPages;

    public int getTotalPages() {
        return totalPages;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
