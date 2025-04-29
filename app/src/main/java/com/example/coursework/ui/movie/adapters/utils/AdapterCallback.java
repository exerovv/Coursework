package com.example.coursework.ui.movie.adapters.utils;

import android.view.View;

public interface AdapterCallback<T> {
    void onItemClicked(T model, View view);
}
