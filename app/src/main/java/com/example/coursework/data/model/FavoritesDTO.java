package com.example.coursework.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import kotlinx.serialization.Serializable;

@Serializable
public class FavoritesDTO {
    @SerializedName("total_page") public int totalPage;
    @SerializedName("result") public List<FavoriteDTO> favorites;
}
