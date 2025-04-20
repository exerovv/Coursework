package com.example.coursework.model.service;

public interface SharedPreferenceRepository {
    void saveSpinnerPos(int pos);
    int getSpinnerPos();
}
