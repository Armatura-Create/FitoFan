package com.example.alex.fitofan.utils;

public class FormatTime {

    public static String formatTime(long time) {
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
                int distance = (int) time / 10;
                formatted = String.valueOf(distance) + "m";
                break;
            case StaticValues.WEIGHT:
                int weight = (int) time / 10;
                formatted = String.valueOf(weight) + "kg";
                break;
            case StaticValues.COUNT:
                int count = (int) time / 10;
                formatted = String.valueOf(count) + "kg";
                break;
        }

        return formatted;
    }
}
