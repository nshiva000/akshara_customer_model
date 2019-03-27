package com.gmail.hanivisushiva.aksharafinserve.Pages.Customer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Enquiry extends AppCompatActivity {

    EditText rid,phone,name,enquiry,email;
    Button send;

    String txt_rid,txt_phone,txt_name,txt_email,txt_enquiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        rid = findViewById(R.id.rid);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        enquiry = findViewById(R.id.remarks);
        send = findViewById(R.id.query_send);

        rid.setText(SharedPrefManager.get_mInstance(getApplicationContext()).getRid());
        email.setText(SharedPrefManager.get_mInstance(getApplicationContext()).getEmail());
        phone.setText(SharedPrefManager.get_mInstance(getApplicationContext()).getMobile());
        name.setText(SharedPrefManager.get_mInstance(getApplicationContext()).getName());


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                txt_rid = rid.getText().toString().trim();
                txt_email = email.getText().toString().trim();
                txt_phone = phone.getText().toString().trim();
                txt_name = name.getText().toString().trim();
                txt_enquiry = enquiry.getText().toString().trim();


                Call<ResponseBody> responseBodyCall = RetrofitClient.getmInstance().getApi().enquiry(txt_rid,txt_name,txt_email,txt_phone,txt_enquiry);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Successfully Sent",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();
                    }
                });





            }
        });





    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
