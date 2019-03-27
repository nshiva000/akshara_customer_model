package com.gmail.hanivisushiva.aksharafinserve;

import android.content.Context;

public class ShowAlert {

    private Context context;
    private String message;

    public ShowAlert(Context context) {
        this.context = context;
    }






    private void ShowOkAlert(String msg){

        new android.app.AlertDialog.Builder(context)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Ok", null)
                .show();

    }


    private void ShowSuccessAlert(String msg){

        new android.app.AlertDialog.Builder(context)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Ok", null)
                .show();

    }

    private void ShowErrorAlert(String msg){

        new android.app.AlertDialog.Builder(context)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Ok", null)
                .show();

    }
}
