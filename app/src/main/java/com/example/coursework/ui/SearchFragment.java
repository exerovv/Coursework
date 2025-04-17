package com.example.coursework.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.ConcatAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coursework.MovieViewModel;
import com.example.coursework.R;
import com.example.coursework.databinding.FragmentSearchBinding;
import com.example.coursework.ui.adapters.MovieAdapter;
import com.example.coursework.ui.adapters.MovieLoaderStateAdapter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private MovieAdapter adapter;
    private MovieViewModel movieViewModel;
    private ConcatAdapter concatAdapter;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        adapter = new MovieAdapter();
        concatAdapter = adapter.withLoadStateFooter(new MovieLoaderStateAdapter());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setAdapter(concatAdapter);
        compositeDisposable.add(movieViewModel.getPagingData().observeOn(AndroidSchedulers.mainThread()).subscribe(
                movies -> adapter.submitData(getLifecycle(), movies),
                error -> Toast.makeText(requireContext(), "Error while loading the data", Toast.LENGTH_SHORT).show()
        ));
        adapter.attachCallback((movie, view1) ->
                findNavController(requireActivity(), R.id.nav_host_fragment_container)
                        .navigate(SearchFragmentDirections.toMovieDetail(movie))
        );
        adapter.addLoadStateListener(loadStates -> {
            boolean isLoading = loadStates.getRefresh() instanceof LoadState.Loading;
            binding.recyclerView.setVisibility(!isLoading ? VISIBLE : GONE);
            binding.progressBar.setVisibility(isLoading ? VISIBLE : GONE);
            return null;
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        adapter.detachCallback();
        compositeDisposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        concatAdapter = null;
    }
}