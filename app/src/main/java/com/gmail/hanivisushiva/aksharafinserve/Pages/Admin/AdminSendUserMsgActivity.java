package com.gmail.hanivisushiva.aksharafinserve.Pages.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.gmail.hanivisushiva.aksharafinserve.Models.MessageUser.MessageUser;
import com.gmail.hanivisushiva.aksharafinserve.Models.UserDataModel.UserDataModel;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminSendUserMsgActivity extends AppCompatActivity {


    ArrayList<String> users_names = new ArrayList<>();


    Spinner spinner;
    EditText remarks;
    TextInputLayout remarks_layout;
    Button send;

    String spinner_txt,remarks_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_send_user_msg);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Message User");
        }



        spinner = findViewById(R.id.spinner_name);
        remarks = findViewById(R.id.remarks);
        remarks_layout = findViewById(R.id.remarks_layout);
        send = findViewById(R.id.send);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_txt = spinner.getSelectedItem().toString();
                remarks_txt = remarks.getText().toString();

                if (TextUtils.isEmpty(remarks_txt)){
                    remarks_layout.setError("Remarks Required");
                    remarks_layout.requestFocus();
                    return;
                }

                remarks_layout.setErrorEnabled(true);
                remarks_layout.setError(null);

                send_user_msg();


                Log.e("sp",spinner_txt+"==="+remarks_txt);
            }
        });


        get_Users();
    }

    private void get_Users(){

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading,Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<UserDataModel> userDataModelCall = RetrofitClient.getmInstance().getApi().get_user_data();

        userDataModelCall.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                UserDataModel userDataModel = response.body();

                if (userDataModel != null){
                    if (userDataModel.getStatus()){
                        for (int i = 0;i<userDataModel.getData().size();i++){
                            users_names.add(userDataModel.getData().get(i).getName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminSendUserMsgActivity.this,android.R.layout.simple_dropdown_item_1line,users_names);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                 progressDialog.dismiss();
                Toast("Server Error, please try again later");
            }
        });
    }


    private void send_user_msg(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading,Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<MessageUser> messageUserCall = RetrofitClient.getmInstance().getApi().send_msg_user(spinner_txt,remarks_txt);

        messageUserCall.enqueue(new Callback<MessageUser>() {
            @Override
            public void onResponse(Call<MessageUser> call, Response<MessageUser> response) {
                MessageUser messageUser = response.body();

                if (messageUser != null){
                    if (messageUser.getStatus()){
                      Toast("message successfully sent");
                      showAlertSuccess();
                    }else {
                        Toast(messageUser.getMessage());
                        ShowAlertError();
                    }
                }else {
                    ShowAlertError();
                    Toast("Server Error, please try again later");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MessageUser> call, Throwable t) {
                ShowAlertError();
               progressDialog.dismiss();
                Toast("Server Error, please try again later");
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

    private void ShowAlertError(){
        new android.app.AlertDialog.Builder(this)
                .setMessage("Something went Wrong, try again later")
                .setCancelable(true)
                .setPositiveButton("Ok",null)
                .show();
    }
}
