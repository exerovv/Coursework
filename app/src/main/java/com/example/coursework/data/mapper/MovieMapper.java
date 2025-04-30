package com.example.coursework.data.mapper;

import com.example.coursework.data.model.MovieDTO;
import com.example.coursework.domain.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieMapper {
    public static Movie toMovie(MovieDTO dto){
        return new Movie(
                dto.id,
                dto.title,
                dto.overview,
                dto.poster_path,
                dto.rating
        );
    }

    public static List<Movie> toMovieList(List<MovieDTO> dtoList){
        ArrayList<Movie> movies = new ArrayList<>();
        if (dtoList != null){
            for (MovieDTO dto : dtoList){
                movies.add(toMovie(dto));
            }
        }
        return movies;
    }

}
