package com.example.alex.fitofan.models;

import java.util.ArrayList;

public class SettingsModel {
    private ArrayList<String> menuItems;

    public SettingsModel() {
        menuItems = new ArrayList<>();
        menuItems.add("Данные профиля");
        menuItems.add("Пароль");
    }

    public ArrayList<String> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ArrayList<String> menuItems) {
        this.menuItems = menuItems;
    }
}
