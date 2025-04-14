package com.example.coursework.utils;

public abstract class AuthState {
    public static class Success extends AuthState {}
    public static class Error extends AuthState {
        public String error;

        public Error(String error) {
            this.error = error;
        }
    }
}
