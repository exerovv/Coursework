package com.example.coursework.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import kotlinx.serialization.Serializable;

@Serializable
public class MovieIDsDTO {
    @SerializedName("result") public List<Integer> idsList;
}
