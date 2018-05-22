package com.example.alex.fitofan.models;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.settings.MApplication;

import java.util.ArrayList;

public class TagModelCard {

    private ArrayList<String> tag;

    public TagModelCard() {
        tag = new ArrayList<>();
        setData();
    }

    private void setData() {
        tag.add(MApplication.getInstance().getResources().getString(R.string.time));
        tag.add(MApplication.getInstance().getResources().getString(R.string.distance));
        tag.add(MApplication.getInstance().getResources().getString(R.string.weight));
        tag.add(MApplication.getInstance().getResources().getString(R.string.count));
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }
}
