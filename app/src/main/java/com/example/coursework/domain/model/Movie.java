package com.example.coursework.domain.model;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

public class Movie implements Serializable {
    private final Integer id;
    private final String title;
    private final String overview;
    private final String poster_path;
    private final Double rating;

    public Movie(Integer id, String title, String overview, String poster_path, Double rating) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getRating() {
        return String.format(Locale.US, "%.1f", rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) &&
                Objects.equals(title, movie.title)  &&
                Objects.equals(rating, movie.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, overview, poster_path, rating);
    }
}
