package com.example.coursework.ui.authentication.viewmodels.states;

public interface AuthState {
    class Success implements AuthState {}
    class Error implements AuthState {
        public String error;

        public Error(String error) {
            this.error = error;
        }
    }
    class Loading implements AuthState {}
    class Default implements AuthState {}
}
