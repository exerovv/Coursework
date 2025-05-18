package com.example.coursework.domain.repositories;

//Интерфейс с операциями с SP добавлен для лучшей масштабируемости и более легкого тестирования
public interface SharedPreferenceRepository {
    void saveSpinnerPos(int pos);
    int getSpinnerPos();
}
