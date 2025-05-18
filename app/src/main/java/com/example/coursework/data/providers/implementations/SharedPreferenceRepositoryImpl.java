package com.example.coursework.data.providers.implementations;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.coursework.domain.repositories.SharedPreferenceRepository;

//Репозиторий для работы с SP
public class SharedPreferenceRepositoryImpl implements SharedPreferenceRepository {
    private static SharedPreferenceRepositoryImpl instance = null;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    //Реализация паттерна Singletone - приватный конструктор
    private SharedPreferenceRepositoryImpl(Context context) {
        this.preferences = context.getSharedPreferences("app_settings", MODE_PRIVATE);
        editor = preferences.edit();
    }

    //Реализация паттерна Singletone - метод инициализации
    public static void init(Context context){
        if (instance == null){
            instance = new SharedPreferenceRepositoryImpl(context);
        }
    }

    //Метод для получение экземпляра SP
    public static SharedPreferenceRepositoryImpl getInstance(){
        if (instance == null){
            throw new IllegalStateException("SharedPreferencesRepository is not initialized.");
        }
        return instance;
    }

    //Метод для сохранения языка
    @Override
    public void saveSpinnerPos(int pos) {
        editor.putInt("spinner_pos", pos);
        editor.apply();
    }

    //Метод для получения языка
    @Override
    public int getSpinnerPos() {
        return preferences.getInt("spinner_pos", 0);
    }

}
