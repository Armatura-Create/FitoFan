package com.example.alex.fitofan.ui.activity.training;

import android.content.Context;

public interface TrainingContact {

    interface View {
        Context getContext();

        void onBackPressed();

    }

    interface EventListener{

    }

}
