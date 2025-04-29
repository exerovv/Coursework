package com.example.coursework.ui.movie.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coursework.R;
import com.example.coursework.databinding.ListSearchedItemBinding;
import com.example.coursework.domain.model.Movie;
import com.example.coursework.ui.movie.adapters.utils.AdapterCallback;
import com.example.coursework.ui.movie.adapters.utils.MovieDiffUtils;

public class SearchMovieAdapter extends PagingDataAdapter<Movie, SearchMovieAdapter.MyViewHolder> {
    public AdapterCallback<Movie> mCallback = null;
    public void attachCallback(AdapterCallback<Movie> callback) {
        this.mCallback = callback;
    }

    public void detachCallback() {
        this.mCallback = null;
    }

    public SearchMovieAdapter() {
        super(new MovieDiffUtils());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListSearchedItemBinding binding = ListSearchedItemBinding.inflate(inflater, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie movie = getItem(position);
        if (movie != null) {
            holder.bind(movie);
            holder.itemView.setOnClickListener(view -> mCallback.onItemClicked(movie, view));
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ListSearchedItemBinding binding;
        public MyViewHolder(ListSearchedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Movie movie){
            Glide
                    .with(itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                    .centerCrop()
                    .placeholder(R.drawable.no_image_placeholder)
                    .error(R.drawable.no_image_placeholder)
                    .into(binding.poster);
            binding.title.setText(movie.getTitle());
            String filmRating = movie.getRating();
            binding.rating.setText(filmRating);
            binding.rating.setTextColor(movie.getRatingColor(filmRating));
        }
    }
}

