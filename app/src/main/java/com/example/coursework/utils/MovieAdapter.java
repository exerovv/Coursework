package com.example.coursework.utils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coursework.databinding.ListItemBinding;
import com.example.coursework.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    public List<Movie> movies = new ArrayList<>();

    public void setMovies(List<Movie> newMovies) {
        this.movies = newMovies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemBinding binding = ListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ListItemBinding binding;

        public MyViewHolder(ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void bind(Movie movie){
            Glide
                    .with(itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500" + movie.getPoster_path())
                    .into(binding.poster);
            binding.title.setText(movie.getTitle());
            Double filmRating = movie.getRating();
            binding.rating.setText(String.format("%.1f", filmRating));
            if (filmRating < 4.5) binding.rating.setTextColor(Color.RED);
            else if (filmRating >= 4.5 && filmRating < 6.5)  binding.rating.setTextColor(Color.GRAY);
            else  binding.rating.setTextColor(Color.GREEN);

        }
    }
}