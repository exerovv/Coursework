package com.example.coursework.ui.authentication.viewmodels.states;

public interface AuthCallback {
    void onSuccess();
    void onError(String error);
}
