package com.example.coursework.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Movie {
    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("vote_average")
    private Double rating;
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public Double getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id)
                && Objects.equals(title, movie.title)
                && Objects.equals(overview, movie.overview)
                && Objects.equals(poster_path, movie.poster_path)
                && Objects.equals(rating, movie.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, overview, poster_path, rating);
    }
}
