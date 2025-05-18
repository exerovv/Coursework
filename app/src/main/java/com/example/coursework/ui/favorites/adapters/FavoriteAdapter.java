package com.example.coursework.ui.favorites.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coursework.R;
import com.example.coursework.databinding.ListFavoriteItemBinding;
import com.example.coursework.domain.model.Movie;
import com.example.coursework.utils.AdapterCallback;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {
    public ArrayList<Movie> mDataList = new ArrayList<>();

    public void setFavoriteList(List<Movie> newList) {
        mDataList.clear();
        mDataList.addAll(newList);
    }

    public AdapterCallback<Movie> mCallback = null;

    public void attachCallback(AdapterCallback<Movie> callback) {
        this.mCallback = callback;
    }

    public void detachCallback() {
        this.mCallback = null;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListFavoriteItemBinding binding = ListFavoriteItemBinding.inflate(inflater, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie movie = mDataList.get(position);
        if (movie != null) {
            holder.bind(movie, mCallback);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ListFavoriteItemBinding binding;

        public MyViewHolder(ListFavoriteItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Movie movie, AdapterCallback<Movie> callback){
            Glide
                    .with(itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                    .centerCrop()
                    .placeholder(R.drawable.no_image_placeholder)
                    .error(R.drawable.no_image_placeholder)
                    .into(binding.poster);
            binding.title.setText(movie.getTitle());
            binding.getRoot().setOnClickListener(view -> callback.onItemClicked(movie, view));
        }
    }
}
