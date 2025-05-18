package com.example.coursework.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//DTO класс в который сериализуется ответ от api
public class MoviesDTO {
    @SerializedName("results")
    public List<MovieDTO> movies;
    //Количество страниц нужно для корректной работы paging
    @SerializedName("total_pages")
    public int totalPages;

    public int getTotalPages() {
        return totalPages;
    }

    public List<MovieDTO> getMovies() {
        return movies;
    }
}
