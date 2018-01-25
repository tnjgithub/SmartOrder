package com.hanibey.smartorderbusiness;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Tanju on 8.10.2017.
 */

public class InformationService {

    Context context;
    public InformationService(Context con){
        context = con;
    }

    public void alertDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Uyarı");
        builder.setMessage(message);

        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
