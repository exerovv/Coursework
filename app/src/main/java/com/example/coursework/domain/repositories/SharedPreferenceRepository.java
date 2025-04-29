package com.example.coursework.domain.repositories;

public interface SharedPreferenceRepository {
    void saveSpinnerPos(int pos);
    int getSpinnerPos();
}
