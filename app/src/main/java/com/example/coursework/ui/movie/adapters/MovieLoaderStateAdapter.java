package com.example.coursework.ui.movie.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.databinding.LoadStateItemBinding;

public class MovieLoaderStateAdapter extends LoadStateAdapter<MovieLoaderStateAdapter.LoadStateViewHolder> {
    @Override
    public void onBindViewHolder(@NonNull LoadStateViewHolder itemViewHolder, @NonNull LoadState loadState) {
        itemViewHolder.bind(loadState);
    }

    @NonNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull LoadState loadState) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        LoadStateItemBinding binding = LoadStateItemBinding.inflate(layoutInflater, viewGroup, false);
        return new LoadStateViewHolder(binding);

    }
    static class LoadStateViewHolder extends RecyclerView.ViewHolder {
        private final LoadStateItemBinding binding;
        public LoadStateViewHolder(@NonNull LoadStateItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LoadState loadState) {
            if (loadState instanceof LoadState.Error){
                binding.errorMsg.setText(((LoadState.Error) loadState).getError().getLocalizedMessage());
            }
            binding.progressBar.setVisibility(loadState instanceof LoadState.Loading ? VISIBLE : GONE);
            binding.errorMsg.setVisibility(!(loadState instanceof LoadState.Loading) ? VISIBLE : GONE);
        }
    }
}

