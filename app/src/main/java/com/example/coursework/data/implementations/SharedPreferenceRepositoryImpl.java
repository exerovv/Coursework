package com.example.coursework.data.implementations;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.coursework.domain.repositories.SharedPreferenceRepository;

public class SharedPreferenceRepositoryImpl implements SharedPreferenceRepository {
    private static SharedPreferenceRepositoryImpl instance = null;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    private SharedPreferenceRepositoryImpl(Context context) {
        this.preferences = context.getSharedPreferences("app_settings", MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void init(Context context){
        if (instance == null){
            instance = new SharedPreferenceRepositoryImpl(context);
        }
    }

    public static SharedPreferenceRepositoryImpl getInstance(){
        if (instance == null){
            throw new IllegalStateException("SharedPreferencesRepository is not initialized.");
        }
        return instance;
    }

    @Override
    public void saveSpinnerPos(int pos) {
        editor.putInt("spinner_pos", pos);
        editor.apply();
    }

    @Override
    public int getSpinnerPos() {
        return preferences.getInt("spinner_pos", 0);
    }

}
