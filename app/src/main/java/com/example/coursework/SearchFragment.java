package com.example.coursework;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coursework.databinding.FragmentSearchBinding;
import com.example.coursework.utils.MovieAdapter;
import com.example.coursework.utils.MovieDiffUtils;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private MovieAdapter adapter;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        adapter = new MovieAdapter();
        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        binding.recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MovieViewModel movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.state.observe(getViewLifecycleOwner(), state -> {
            if (state.error != null) {
                Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show();
            }
            if (state.movies != null) {
                DiffUtil.Callback util = new MovieDiffUtils(adapter.movies, state.movies);
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(util);
                adapter.setMovies(state.movies);
                diffResult.dispatchUpdatesTo(adapter);
            }
        });

        movieViewModel.fetchPopularMovies(2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}