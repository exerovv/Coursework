package com.example.coursework;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework.api.Movie;
import com.example.coursework.api.MovieRepository;
import com.example.coursework.api.MovieResponse;
import com.example.coursework.api.TmdbApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private static final String API_KEY = "4b104e9ab7bbf8b901f150ea9dd1eeee";

    public SearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TmdbApi service = MovieRepository.getClient().create(TmdbApi.class);
        Call<MovieResponse> call = service.loadMovies(API_KEY);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Movie movie : response.body().getMovies()) {
                        Log.d("MOVIE", "Title: " + movie.getTitle());
                    }
                } else {
                    Log.e("API_ERROR", "Response not successful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage(), t);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}