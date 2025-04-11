package com.example.coursework.ui;

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

import com.example.coursework.MovieViewModel;
import com.example.coursework.databinding.FragmentSearchBinding;
import com.example.coursework.model.Movie;
import com.example.coursework.utils.AdapterCallback;
import com.example.coursework.utils.MovieAdapter;
import com.example.coursework.utils.MovieDiffUtils;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private MovieAdapter adapter;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        if (adapter == null){
            adapter = new MovieAdapter();
        }
        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        binding.recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MovieViewModel movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        compositeDisposable.add(movieViewModel.getPagingData().subscribe(
                movies -> adapter.submitData(getLifecycle(), movies ),
                error -> Toast.makeText(requireContext(), "Error while loading the data", Toast.LENGTH_SHORT).show()
        ));

        adapter.attachCallback((model, view1) -> {
//                TODO();
            Toast.makeText(requireContext(), "Детали фильма", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        adapter.detachCallback();
        compositeDisposable.clear();
    }
}