package com.example.coursework.ui.authentication.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.coursework.data.providers.implementations.AuthRepositoryImpl;

import java.util.Objects;

public class UserSessionViewModel extends ViewModel {
    private final AuthRepositoryImpl authRepository = new AuthRepositoryImpl();

    public boolean checkUser(){
        return authRepository.getmAuth().getCurrentUser() != null;
    }

    public String getCurrentUser(){
        return Objects.requireNonNull(authRepository.getmAuth().getCurrentUser()).toString();
    }
}
