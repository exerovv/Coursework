package com.example.coursework.ui.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.model.service.impl.SharedPreferenceRepositoryImpl;

public class LanguageViewModel extends ViewModel {
    private final SharedPreferenceRepositoryImpl sharedPreferenceRepository =
            SharedPreferenceRepositoryImpl.getInstance();

    private final MutableLiveData<Integer> langLiveData =
            new MutableLiveData<>(sharedPreferenceRepository.getSpinnerPos());
    public LiveData<Integer> getLangLiveData() {
        return langLiveData;
    }
    public void saveSpinnerPos(int position) {
        if (!posEquals(position)){
            sharedPreferenceRepository.saveSpinnerPos(position);
            langLiveData.postValue(position);
        }
    }

    private boolean posEquals(int newPosition){
        int currentPos = sharedPreferenceRepository.getSpinnerPos();
        return currentPos == newPosition;
     }
}

