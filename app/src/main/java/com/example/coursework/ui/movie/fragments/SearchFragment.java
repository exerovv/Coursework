package com.example.coursework.ui.movie.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.ConcatAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coursework.ui.movie.adapters.SearchMovieAdapter;
import com.example.coursework.ui.profile.viewmodels.LanguageViewModel;
import com.example.coursework.ui.movie.viewmodels.MovieViewModel;
import com.example.coursework.R;
import com.example.coursework.databinding.FragmentSearchBinding;
import com.example.coursework.ui.movie.adapters.PopularMovieAdapter;
import com.example.coursework.ui.movie.adapters.MovieLoaderStateAdapter;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private PopularMovieAdapter popularMovieAdapter;
    private SearchMovieAdapter searchMovieAdapter;
    private MovieViewModel movieViewModel;
    private LanguageViewModel languageViewModel;
    private ConcatAdapter concatAdapter;
    private final CompositeDisposable popularCompositeDisposable = new CompositeDisposable();
    private final CompositeDisposable searchCompositeDisposable = new CompositeDisposable();

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        languageViewModel = new ViewModelProvider(requireActivity()).get(LanguageViewModel.class);
        popularMovieAdapter = new PopularMovieAdapter();
        searchMovieAdapter = new SearchMovieAdapter();
        concatAdapter = popularMovieAdapter.withLoadStateFooter(new MovieLoaderStateAdapter());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.options_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.search) {
                    SearchView searchView = (SearchView) menuItem.getActionView();
                    menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionExpand(@NonNull MenuItem menuItem) {
                            setupUI(true);
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionCollapse(@NonNull MenuItem menuItem) {
                            setupUI(false);
                            return true;
                        }
                    });
                    if (searchView != null) {
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                movieViewModel.setQuery(query);
                                return true;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                return true;
                            }
                        });
                    }
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        movieViewModel.getQueryLiveData().observe(getViewLifecycleOwner(), query -> {
            Log.d("SearchQuery", "live data observed");
            if (query != null) {
                Integer language = languageViewModel.getLangLiveData().getValue();
                Log.d("SearchQuery", "Query with lang: " + language);
                searchCompositeDisposable.add(movieViewModel.getSearchedMoviesPagingData(language, query)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                movies -> searchMovieAdapter.submitData(getLifecycle(), movies),
                                error -> Toast.makeText(requireContext(), "Error while loading the data", Toast.LENGTH_SHORT).show()
                        ));
            }
        });

        languageViewModel.getLangLiveData().observe(getViewLifecycleOwner(), integer ->
                popularCompositeDisposable.add(movieViewModel.getPopularMoviesPagingData(integer)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                movies -> popularMovieAdapter.submitData(getLifecycle(), movies),
                                error -> Toast.makeText(requireContext(), "Error while loading the data", Toast.LENGTH_SHORT).show()
                        ))
        );

        Objects.requireNonNull(binding.popularRecyclerView).setAdapter(concatAdapter);
        Objects.requireNonNull(binding.searchRecyclerView).setAdapter(searchMovieAdapter);
        searchMovieAdapter.attachCallback((movie, view1) ->
                findNavController(requireActivity(), R.id.nav_host_fragment_container)
                        .navigate(SearchFragmentDirections.toMovieDetailFragment(movie)));

        popularMovieAdapter.attachCallback((movie, view1) ->
                findNavController(requireActivity(), R.id.nav_host_fragment_container)
                        .navigate(SearchFragmentDirections.toMovieDetailFragment(movie)));

        searchMovieAdapter.addLoadStateListener(loadStates -> {
            boolean isLoading = loadStates.getRefresh() instanceof LoadState.Loading;
            boolean isListEmpty = loadStates.getRefresh() instanceof LoadState.NotLoading && searchMovieAdapter.getItemCount() == 0;
            Objects.requireNonNull(binding.nothingFound).setVisibility(isListEmpty ? VISIBLE : GONE);
            binding.searchRecyclerView.setVisibility(!isLoading ? VISIBLE : GONE);
            Objects.requireNonNull(binding.searchProgressBar).setVisibility(isLoading ? VISIBLE : GONE);
            return null;
        });

        popularMovieAdapter.addLoadStateListener(loadStates -> {
            boolean isLoading = loadStates.getRefresh() instanceof LoadState.Loading;
            binding.popularRecyclerView.setVisibility(!isLoading ? VISIBLE : GONE);
            Objects.requireNonNull(binding.popularProgressBar).setVisibility(isLoading ? VISIBLE : GONE);
            return null;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        popularMovieAdapter.detachCallback();
        searchMovieAdapter.detachCallback();
        popularCompositeDisposable.clear();
        searchCompositeDisposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        popularMovieAdapter = null;
        concatAdapter = null;
    }

    public void setupUI(boolean isSearching) {
        int popularVisibility = !isSearching ? VISIBLE : GONE;
        int searchVisibility = isSearching ? VISIBLE : GONE;
        Objects.requireNonNull(binding.searchPart).setVisibility(searchVisibility);
        Objects.requireNonNull(binding.popularPart).setVisibility(popularVisibility);
        Objects.requireNonNull(binding.popularTitle).setVisibility(popularVisibility);
    }
}