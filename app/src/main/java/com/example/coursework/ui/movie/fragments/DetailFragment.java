package com.example.coursework.ui.movie.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.coursework.databinding.FragmentDetailBinding;
import com.example.coursework.domain.model.Movie;
import com.example.coursework.ui.utils.MovieUIMapper;

public class DetailFragment extends Fragment {
    private FragmentDetailBinding binding = null;
    public DetailFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Movie movie = DetailFragmentArgs.fromBundle(getArguments()).getMovie();
        Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .into(binding.detailImage);
        binding.title.setText(movie.getTitle());
        binding.overview.setText(movie.getOverview());
        binding.overview.setMovementMethod(new ScrollingMovementMethod());
        String filmRating = movie.getRating();
        binding.rating.setText(filmRating);
        binding.rating.setTextColor(MovieUIMapper.getRatingColor(filmRating));
        Log.d("DetailFragment", String.valueOf(binding.rating.getTextColors()));
    }
}