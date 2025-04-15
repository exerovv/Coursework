package com.example.coursework.model;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Movie implements Serializable {
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

    @SuppressLint("DefaultLocale")
    public String getRating() {
        return String.format("%.1f", rating);
    }

    public int getRatingColor(String rating){
        double filmRate = Double.parseDouble(rating);
        if (filmRate < 4.5) return Color.RED;
        else if (filmRate >= 4.5 && filmRate < 6.5) return Color.GRAY;
        else return Color.GREEN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(this.id, movie.id)
                && Objects.equals(this.title, movie.title)
                && Objects.equals(this.rating, movie.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, rating);
    }
}
