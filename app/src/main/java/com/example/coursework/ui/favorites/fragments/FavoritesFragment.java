package com.example.coursework.ui.favorites.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentFavoritesBinding;
import com.example.coursework.ui.authentication.viewmodels.UserSessionViewModel;
import com.example.coursework.ui.favorites.adapters.FavoriteAdapter;
import com.example.coursework.ui.favorites.viewmodels.FavoriteViewModel;
import com.example.coursework.ui.favorites.viewmodels.states.FavoriteState;
import com.example.coursework.ui.movie.viewmodels.states.SingleMovieUiState;
import com.example.coursework.ui.profile.viewmodels.LanguageViewModel;
import com.example.coursework.utils.MovieDiffUtils;

import java.util.Objects;

public class FavoritesFragment extends Fragment {
    private final String TAG = FavoritesFragment.class.getSimpleName();

    private FavoriteViewModel favoriteViewModel;
    private LanguageViewModel languageViewModel;
    private UserSessionViewModel userSessionViewModel;
    private FavoriteAdapter mAdapter = new FavoriteAdapter();
    private FragmentFavoritesBinding binding;
    private DiffUtil.Callback diffUtil;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
        languageViewModel = new ViewModelProvider(requireActivity()).get(LanguageViewModel.class);
        userSessionViewModel = new ViewModelProvider(requireActivity()).get(UserSessionViewModel.class);
        mAdapter = new FavoriteAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.favoriteRecyclerView.setAdapter(mAdapter);
        favoriteViewModel.getIsError().observe(getViewLifecycleOwner(), this::errorUI);

        mAdapter.attachCallback((movie, view1) ->
                findNavController(requireActivity(), R.id.nav_host_fragment_container)
                        .navigate(FavoritesFragmentDirections.toMovieDetailFragment(movie.getId())));

        languageViewModel.getLangLiveData().observe(getViewLifecycleOwner(), integer ->
                setupObservers(userSessionViewModel.getCurrentUser(), integer));

        favoriteViewModel.getFavoriteList().observe(getViewLifecycleOwner(), data -> {
                    diffUtil = new MovieDiffUtils(data, mAdapter.mDataList);
                    DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtil);
                    mAdapter.setFavoriteList(data);
                    result.dispatchUpdatesTo(mAdapter);
                }
        );

        binding.warningBtn.setOnClickListener(view2 -> favoriteViewModel.fetchFavorites(
                userSessionViewModel.getCurrentUser(), Objects.requireNonNull(languageViewModel.getLangLiveData().getValue()))
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mAdapter.detachCallback();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter = null;
    }

    private void errorUI(boolean isError) {
        binding.warning.setVisibility(isError ? VISIBLE : GONE);
    }

    private void loadingUI(boolean isLoading) {
        binding.favoriteProgressBar.setVisibility(isLoading ? VISIBLE : GONE);
    }

    private void setupObservers(String user, int pos) {
        favoriteViewModel.getFavoritesLiveData(user, pos).observe(
                getViewLifecycleOwner(),
                state -> {
                    if (state instanceof FavoriteState.Success) {
                        Log.d(TAG, "Success");
                        favoriteViewModel.setIsError(false);
                        loadingUI(false);
                    }
                    if (state instanceof SingleMovieUiState.Error) {
                        Log.d(TAG, "Error");
                        favoriteViewModel.setIsError(true);
                        loadingUI(false);
                    }
                    if (state instanceof SingleMovieUiState.Loading) {
                        Log.d(TAG, "Loading");
                        loadingUI(true);
                    }
                    if (state instanceof SingleMovieUiState.Default) {
                        Log.d(TAG, "Default");
                        loadingUI(false);
                    }
                }
        );
    }
}