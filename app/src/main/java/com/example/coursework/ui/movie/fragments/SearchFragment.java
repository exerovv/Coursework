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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private SearchMovieAdapter searchMovieAdapter;
    private SearchViewModel searchViewModel;
    private LanguageViewModel languageViewModel;
    private final CompositeDisposable searchCompositeDisposable = new CompositeDisposable();

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
                            binding.warning.setVisibility(GONE);
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionCollapse(@NonNull MenuItem menuItem) {
                            binding.nothingFound.setVisibility(GONE);
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

        searchViewModel.getQueryLiveData().observe(getViewLifecycleOwner(), query -> {
            Log.d("SearchQuery", "live data observed");
            if (query != null) {
                Integer language = languageViewModel.getLangLiveData().getValue();
                Log.d("SearchQuery", "Query with lang: " + language);
                searchCompositeDisposable.add(searchViewModel.getSearchedMoviesPagingData(language, query)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                movies -> searchMovieAdapter.submitData(getLifecycle(), movies)
                        ));
            }
        });

        binding.searchRecyclerView.setAdapter(searchMovieAdapter);

        searchMovieAdapter.attachCallback((movie, view1) ->
                findNavController(requireActivity(), R.id.nav_host_fragment_container)
                        .navigate(SearchFragmentDirections.toMovieDetailFragment(movie.getId())));

        searchMovieAdapter.addLoadStateListener(loadStates -> {
            boolean isLoading = loadStates.getRefresh() instanceof LoadState.Loading;
            boolean isError = loadStates.hasError();
            boolean isNotLoading = loadStates.getRefresh() instanceof LoadState.NotLoading;
            boolean isListEmpty = isNotLoading && searchMovieAdapter.getItemCount() == 0;
            if (isError && searchMovieAdapter.getItemCount() == 0) errorUI(true);
            if (isNotLoading && !isError) errorUI(false);
            binding.nothingFound.setVisibility(isListEmpty ? VISIBLE : GONE);
            binding.searchRecyclerView.setVisibility(!isLoading ? VISIBLE : GONE);
            binding.searchProgressBar.setVisibility(isLoading ? VISIBLE : GONE);
            return null;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        searchMovieAdapter.detachCallback();
        searchCompositeDisposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchMovieAdapter = null;
    }

    public void errorUI(boolean isError){
        binding.warning.setVisibility(isError ? VISIBLE : GONE);
        binding.mainPart.setVisibility(!isError ? VISIBLE : GONE);
    }
}