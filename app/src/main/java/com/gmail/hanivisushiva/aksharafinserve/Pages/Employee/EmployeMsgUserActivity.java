package com.gmail.hanivisushiva.aksharafinserve.Pages.Employee;

import android.app.ProgressDialog;
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

import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeMsgUser.EmployeMsgUser;
import com.gmail.hanivisushiva.aksharafinserve.Models.UserDataModel.UserDataModel;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeMsgUserActivity extends AppCompatActivity {

    Spinner spin;
    Button send;
    EditText remark;
    String remark_string;
    TextInputLayout textInputLayout;

    ArrayList<String> users_names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe_msg_user);

        spin = findViewById(R.id.user_spin);
        send = findViewById(R.id.send_btn);
        remark = findViewById(R.id.message);
        textInputLayout = findViewById(R.id.message_layout);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Message User");

        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String spin_text = spin.getSelectedItem().toString();
                String remarks_txt = remark.getText().toString();

                if (TextUtils.isEmpty(remarks_txt)){
                    textInputLayout.setError("Remarks Required");
                    textInputLayout.requestFocus();
                    return;
                }

                textInputLayout.setError(null);
                textInputLayout.setErrorEnabled(true);

                Log.e("log",spin_text+"--"+remarks_txt);
                msg_user(spin_text,remarks_txt, SharedPrefManager.get_mInstance(getApplicationContext()).getRid());

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

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EmployeMsgUserActivity.this,android.R.layout.simple_dropdown_item_1line,users_names);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin.setAdapter(adapter);
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    private void msg_user(String user,String remark,String rid){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading,please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<EmployeMsgUser> call = RetrofitClient.getmInstance().getApi().employee_msg_user(user,remark,rid);

        call.enqueue(new Callback<EmployeMsgUser>() {
            @Override
            public void onResponse(Call<EmployeMsgUser> call, Response<EmployeMsgUser> response) {
                progressDialog.dismiss();
                EmployeMsgUser employeMsgUser = response.body();
                if (employeMsgUser != null){
                    if (employeMsgUser.getStatus()){
                        Toast("message sent successfully");
                    }else {
                        Toast("message not sent");
                    }
                }else {
                    Toast("server not responding,try again later");
                }
            }

            @Override
            public void onFailure(Call<EmployeMsgUser> call, Throwable t) {
                progressDialog.dismiss();
                Toast("server not responding,try again later");
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
}
