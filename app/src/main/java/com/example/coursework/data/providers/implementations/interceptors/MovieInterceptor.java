package com.example.coursework.data.providers.implementations.interceptors;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MovieInterceptor implements Interceptor {
    private static final String API_KEY = "4b104e9ab7bbf8b901f150ea9dd1eeee";
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl newUrl = originalRequest.url().newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build();
        Request newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build();
        return chain.proceed(newRequest);
    }
}
