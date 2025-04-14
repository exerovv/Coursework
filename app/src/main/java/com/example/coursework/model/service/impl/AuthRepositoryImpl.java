package com.example.coursework.model.service.impl;

import android.util.Log;

import com.example.coursework.model.service.AuthRepository;
import com.example.coursework.utils.AuthCallback;
import com.google.firebase.auth.FirebaseAuth;

public class AuthRepositoryImpl implements AuthRepository {
    private final FirebaseAuth mAuth;

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    private static final String TAG = "Login";

    public AuthRepositoryImpl() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void signUp(String email, String password, AuthCallback authCallback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "sign up success!");
                        authCallback.onSuccess();
                    } else {
                        Exception e = task.getException();
                        String errorMsg = e != null ? e.getLocalizedMessage() : "Unknown error";
                        Log.d(TAG, "sign up failure! " + errorMsg);
                        authCallback.onError(errorMsg);
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
                        Exception e = task.getException();
                        String errorMsg = e != null ? e.getLocalizedMessage() : "Unknown error";
                        Log.d(TAG, "sign in failure! " + errorMsg);
                        authCallback.onError(errorMsg);
                    }
                });
    }
}
