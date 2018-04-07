package com.example.alex.fitofan.utils;

import android.content.Context;
import android.graphics.Typeface;

public class CustomFontsLoader {

    public static final int FONT_COND_PRO =   0;
    public static final int FONT_NAME_2 =   1;
    public static final int FONT_NAME_3 =   2;

    private static final int NUM_OF_CUSTOM_FONTS = 1;

    private static boolean fontsLoaded = false;

    private static Typeface[] fonts = new Typeface[1];

    private static String[] fontPath = {
            "fonts/text_cond_pro.ttf"
    };


    /**
     * Returns a loaded custom font based on it identifier.
     *
     * @param context - the current context
     * @param fontIdentifier = the identifier of the requested font
     *
     * @return Typeface object of the requested font.
     */
    public static Typeface getTypeface(Context context, int fontIdentifier) {
        if (!fontsLoaded) {
            loadFonts(context);
        }
        return fonts[fontIdentifier];
    }


    private static void loadFonts(Context context) {
        for (int i = 0; i < NUM_OF_CUSTOM_FONTS; i++) {
            fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
        }
        fontsLoaded = true;

    }
}
