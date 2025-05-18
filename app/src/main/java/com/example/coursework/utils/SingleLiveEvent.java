package com.example.coursework.utils;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private final String TAG = SingleLiveEvent.class.getSimpleName();
    private final AtomicBoolean mPending = new AtomicBoolean(false);

    public SingleLiveEvent(T value) {
        super(value);
    }

    public SingleLiveEvent() {
        super();
    }

    @MainThread
    public void observe(@NonNull LifecycleOwner lifecycleOwner, @NonNull Observer<? super T> observer){
        if (hasActiveObservers()){
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }
        super.observe(lifecycleOwner, data -> {
            Log.d(TAG, "" + mPending);
            if (mPending.compareAndSet(true, false)){
                observer.onChanged(data);
            }
        });
    }

    public void postValue(@Nullable T t){
        mPending.set(true);
        super.postValue(t);
    }
}
