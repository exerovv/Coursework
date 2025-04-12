package com.example.coursework.model.service.impl;

import android.util.Log;

import com.example.coursework.utils.AuthCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class UserAuthImpl {
    private final FirebaseAuth mAuth;

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    private static final String TAG = "Login";

    public UserAuthImpl() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void signUp(String email, String password, AuthCallback authCallback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "sign up success!");
                        authCallback.onSuccess();
                    } else {
                        Log.d(TAG, "sign up failure!" + task.getException());
                        authCallback.onError();
                    }
                });
    }

    public void signIn(String email, String password, AuthCallback authCallback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "sign in success!");
                        authCallback.onSuccess();
                    } else {
                        Log.d(TAG, "sign in failure!" + task.getException());
                        authCallback.onError();
                    }
                });
    }

    public boolean validateEmail(String email){
        return Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email);
    }

    public boolean validatePassword(String password){
        return password.length() >= 8;
    }
}
