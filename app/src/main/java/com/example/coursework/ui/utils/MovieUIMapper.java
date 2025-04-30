package com.example.coursework.ui.utils;

import android.graphics.Color;

public class MovieUIMapper {
    public static int getRatingColor(String rating){
        double filmRate = Double.parseDouble(rating.replace(",", "."));
        if (filmRate < 4.5) return Color.RED;
        else if (filmRate >= 4.5 && filmRate < 6.5) return Color.GRAY;
        else return Color.GREEN;
    }
}
