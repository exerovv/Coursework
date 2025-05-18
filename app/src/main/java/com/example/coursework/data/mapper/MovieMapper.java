package com.example.coursework.data.mapper;

import com.example.coursework.data.model.FavoriteDTO;
import com.example.coursework.data.model.MovieDTO;
import com.example.coursework.domain.model.Movie;

import java.util.ArrayList;
import java.util.List;

//Класс для преобразования DTO объекта в "чистый" из domain слоя
public class MovieMapper {
    //Метод для преобразования одного объекта
    public static Movie toMovie(MovieDTO dto){
        return new Movie(
                dto.id,
                dto.title,
                dto.overview,
                dto.poster_path,
                dto.rating
        );
    }
    //Метод для преобразования списка объектов
    public static List<Movie> toMovieList(List<MovieDTO> dtoList){
        ArrayList<Movie> movies = new ArrayList<>();
        if (dtoList != null){
            for (MovieDTO dto : dtoList){
                movies.add(toMovie(dto));
            }
        }
        return movies;
    }

    public static Movie favoriteToMovie(FavoriteDTO dto){
        return new Movie(
                dto.id,
                dto.title,
                null,
                dto.poster_path,
                null
        );
    }

    public static List<Movie> favoriteToMovieList(List<FavoriteDTO> dtoList){
        ArrayList<Movie> movies = new ArrayList<>();
        if (dtoList != null){
            for (FavoriteDTO dto : dtoList){
                movies.add(favoriteToMovie(dto));
            }
        }
        return movies;
    }

//    public static List<Integer> toIDsList(MovieIDsDTO)


}
