package com.example.coursework.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//DTO класс в который сериализуется ответ от api для конкретного фильма
public class MovieDTO implements Serializable {
    @SerializedName("id")
    public Integer id;
    @SerializedName("title")
    public String title;
    @SerializedName("overview")
    public String overview;
    @SerializedName("poster_path")
    public String poster_path;
    @SerializedName("vote_average")
    public Double rating;
}
