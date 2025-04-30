package com.example.coursework.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesDTO {
    @SerializedName("results")
    public List<MovieDTO> movies;
    @SerializedName("total_pages")
    public int totalPages;

    public int getTotalPages() {
        return totalPages;
    }

    public List<MovieDTO> getMovies() {
        return movies;
    }
}
