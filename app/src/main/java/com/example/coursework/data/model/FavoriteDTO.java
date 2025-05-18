package com.example.coursework.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FavoriteDTO implements Serializable {
    @SerializedName("movie_id")
    public Integer id;
    @SerializedName("title")
    public String title;
    @SerializedName("poster")
    public String poster_path;
}
