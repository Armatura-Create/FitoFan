package com.example.alex.fitofan.ui.activity.settings;

import android.content.Context;

public interface SettingsContact {

    interface View {
        Context getContext();

    }

    interface EventListener{
        void onContinue(int count);
    }

}
