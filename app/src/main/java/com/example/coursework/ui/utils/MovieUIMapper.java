package com.example.coursework.ui.utils;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

import android.content.Context;
import android.graphics.Color;

import com.example.coursework.R;

import java.util.Objects;

public class MovieUIMapper {
    public static int getRatingColor(String rating){
        double filmRate = Double.parseDouble(rating.replace(",", "."));
        if (filmRate < 4.5) return Color.RED;
        else if (filmRate >= 4.5 && filmRate < 6.5) return Color.GRAY;
        else return Color.GREEN;
    }

    public static String getOverviewOrBlank(String overview, Context context){
        return !Objects.equals(overview, "") ? overview : context.getString(R.string.blank_overview);
    }

    public static int getTextAlignment(String overview){
        return Objects.equals(overview, "") ? TEXT_ALIGNMENT_CENTER : TEXT_ALIGNMENT_TEXT_START;
    }
}
