package com.example.coursework.data.model;

import com.google.gson.annotations.SerializedName;

import kotlinx.serialization.Serializable;

//Дата класс для использования в body запроса на добавление в избранные
@Serializable
public class FavoriteReceive {
    @SerializedName("userId") public String userId;
    @SerializedName("movieId") public int movieId;
    @SerializedName("language") public int language;
    @SerializedName("title") public String title;
    @SerializedName("poster") public String poster;

    public FavoriteReceive(String userId, int movieId, int language, String title, String poster) {
        this.userId = userId;
        this.movieId = movieId;
        this.language = language;
        this.title = title;
        this.poster = poster;
    }
}
