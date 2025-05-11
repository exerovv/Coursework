package com.example.coursework.ui.favorites.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.providers.implementations.FavoriteServiceImpl;
import com.example.coursework.ui.favorites.viewmodels.states.FavoriteState;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteViewModel extends ViewModel {
    private final MutableLiveData<FavoriteState> favoriteState = new MutableLiveData<>();

    private final FavoriteServiceImpl favoriteService = new FavoriteServiceImpl();

    private Disposable subscribe;

    public LiveData<FavoriteState> getFavoriteState() {
        return favoriteState;
    }

    public void insertFavorites(String userId, int movieId){
        subscribe = favoriteService.insertFavorite(userId, movieId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> favoriteState.postValue(new FavoriteState.Success()),
                        onError -> favoriteState.postValue(new FavoriteState.Error())
                );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (subscribe != null) subscribe.dispose();
    }
}
