package com.example.coursework;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.model.service.impl.AuthRepositoryImpl;
import com.example.coursework.utils.AuthCallback;
import com.example.coursework.utils.AuthState;

import java.util.regex.Pattern;


public class AuthViewModel extends ViewModel {
    private final AuthRepositoryImpl authRepository = new AuthRepositoryImpl();
    private final MutableLiveData<AuthState> authState = new MutableLiveData<>();

    public MutableLiveData<AuthState> getAuthState() {
        return authState;
    }

    public void login(String email, String password) {
        if (validation(email, password)) {
            authRepository.signIn(email, password, new AuthCallback() {
                @Override
                public void onSuccess() {
                    authState.postValue(new AuthState.Success());
                }

                @Override
                public void onError(String error) {
                    authState.postValue(new AuthState.Error(error));
                }
            });
        }else{
            authState.postValue(new AuthState.Error("Invalid data"));
        }
    }

    public void register(String email, String password){
        if (validation(email, password)){
            authRepository.signUp(email, password, new AuthCallback() {
                @Override
                public void onSuccess() {
                    authState.postValue(new AuthState.Success());
                }

                @Override
                public void onError(String error) {
                    authState.postValue(new AuthState.Error(error));
                }
            });
        }else{
            authState.postValue(new AuthState.Error("Invalid data"));
        }
    }

    public boolean checkUser(){
        return authRepository.getmAuth().getCurrentUser() != null;
    }

    public boolean validation(String email, String password){
        return validateEmail(email) && validatePassword(password);
    }

    public boolean validateEmail(String email){
        return Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email);
    }

    public boolean validatePassword(String password){
        return password.length() >= 8;
    }
}
