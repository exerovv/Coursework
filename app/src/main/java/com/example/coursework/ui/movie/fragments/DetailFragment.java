package com.example.coursework.ui.movie.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.coursework.databinding.FragmentDetailBinding;
import com.example.coursework.domain.model.Movie;
import com.example.coursework.ui.movie.viewmodels.DetailViewModel;
import com.example.coursework.ui.profile.viewmodels.LanguageViewModel;
import com.example.coursework.ui.utils.MovieUIMapper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class DetailFragment extends Fragment {
    private FragmentDetailBinding binding = null;
    private DetailViewModel detailViewModel;
    private LanguageViewModel languageViewModel;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        languageViewModel = new ViewModelProvider(requireActivity()).get(LanguageViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.progressBar.setVisibility(VISIBLE);
        int movieId = DetailFragmentArgs.fromBundle(getArguments()).getMovieId();
        languageViewModel.getLangLiveData().observe(getViewLifecycleOwner(), integer ->
                compositeDisposable.add(detailViewModel.getSingleMovieData(movieId, integer)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> binding.progressBar.setVisibility(GONE))
                .subscribe(
                        this::updateUI,
                        error -> {
                            binding.mainPart.setVisibility(GONE);
                            binding.warning.setVisibility(VISIBLE);
                        }
                )
        ));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
        binding = null;
    }

    private void updateUI(Movie movie) {
        Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .into(binding.detailImage);
        binding.title.setText(movie.getTitle());
        binding.overview.setText(movie.getOverview());
        binding.overview.setMovementMethod(new ScrollingMovementMethod());
        String filmRating = movie.getRating();
        binding.rating.setText(filmRating);
        binding.rating.setTextColor(MovieUIMapper.getRatingColor(filmRating));
    }
}