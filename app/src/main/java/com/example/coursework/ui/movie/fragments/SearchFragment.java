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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework.ui.movie.adapters.SearchMovieAdapter;
import com.example.coursework.ui.profile.viewmodels.LanguageViewModel;
import com.example.coursework.ui.movie.viewmodels.SearchViewModel;
import com.example.coursework.R;
import com.example.coursework.databinding.FragmentSearchBinding;

import java.util.Objects;


public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private SearchMovieAdapter searchMovieAdapter;
    private SearchViewModel searchViewModel;
    private LanguageViewModel languageViewModel;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        languageViewModel = new ViewModelProvider(requireActivity()).get(LanguageViewModel.class);
        searchMovieAdapter = new SearchMovieAdapter();
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
                            nothingFoundUI(false);
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionCollapse(@NonNull MenuItem menuItem) {
                            return true;
                        }
                    });
                    if (searchView != null) {
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                searchViewModel.setQuery(query);
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

        searchViewModel.getIsError().observe(getViewLifecycleOwner(), this::errorUI);

        searchViewModel.getQueryLiveData().observe(getViewLifecycleOwner(), query -> {
            Log.d("SearchQuery", "live data observed");
            if (query != null) {
                Integer language = languageViewModel.getLangLiveData().getValue();
                Log.d("SearchQuery", "Query with lang: " + language);
                searchViewModel.getSearchedMoviesPagingData(Objects.requireNonNull(language), query).observe(
                        getViewLifecycleOwner(),
                        movies -> searchMovieAdapter.submitData(getLifecycle(), movies)
                );
            }
        });

        binding.warningBtn.setOnClickListener(view2 -> searchViewModel.searchMovies(
                Objects.requireNonNull(languageViewModel.getLangLiveData().getValue()),
                searchViewModel.getQueryLiveData().getValue()
        ));

        binding.searchRecyclerView.setAdapter(searchMovieAdapter);

        searchMovieAdapter.attachCallback((movie, view1) ->
                findNavController(requireActivity(), R.id.nav_host_fragment_container)
                        .navigate(SearchFragmentDirections.toMovieDetailFragment(movie.getId())));

        searchMovieAdapter.addLoadStateListener(loadStates -> {
            boolean isLoading = loadStates.getRefresh() instanceof LoadState.Loading;
            boolean isError = loadStates.hasError();
            boolean isNotLoading = loadStates.getRefresh() instanceof LoadState.NotLoading;
            boolean isListEmpty = isNotLoading && searchMovieAdapter.getItemCount() == 0;
            if (isError) searchViewModel.setIsError(true);
            if (isNotLoading && !isError) {
                searchViewModel.setIsError(false);
            }
            loadingUI(isLoading);
            nothingFoundUI(isListEmpty);
            return null;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        searchMovieAdapter.detachCallback();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchMovieAdapter = null;
    }

    private void errorUI(boolean isError){
        binding.warning.setVisibility(isError ? VISIBLE : GONE);
    }

    private void loadingUI(boolean isLoading){
        binding.searchRecyclerView.setVisibility(!isLoading ? VISIBLE : GONE);
        binding.searchProgressBar.setVisibility(isLoading ? VISIBLE : GONE);
    }

    private void nothingFoundUI(boolean isListEmpty){
        binding.nothingFound.setVisibility(isListEmpty ? VISIBLE : GONE);
    }
}