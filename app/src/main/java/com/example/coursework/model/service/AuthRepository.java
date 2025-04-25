package com.example.coursework.model.service;

import com.example.coursework.utils.AuthCallback;
import com.google.firebase.auth.FirebaseUser;

public interface AuthRepository {
    void signIn(String email, String password, AuthCallback authCallback);
    void signUp(String email, String password, AuthCallback authCallback);
    void changePassword(String email, String oldPassword, String newPassword, AuthCallback authCallback);
    void logout();

}
