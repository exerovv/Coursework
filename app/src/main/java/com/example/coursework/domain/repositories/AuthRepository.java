package com.example.coursework.domain.repositories;

import com.example.coursework.ui.authentication.viewmodels.states.AuthCallback;

//Интерфейс с операциями авторизации добавлен для лучшей масштабируемости и более легкого тестирования
public interface AuthRepository {
    void signIn(String email, String password, AuthCallback authCallback);
    void signUp(String email, String password, AuthCallback authCallback);
    void changePassword(String email, String oldPassword, String newPassword, AuthCallback authCallback);
    void logout();

}
