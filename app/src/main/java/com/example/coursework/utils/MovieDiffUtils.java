package com.example.coursework.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.example.coursework.model.Movie;

import java.util.List;
import java.util.Objects;

public class MovieDiffUtils extends DiffUtil.Callback{
    List<Movie> oldList;
    List<Movie> newList;

    public MovieDiffUtils(List<Movie> oldList, List<Movie> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        if (oldList != null){
            return oldList.size();
        }
        return 0;
    }

    @Override
    public int getNewListSize() {
        if (newList != null){
            return newList.size();
        }
        return 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newList.get(newItemPosition).getId(), oldList.get(oldItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).equals(oldList.get(oldItemPosition));
    }
}
