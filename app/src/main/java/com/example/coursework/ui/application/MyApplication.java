package com.example.coursework.ui.application;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import com.example.coursework.data.providers.implementations.SharedPreferenceRepositoryImpl;

//Класс приложения для инициализации экземпляра SP и первичной установки языка
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferenceRepositoryImpl.init(this);
        int pos = SharedPreferenceRepositoryImpl.getInstance().getSpinnerPos();
        String lang = pos == 0 ? "en" : "ru";
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang));
    }
}
