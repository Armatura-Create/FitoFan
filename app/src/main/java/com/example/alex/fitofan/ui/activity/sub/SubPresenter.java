package com.example.alex.fitofan.ui.activity.sub;


import android.app.Dialog;
import android.content.Intent;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.utils.CustomDialog;

public class SubPresenter implements SubContract.EventListener {

    private SubContract.View view;

    public SubPresenter(SubContract.View view) {
        this.view = view;
    }

    @Override
    public void alertExit() {

        Dialog dialog = CustomDialog.dialogSimple(view.getContext(),
                "Sing Out",
                "Are you sure?",
                "Yes",
                "No");
        dialog.findViewById(R.id.bt_positive).setOnClickListener(v -> view.goSingOut());
        dialog.findViewById(R.id.bt_negative).setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void shareApp() {
        //TODO Разобраться с этим
//        ProgressDialog dialog = ProgressDialog.show(view.getContext(), "",
//                "Loading. Please wait...", false);
//        dialog.setCancelable(true);
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
