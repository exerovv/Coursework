package com.example.coursework.ui.favorites.viewmodels.states;

public class FavoriteFieldsState {
    private int lastPos = 0;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getLastPos() {
        return lastPos;
    }

    public void setLastPos(int lastPos) {
        this.lastPos = lastPos;
    }
}