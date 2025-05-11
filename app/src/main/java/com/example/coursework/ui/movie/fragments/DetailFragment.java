package com.example.coursework.ui.movie.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.coursework.R;
import com.example.coursework.databinding.FragmentDetailBinding;
import com.example.coursework.domain.model.Movie;
import com.example.coursework.ui.authentication.viewmodels.UserSessionViewModel;
import com.example.coursework.ui.favorites.viewmodels.FavoriteViewModel;
import com.example.coursework.ui.favorites.viewmodels.states.FavoriteState;
import com.example.coursework.ui.movie.viewmodels.DetailViewModel;
import com.example.coursework.ui.movie.viewmodels.states.SingleMovieUiState;
import com.example.coursework.ui.profile.viewmodels.LanguageViewModel;
import com.example.coursework.ui.utils.MovieUIMapper;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class DetailFragment extends Fragment {
    private FragmentDetailBinding binding = null;
    private DetailViewModel detailViewModel;
    private LanguageViewModel languageViewModel;
    private FavoriteViewModel favoriteViewModel;
    private UserSessionViewModel userSessionViewModel;

    private final String TAG = DetailFragment.class.getSimpleName();

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        languageViewModel = new ViewModelProvider(requireActivity()).get(LanguageViewModel.class);
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        userSessionViewModel = new ViewModelProvider(requireActivity()).get(UserSessionViewModel.class);
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
        languageViewModel.getLangLiveData().observe(getViewLifecycleOwner(), pos -> {
                    Log.d(TAG, "" + pos);
                    setupObservers(
                            movieId,
                            Objects.requireNonNull(languageViewModel.getLangLiveData().getValue())
                    );
                }
        );
        binding.warningBtn.setOnClickListener(view1 ->
                detailViewModel.fetchSingleMovie(
                        movieId,
                        Objects.requireNonNull(languageViewModel.getLangLiveData().getValue()))
        );

        favoriteViewModel.getFavoriteState().observe(getViewLifecycleOwner(), state -> {
            if (state instanceof FavoriteState.Success){
                Snackbar.make(binding.getRoot(), getString(R.string.favorites_success), Snackbar.LENGTH_SHORT)
                        .show();
            }else{
                Snackbar.make(binding.getRoot(), getString(R.string.favorites_error), Snackbar.LENGTH_SHORT)
                        .show();
            }
        });

        binding.watchLaterBtn.setOnClickListener(view2 -> {
            if (!userSessionViewModel.checkUser()){
                Snackbar.make(binding.getRoot(), getString(R.string.no_user_warning), Snackbar.LENGTH_SHORT)
                        .show();
            }
            else{
                favoriteViewModel.insertFavorites(userSessionViewModel.getCurrentUser(), movieId);
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateUI(Movie movie) {
        Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .placeholder(R.drawable.no_image_placeholder)
                .into(binding.detailImage);
        binding.title.setText(movie.getTitle());
        String overview = movie.getOverview();
        binding.overview.setTextAlignment(MovieUIMapper.getTextAlignment(overview));
        binding.overview.setText(MovieUIMapper.getOverviewOrBlank(overview, requireContext()));
        binding.overview.setMovementMethod(new ScrollingMovementMethod());
        String filmRating = movie.getRating();
        binding.rating.setText(filmRating);
        binding.rating.setTextColor(MovieUIMapper.getRatingColor(filmRating));
    }

    private void setupObservers(int movieId, int pos) {
        detailViewModel.getSingleMovieLiveData(movieId, pos).observe(
                getViewLifecycleOwner(),
                state -> {
                    Log.d(TAG, state.getClass().getSimpleName());
                    if (state instanceof SingleMovieUiState.Success) {
                        updateUI(((SingleMovieUiState.Success) state).data);
                        errorUI(false);
                        loadingUI(false);
                    }
                    if (state instanceof SingleMovieUiState.Error) {
                        errorUI(true);
                        loadingUI(false);
                    }
                    if (state instanceof SingleMovieUiState.Loading) {
                        loadingUI(true);
                    }
                    if (state instanceof SingleMovieUiState.Default) {
                        errorUI(false);
                        loadingUI(false);
                    }
                }
        );
    }

    private void errorUI(boolean isError) {
        binding.warning.setVisibility(isError ? VISIBLE : GONE);
    }

    private void loadingUI(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? VISIBLE : GONE);
        binding.mainPart.setVisibility(!isLoading ? VISIBLE : GONE);
    }
}