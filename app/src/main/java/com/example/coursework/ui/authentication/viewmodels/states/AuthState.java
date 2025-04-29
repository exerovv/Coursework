package com.example.coursework.ui.authentication.viewmodels.states;

public abstract class AuthState {
    public static class Success extends AuthState {}
    public static class Error extends AuthState {
        public String error;

        public Error(String error) {
            this.error = error;
        }
    }
    public static class Loading extends AuthState {}
    public static class Default extends AuthState {}
}
