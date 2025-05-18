package com.example.coursework.ui.movie.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.ConcatAdapter;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentHomeBinding;
import com.example.coursework.ui.movie.adapters.MovieLoaderStateAdapter;
import com.example.coursework.ui.movie.adapters.PopularMovieAdapter;
import com.example.coursework.ui.movie.viewmodels.HomeViewModel;
import com.example.coursework.ui.profile.viewmodels.LanguageViewModel;

import java.util.Objects;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private PopularMovieAdapter popularMovieAdapter;
    private HomeViewModel homeViewModel;
    private LanguageViewModel languageViewModel;
    private ConcatAdapter concatAdapter;

    private final String TAG = HomeViewModel.class.getSimpleName();

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        languageViewModel = new ViewModelProvider(requireActivity()).get(LanguageViewModel.class);
        popularMovieAdapter = new PopularMovieAdapter();
        concatAdapter = popularMovieAdapter.withLoadStateFooter(new MovieLoaderStateAdapter());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel.getIsError().observe(getViewLifecycleOwner(), this::errorUI);

        languageViewModel.getLangLiveData().observe(getViewLifecycleOwner(), lang ->
                homeViewModel.getPopularMoviesPagingData(lang).observe(
                        getViewLifecycleOwner(),
                        moviePagingData -> popularMovieAdapter.submitData(getLifecycle(), moviePagingData)
                )
        );

        binding.warningBtn.setOnClickListener(view2 -> homeViewModel.fetchPopularMovies(
                Objects.requireNonNull(languageViewModel.getLangLiveData().getValue()))
        );

        Objects.requireNonNull(binding.popularRecyclerView).setAdapter(concatAdapter);

        popularMovieAdapter.attachCallback((movie, view1) ->
                findNavController(requireActivity(), R.id.nav_host_fragment_container)
                        .navigate(HomeFragmentDirections.toMovieDetailFragment(movie.getId())));

        popularMovieAdapter.addLoadStateListener( loadStates -> {
            Log.d(TAG, loadStates.getRefresh().getClass().getSimpleName());
            boolean isLoading = loadStates.getRefresh() instanceof LoadState.Loading;
            boolean isError = loadStates.hasError();
            if (isError) homeViewModel.setIsError(true);
            if (!isError && !isLoading) {
                homeViewModel.setIsError(false);
                loadingUI(false);
            }
            if (isLoading)
                loadingUI(true);
            return null;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        popularMovieAdapter.detachCallback();
//        popularMovieAdapter.removeLoadStateListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        popularMovieAdapter = null;
        concatAdapter = null;
    }

    private void errorUI(boolean isError) {
        binding.warning.setVisibility(isError ? VISIBLE : GONE);
    }

    private void loadingUI(boolean isLoading){
        binding.popularProgressBar.setVisibility(isLoading ? VISIBLE : GONE);
    }
}