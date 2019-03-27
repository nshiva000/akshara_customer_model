package com.gmail.hanivisushiva.aksharafinserve.Pages.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.Models.AdminLatestUpdates.AdminLatestUpdates;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLatestUpdatesActivity extends AppCompatActivity {

    EditText name,des,url;
    TextInputLayout name_layout,des_layout,url_layout;
    String name_txt,des_txt,url_txt;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_latest_updates);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Latest Updates");

        }


        name = findViewById(R.id.name);
        des = findViewById(R.id.description);
        url = findViewById(R.id.url);

        name_layout = findViewById(R.id.name_layout);
        des_layout = findViewById(R.id.description_layout);
        url_layout = findViewById(R.id.url_layout);

        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_txt = name.getText().toString();
                des_txt = des.getText().toString();
                url_txt = url.getText().toString();

                if (TextUtils.isEmpty(name_txt)){
                    name_layout.setError("Name is required");
                    name_layout.requestFocus();
                    return;
                }

                name_layout.setErrorEnabled(true);
                name_layout.setError(null);

                if (TextUtils.isEmpty(des_txt)){
                    des_layout.setError("Des is required");
                    des_layout.requestFocus();
                    return;
                }

                des_layout.setErrorEnabled(true);
                des_layout.setError(null);

                if (TextUtils.isEmpty(url_txt)){
                    url_layout.setError("Url is required");
                    url_layout.requestFocus();
                    return;
                }

                url_layout.setErrorEnabled(true);
                url_layout.setError(null);







                latest_update();
            }
        });
    }

    private void latest_update(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading, Please Wait");
        progressDialog.show();
        Call<AdminLatestUpdates> call = RetrofitClient.getmInstance().getApi().admin_latest_updates(name_txt,des_txt,url_txt);
        call.enqueue(new Callback<AdminLatestUpdates>() {
            @Override
            public void onResponse(Call<AdminLatestUpdates> call, Response<AdminLatestUpdates> response) {
                progressDialog.dismiss();
                AdminLatestUpdates adminLatestUpdates = response.body();
                if (adminLatestUpdates != null){
                    if (adminLatestUpdates.getStatus()){
                        showAlertSuccess();
                        Toast("successfully added");
                    }else {
                        ShowAlertError();
                        Toast(adminLatestUpdates.getMessage());
                    }
                }else {
                    ShowAlertError();
                    Toast("Server Not responding,try again later");
                }
            }

            @Override
            public void onFailure(Call<AdminLatestUpdates> call, Throwable t) {
                progressDialog.dismiss();
                ShowAlertError();
                Toast("Server Not responding,try again later");
            }
        });
    }


    private void Toast(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void showAlertSuccess(){
        new android.app.AlertDialog.Builder(this)
                .setMessage("Updated Sent")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void ShowAlertError(){
        new android.app.AlertDialog.Builder(this)
                .setMessage("Something Went wrong, please try again later")
                .setCancelable(true)
                .setPositiveButton("Ok",null)
                .show();
    }
}


