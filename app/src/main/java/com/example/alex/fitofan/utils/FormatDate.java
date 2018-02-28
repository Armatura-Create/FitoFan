package com.example.alex.fitofan.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by st18rai on 07.12.17.
 */

public class FormatDate {

    public static String formatDate(String date) {
        String formatted = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
            sdf.applyPattern("dd-MM-yyyy, E");
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatted = sdf.format(c.getTime());

        return formatted;
    }
}
