package com.example.alex.fitofan.utils;

public class FormatTime {

    public static String formatTime(long time) {
        String formatted = "";
        long hours = ((time / 1000) / 3600) % 24,
                minute = ((time / 1000) / 60L) % 60L,
                second = (time / 1000) % 60L;
        if (hours > 0) {
            formatted = String.format("%02d:%02d:%02d", hours, minute, second);
        } else {
            formatted = String.format("%02d:%02d", minute, second);
        }

        return formatted;
    }
}
