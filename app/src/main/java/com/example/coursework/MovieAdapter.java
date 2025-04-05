package com.example.coursework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coursework.model.Movie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private List<Movie> movies = new ArrayList<>();

    public void setMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
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
        ImageView poster;
        TextView title;
        TextView rating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.rating);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void bind(Movie movie){
            Glide.with(itemView.getContext()).load("https://image.tmdb.org/t/p/w500" + movie.getPoster_path()).into(poster);
            title.setText(movie.getTitle());
            Double filmRating = movie.getRating();
            rating.setText(String.format("%.1f", filmRating));
            if (filmRating < 4.5) rating.setTextColor(Color.RED);
            else if (filmRating >= 4.5 && filmRating < 6.5) rating.setTextColor(Color.GRAY);
            else rating.setTextColor(Color.GREEN);

        }
    }
}