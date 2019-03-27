package com.gmail.hanivisushiva.aksharafinserve.Pages.Admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.gmail.hanivisushiva.aksharafinserve.Models.UserDataModel.Datum;
import com.gmail.hanivisushiva.aksharafinserve.Models.UserDataModel.UserDataModel;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Adapters.Admin.UserDataAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDataActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UserDataAdapter userDataAdapter;

    ArrayList<Datum> datumArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("User Data");
        }



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        get_user_data();
    }

    private void get_user_data(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading .. Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<UserDataModel> userDataModelCall = RetrofitClient.getmInstance().getApi().get_user_data();
        userDataModelCall.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                progressDialog.dismiss();
                UserDataModel userDataModel = response.body();
                if (userDataModel != null){
                    if (userDataModel.getStatus()){
                        datumArrayList.addAll(userDataModel.getData());
                        UserDataAdapter userDataAdapter = new UserDataAdapter(getApplicationContext(),datumArrayList);
                        recyclerView.setAdapter(userDataAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                progressDialog.dismiss();
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
