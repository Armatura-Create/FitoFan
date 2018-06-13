package com.example.alex.fitofan.ui.activity.main;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.utils.CustomDialog.CustomDialog;

public class MainPresenter implements MainContract.EventListener {

    private MainContract.View view;

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void alertExit() {

        Dialog dialog = CustomDialog.dialogSimple(view.getContext(),
                view.getContext().getResources().getString(R.string.sing_out),
                view.getContext().getResources().getString(R.string.sure),
                view.getContext().getResources().getString(R.string.yes),
                view.getContext().getResources().getString(R.string.no));
        dialog.findViewById(R.id.bt_positive).setOnClickListener(v -> view.goSingOut());
        dialog.findViewById(R.id.bt_negative).setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void shareApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "#thisisFitoFan");
            String sAux = "\nLet me recommend you this app\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.fitofan \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            view.getContext().startActivity(Intent.createChooser(i, "Choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    @Override
    public void rateApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.fitofan"));
        view.getContext().startActivity(intent);
    }
}
