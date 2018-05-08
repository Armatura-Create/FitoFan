package com.example.alex.fitofan.ui.activity.sub;

import android.content.Context;

/**
 * Created by Alex Kucherenko(Godsmack) on 10/14/17.
 */

interface SubContract {

    interface View {
        Context getContext();

        void goSingOut();

    }

    interface EventListener {
        void alertExit();

        void shareApp();
    }
}
