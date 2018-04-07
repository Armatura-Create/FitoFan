package com.example.alex.fitofan.utils;

import android.annotation.SuppressLint;

public class FormatTime {

    @SuppressLint("DefaultLocale")
    public static String formatCountWithDimension(long time) {
        String formatted = "";
        int type = (int) time % 10;

        switch (type) {
            case StaticValues.TIME:
                long hours = ((time / 10000) / 3600) % 24,
                        minute = ((time / 10000) / 60L) % 60L,
                        second = (time / 10000) % 60L;
                if (hours > 0)
                    formatted = String.format("%02d:%02d:%02d", hours, minute, second);
                else
                    formatted = String.format("%02d:%02d", minute, second);
                break;
            case StaticValues.DISTANCE:
                float distance = time / 10;
                if (distance < 1000)
                    formatted = String.valueOf(distance) + "m";
                else
                    formatted = String.valueOf(distance / 1000) + "km";
                break;
            case StaticValues.WEIGHT:
                int weight = (int) time / 10;
                formatted = String.valueOf(weight) + "kg";
                break;
            case StaticValues.COUNT:
                int count = (int) time / 10;
                formatted = String.valueOf(count) + "rep";
                break;
        }

        return formatted;
    }

    @SuppressLint("DefaultLocale")
    public static String formatCount(long time) {
        String formatted = "";
        int type = (int) time % 10;

        switch (type) {
            case StaticValues.TIME:
                long hours = ((time / 10000) / 3600) % 24,
                        minute = ((time / 10000) / 60L) % 60L,
                        second = (time / 10000) % 60L;
                if (hours > 0)
                    formatted = String.format("%02d:%02d:%02d", hours, minute, second);
                else
                    formatted = String.format("%02d:%02d", minute, second);
                break;
            case StaticValues.DISTANCE:
                float distance = time / 10;
                if (distance < 1000)
                    formatted = String.valueOf(distance);
                else
                    formatted = String.valueOf(distance / 1000);
                break;
            case StaticValues.WEIGHT:
                int weight = (int) time / 10;
                formatted = String.valueOf(weight);
                break;
            case StaticValues.COUNT:
                int count = (int) time / 10;
                formatted = String.valueOf(count);
                break;
        }

        return formatted;
    }

    @SuppressLint("DefaultLocale")
    public static String formatTime(long time) {
        String formatted;

        long hours = ((time / 1000) / 3600) % 24,
                minute = ((time / 1000) / 60L) % 60L,
                second = (time / 1000) % 60L;
        if (hours > 0)
            formatted = String.format("%02d:%02d:%02d", hours, minute, second);
        else
            formatted = String.format("%02d:%02d", minute, second);

        return formatted;
    }


    @SuppressLint("DefaultLocale")
    public static String formatType(long time) {
        String formatted = "";
        int type = (int) time % 10;

        switch (type) {
            case StaticValues.DISTANCE:
                float distance = time / 10;
                if (distance < 1000)
                    formatted = "m";
                else
                    formatted = "km";
                break;
            case StaticValues.WEIGHT:
                formatted = "kg";
                break;
            case StaticValues.COUNT:
                formatted = "rep";
                break;
        }

        return formatted;
    }
}
