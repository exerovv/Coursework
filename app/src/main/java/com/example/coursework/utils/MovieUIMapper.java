package com.example.coursework.utils;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

import android.content.Context;
import android.graphics.Color;

import com.example.coursework.R;

import java.util.Objects;

//Класс с методами для преобразования UI элементов
public class MovieUIMapper {
    //Метод для изменения цвета рейтинга в зависимости от значения
    public static int getRatingColor(String rating){
        double filmRate = Double.parseDouble(rating.replace(",", "."));
        if (filmRate < 4.5) return Color.RED;
        else if (filmRate >= 4.5 && filmRate < 6.5) return Color.GRAY;
        else return Color.GREEN;
    }

    //При пустом описании выводится надпись "Описание еще не добавлено"
    public static String getOverviewOrBlank(String overview, Context context){
        return !Objects.equals(overview, "") ? overview : context.getString(R.string.blank_overview);
    }
    //Изменение выравнивания текста в зависимости от того пустое оно или нет
    public static int getTextAlignment(String overview){
        return Objects.equals(overview, "") ? TEXT_ALIGNMENT_CENTER : TEXT_ALIGNMENT_TEXT_START;
    }
}
