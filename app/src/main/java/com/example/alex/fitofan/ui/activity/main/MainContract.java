package com.example.alex.fitofan.ui.activity.main;

import android.content.Context;
import android.support.v4.view.ViewPager;

/**
 * Created by Alex Kucherenko(Godsmack) on 10/14/17.
 */

interface MainContract {

    interface View {
        Context getContext();

        void goSingOut();

    }

    interface EventListener {
        void alertExit();

        void shareApp();
    }
}
