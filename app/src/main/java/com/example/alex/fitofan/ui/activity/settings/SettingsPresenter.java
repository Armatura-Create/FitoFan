package com.example.alex.fitofan.ui.activity.settings;

public class SettingsPresenter implements SettingsContact.EventListener{

    private SettingsContact.View view;

    SettingsPresenter(SettingsContact.View view) {
        this.view = view;
    }

}
