package com.example.alex.fitofan.ui.activity.main;


import android.app.AlertDialog;
import android.content.Intent;

public class MainPresenter implements MainContract.EventListener {

    private MainContract.View view;

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void alertExit() {
        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
        alertDialog.setTitle("Sing out");
        alertDialog.setMessage("Are you sure?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", (dialogInterface, i) -> dialogInterface.cancel());
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", (dialogInterface, i) -> {
            view.goSingOut();
        });
        alertDialog.show();
    }

    @Override
    public void shareApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "#thisisFitoFan");
            String sAux = "\nLet me recommend you this site\n\n";
            sAux = sAux + "http://tkdo.events/ \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            view.getContext().startActivity(Intent.createChooser(i, "Choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }
}
