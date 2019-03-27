package com.gmail.hanivisushiva.aksharafinserve.Pages.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.Models.AdminPhoneMsg.AdminPhoneMsg;
import com.gmail.hanivisushiva.aksharafinserve.Models.Services.Services;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.ShowAlert;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPhoneMsgActivity extends AppCompatActivity {

    Spinner spinner;
    EditText remarks;
    TextInputLayout remarks_layout;
    Button send;

    String spinner_txt,remarks_txt;
    ArrayList<String> arrayList = new ArrayList<>();
    HashMap<String,String> process = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_phone_msg);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Phone Message");
        }



        spinner = findViewById(R.id.spinner_name);
        remarks = findViewById(R.id.remarks);

        remarks_layout = findViewById(R.id.remarks_layout);
        send = findViewById(R.id.send);

        get_services();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (process.get(spinner.getSelectedItem().toString()) != null){
                    spinner_txt = process.get(spinner.getSelectedItem().toString());
                }
                remarks_txt = remarks.getText().toString();

                if (TextUtils.isEmpty(remarks_txt)){
                    remarks_layout.setError("Remarks required");
                    remarks_layout.requestFocus();
                    return;
                }

                remarks_layout.setError(null);
                remarks_layout.setErrorEnabled(true);

                send_phone_message();
            }
        });
    }


    private void get_services(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading,Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<Services> servicesCall = RetrofitClient.getmInstance().getApi().get_admin_services();

        servicesCall.enqueue(new Callback<Services>() {
            @Override
            public void onResponse(Call<Services> call, Response<Services> response) {
                Services services = response.body();

                if (services != null){
                    if (services.getStatus()){

                        for (int i = 0; i < services.getData().size(); i++){
                            arrayList.add(services.getData().get(i).getServices());
                            process.put(services.getData().get(i).getServices(),services.getData().get(i).getId());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminPhoneMsgActivity.this,android.R.layout.simple_dropdown_item_1line,arrayList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Services> call, Throwable t) {
               progressDialog.dismiss();
            }
        });
    }


    private void send_phone_message(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading,Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<AdminPhoneMsg> adminPhoneMsgCall = RetrofitClient.getmInstance().getApi().send_phone_msg(spinner_txt,remarks_txt);

        Log.e("is",spinner_txt+"iii");

        adminPhoneMsgCall.enqueue(new Callback<AdminPhoneMsg>() {
            @Override
            public void onResponse(Call<AdminPhoneMsg> call, Response<AdminPhoneMsg> response) {
                AdminPhoneMsg adminPhoneMsg = response.body();

                Log.e("ss",adminPhoneMsg.toString()+"result");
                if (adminPhoneMsg != null){
                    if (adminPhoneMsg.getStatus()){
                        Toast(adminPhoneMsg.getMessage());
                        showAlertSuccess();
                    }else {
                        Toast(adminPhoneMsg.getMessage());
                        ShowAlertError("status is false");
                    }
                }else {
                    ShowAlertError("response is null");
                    Toast("Server Error, please try again later");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<AdminPhoneMsg> call, Throwable t) {
                ShowAlertError("Server Error, please try again later");
                Toast("Server Error, please try again later");
                Log.e("error",t.toString());
                progressDialog.dismiss();
            }
        });
    }


    private void Toast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
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
                .setMessage("Message sent Successfully")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void ShowAlertError(String msg){
        new android.app.AlertDialog.Builder(this)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Ok",null)
                .show();
    }
}

