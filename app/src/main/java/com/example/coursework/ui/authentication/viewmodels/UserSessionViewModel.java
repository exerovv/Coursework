package com.example.coursework.ui.authentication.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.coursework.data.providers.implementations.AuthRepositoryImpl;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UserSessionViewModel extends ViewModel {
    private final AuthRepositoryImpl authRepository = new AuthRepositoryImpl();

    public boolean checkUser(){
        return authRepository.getmAuth().getCurrentUser() != null;
    }

    public String getCurrentUser(){
        FirebaseUser user  = authRepository.getmAuth().getCurrentUser();
        return user != null ? user.getUid() : "";
    }

    public String getCurrentUserEmail(){
        return Objects.requireNonNull(authRepository.getmAuth().getCurrentUser()).getEmail();
    }
}
