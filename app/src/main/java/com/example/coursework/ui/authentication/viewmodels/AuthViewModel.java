package com.example.coursework.ui.authentication.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.providers.implementations.AuthRepositoryImpl;
import com.example.coursework.ui.authentication.viewmodels.states.AuthCallback;
import com.example.coursework.ui.authentication.viewmodels.states.AuthState;
import com.example.coursework.utils.SingleLiveEvent;

import java.util.regex.Pattern;


public class AuthViewModel extends ViewModel {
    private final AuthRepositoryImpl authRepository = new AuthRepositoryImpl();
    private final SingleLiveEvent<AuthState> authState = new SingleLiveEvent<>(new AuthState.Default());
    public MutableLiveData<AuthState> getAuthState() {
        return authState;
    }

    public void login(String email, String password) {
        if (!validateEmail(email)){
            authState.postValue(new AuthState.Error("Invalid email"));
            return;
        }
        if(!validatePassword(password)){
            authState.postValue(new AuthState.Error("Invalid password"));
            return;
        }
        authState.postValue(new AuthState.Loading());
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
    }

    public void register(String email, String password){
        if (!validateEmail(email)){
            authState.postValue(new AuthState.Error("Invalid email"));
            return;
        }
        if(!validatePassword(password)){
            authState.postValue(new AuthState.Error("Invalid password"));
            return;
        }
        authState.postValue(new AuthState.Loading());
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
    }

    public void changePassword(String email, String oldPassword, String newPassword){
        if (!validateEmail(email)){
            authState.postValue(new AuthState.Error("Invalid email"));
            return;
        }
        if(!validatePassword(oldPassword)){
            authState.postValue(new AuthState.Error("Old password is incorrect"));
            return;
        }
        if(!validateNewPassword(oldPassword, newPassword)){
            authState.postValue(new AuthState.Error("New password is incorrect"));
            return;
        }
        authState.postValue(new AuthState.Loading());
        authRepository.changePassword(email, oldPassword, newPassword, new AuthCallback() {
            @Override
            public void onSuccess() {
                authState.postValue(new AuthState.Success());
            }

            @Override
            public void onError(String error) {
                authState.postValue(new AuthState.Error(error));
            }
        });
    }

    public void logout(){
        authRepository.logout();
    }

    public boolean validateEmail(String email){
        return Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email);
    }

    public boolean validateNewPassword(String oldPassword, String newPassword){
        return validatePassword(newPassword) && !newPassword.equals(oldPassword);
    }

    public boolean validatePassword(String password){
        return password.length() >= 8;
    }
}
