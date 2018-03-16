package com.example.alex.fitofan.ui.activity.training;

import android.content.Context;

public interface TrainingContact {

    interface View {
        Context getContext();

        void onBackPressed();

        void close();
    }

    interface EventListener{

        void close();
    }

}
