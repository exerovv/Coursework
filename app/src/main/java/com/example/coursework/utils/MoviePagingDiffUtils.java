package com.example.coursework.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.coursework.domain.model.Movie;

import java.util.Objects;

public class MoviePagingDiffUtils extends DiffUtil.ItemCallback<Movie>{
    @Override
    public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
        return Objects.equals(newItem.getId(), oldItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
        return Objects.equals(newItem, oldItem);
    }
}
