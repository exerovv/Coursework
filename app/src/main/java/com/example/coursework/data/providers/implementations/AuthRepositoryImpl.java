package com.example.coursework.data.providers.implementations;

import android.util.Log;


import com.example.coursework.domain.repositories.AuthRepository;
import com.example.coursework.ui.authentication.viewmodels.states.AuthCallback;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Репозиторий для авторизации
public class AuthRepositoryImpl implements AuthRepository {
    private final FirebaseAuth mAuth;

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    private static final String TAG = "Login";

    public AuthRepositoryImpl() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    //Метод для регистрации
    @Override
    public void signUp(String email, String password, AuthCallback authCallback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "sign up success!");
                        authCallback.onSuccess();
                    } else {
                        String errorMsg = "Authentication error";
                        Log.d(TAG, "sign up failure! " + errorMsg);
                        authCallback.onError(errorMsg);
                    }
                });
    }

    //Метод для смены пароля
    @Override
    public void changePassword(String email, String oldPassword, String newPassword, AuthCallback authCallback) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            //Текущие данные для авторизации
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
            //Для смены пароля требуется ре-аутентификация
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    user.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) authCallback.onSuccess();
                        else {
                            String errorMsg = "Couldn't change password";
                            Log.d(TAG, "password changing failure! " + errorMsg);
                            authCallback.onError(errorMsg);
                        }
                    });
                }
                else{
                    String errorMsg = "Authentication error or user doesn't exist!";
                    Log.d(TAG, "sign in password changing failure! " + errorMsg);
                    authCallback.onError(errorMsg);
                }
            });
        }else{
            String errorMsg = "User doesn't exist";
            Log.d(TAG, "sign in password changing failure! " + errorMsg);
            authCallback.onError(errorMsg);
        }
    }

    //Метод для выхода из аккаунта
    @Override
    public void logout() {
        mAuth.signOut();
    }

    //Метод для авторизации
    public void signIn(String email, String password, AuthCallback authCallback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "sign in success!");
                        authCallback.onSuccess();
                    } else {
                        String errorMsg = "Authentication error or user doesn't exist!";
                        Log.d(TAG, "sign in failure! " + errorMsg);
                        authCallback.onError(errorMsg);
                    }
                });
    }
}
