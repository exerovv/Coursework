package com.example.coursework.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.example.coursework.domain.model.Movie;

import java.util.List;
import java.util.Objects;

public class MovieDiffUtils extends DiffUtil.Callback {
    private final List<Movie> newList;
    private final List<Movie> oldList;

    public MovieDiffUtils(List<Movie> newList, List<Movie> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldList.get(oldItemPosition).getId(), newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldList.get(oldItemPosition), newList.get(newItemPosition));
    }
}
