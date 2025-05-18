package com.example.coursework.ui.favorites.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.providers.implementations.FavoriteServiceImpl;
import com.example.coursework.domain.model.Movie;
import com.example.coursework.ui.favorites.viewmodels.states.FavoriteFieldsState;
import com.example.coursework.ui.favorites.viewmodels.states.FavoriteSnackBarState;
import com.example.coursework.ui.favorites.viewmodels.states.FavoriteState;
import com.example.coursework.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.Disposable;

public class FavoriteViewModel extends ViewModel {
    private final FavoriteServiceImpl favoriteService = new FavoriteServiceImpl();
    private final FavoriteFieldsState favoriteFieldsState = new FavoriteFieldsState();
    private final SingleLiveEvent<FavoriteSnackBarState> favoriteSnackBarState = new SingleLiveEvent<>();

    public LiveData<FavoriteSnackBarState> getFavoriteSnackBarState() {
        return favoriteSnackBarState;
    }

    private final MutableLiveData<Boolean> isError = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsError() {
        return isError;
    }

    public void setIsError(boolean flag) {
        if (flag != Boolean.TRUE.equals(isError.getValue())) {
            isError.setValue(flag);
        }
    }

    private final MutableLiveData<HashSet<Integer>> idsLiveData = new MutableLiveData<>(new HashSet<>());

    public LiveData<HashSet<Integer>> getIdsLiveData() {
        return idsLiveData;
    }

    public void setFavoriteId(String user) {
        if (Objects.requireNonNull(idsLiveData.getValue()).isEmpty()) {
            fetchFavoritesIds(user);
        }
    }

    private final MutableLiveData<List<Movie>> favoriteList = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Movie>> getFavoriteList(){
        return favoriteList;
    }

    private final MutableLiveData<FavoriteState> favoriteState = new MutableLiveData<>(new FavoriteState.Default());

    public LiveData<FavoriteState> getFavoritesLiveData(String user, int pos) {
        if ((favoriteState.getValue() instanceof FavoriteState.Default)
                || pos != favoriteFieldsState.getLastPos()) {
            if (!(favoriteState.getValue() instanceof FavoriteState.Default))
                subscribeMovies.dispose();
            favoriteFieldsState.setLastPos(pos);
            fetchFavorites(user, pos);
        }
        return favoriteState;
    }

    private Disposable subscribeSnackBar;
    private Disposable subscribeMovies;
    private Disposable subscribeIds;


    private void insertFavorites(String userId, int movieId, int language, String title, String poster) {
        subscribeSnackBar = favoriteService.insertFavorite(userId, movieId, language, title, poster)
                .subscribe(
                        () -> favoriteSnackBarState.postValue(new FavoriteSnackBarState.Success("ins")),
                        onError -> favoriteSnackBarState.postValue(new FavoriteSnackBarState.Error("ins"))
                );
    }

    private void deleteFavorites(String userId, int movieId) {
        subscribeSnackBar = favoriteService.deleteFavorite(userId, movieId)
                .subscribe(
                        () -> favoriteSnackBarState.postValue(new FavoriteSnackBarState.Success("del")),
                        onError -> favoriteSnackBarState.postValue(new FavoriteSnackBarState.Error("del"))
                );
    }

    public void changeFavoriteId(String user, int movieId, int langCode, String title, String poster) {
        HashSet<Integer> currentSet = idsLiveData.getValue();
        List<Movie> currentList = favoriteList.getValue();
        if (currentSet == null) return;
        if (currentList == null) return;
        HashSet<Integer> newSet = new HashSet<>(currentSet);
        ArrayList<Movie> newList = new ArrayList<>(currentList);
        if (!currentSet.contains(movieId)) {
            Movie newMovie = new Movie(movieId, title, null, poster, null);
            insertFavorites(user, movieId, langCode, title, poster);
            newSet.add(movieId);
            newList.add(newMovie);
        } else {
            deleteFavorites(user, movieId);
            newSet.remove(movieId);
            newList.removeIf(movie -> movie.getId() == movieId);
        }
        idsLiveData.postValue(newSet);
        favoriteList.postValue(newList);
    }

    public void fetchFavorites(String user, int position) {
        favoriteState.postValue(new FavoriteState.Loading());
        subscribeMovies = favoriteService.fetchFavorites(user, position)
                .subscribe(movies -> {
                    favoriteState.postValue(new FavoriteState.Success());
                    favoriteList.postValue(movies);
                        },
                        error -> favoriteState.postValue(new FavoriteState.Error()));
    }

    private void fetchFavoritesIds(String user) {
        subscribeIds = favoriteService.fetchFavoritesIds(user).
                subscribe(ids -> {
                            idsLiveData.postValue(new HashSet<>(ids));
                            isError.postValue(false);
                        },
                        error -> isError.postValue(true));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (subscribeSnackBar != null) subscribeSnackBar.dispose();
        if (subscribeMovies != null) subscribeMovies.dispose();
        if (subscribeIds != null) subscribeIds.dispose();
    }
}