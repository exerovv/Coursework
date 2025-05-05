package com.example.coursework.ui.movie.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;
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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private PopularMovieAdapter popularMovieAdapter;
    private HomeViewModel homeViewModel;
    private LanguageViewModel languageViewModel;
    private ConcatAdapter concatAdapter;
    private final CompositeDisposable popularCompositeDisposable = new CompositeDisposable();

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
        languageViewModel.getLangLiveData().observe(getViewLifecycleOwner(), integer ->
                popularCompositeDisposable.add(homeViewModel.getPopularMoviesPagingData(integer)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                movies -> popularMovieAdapter.submitData(getLifecycle(), movies)
                        ))
        );

        Objects.requireNonNull(binding.popularRecyclerView).setAdapter(concatAdapter);

        popularMovieAdapter.attachCallback((movie, view1) ->
                findNavController(requireActivity(), R.id.nav_host_fragment_container)
                        .navigate(HomeFragmentDirections.toMovieDetailFragment(movie.getId())));

        popularMovieAdapter.addLoadStateListener(loadStates -> {
            boolean isLoading = loadStates.getRefresh() instanceof LoadState.Loading;
            boolean isError = loadStates.hasError();
            boolean isNotLoading = loadStates.getRefresh() instanceof LoadState.NotLoading;
            boolean isListEmpty = popularMovieAdapter.getItemCount() == 0;
            if (isError && isListEmpty) errorUI(true);
            if (isNotLoading && !isError) errorUI(false);
            binding.popularRecyclerView.setVisibility(!isLoading ? VISIBLE : GONE);
            binding.popularProgressBar.setVisibility(isLoading ? VISIBLE : GONE);
            return null;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        popularMovieAdapter.detachCallback();
        popularCompositeDisposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        popularMovieAdapter = null;
        concatAdapter = null;
    }

    public void errorUI(boolean isError) {
        binding.warning.setVisibility(isError ? VISIBLE : GONE);
        binding.mainPart.setVisibility(!isError ? VISIBLE : GONE);
    }
}