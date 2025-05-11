package com.example.coursework.data.model;

import com.google.gson.annotations.SerializedName;

import kotlinx.serialization.Serializable;

@Serializable
public class FavoriteReceive {
    @SerializedName("userId") public String userId;
    @SerializedName("movieId") public int movieId;

    public FavoriteReceive(String userId, int movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }
}
