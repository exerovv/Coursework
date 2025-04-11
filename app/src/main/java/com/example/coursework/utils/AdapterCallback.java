package com.example.coursework.utils;

import android.view.View;

public interface AdapterCallback<T> {
    void onItemClicked(T model, View view);
}
