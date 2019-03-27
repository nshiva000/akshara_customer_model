package com.gmail.hanivisushiva.aksharafinserve.Pages.Admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.gmail.hanivisushiva.aksharafinserve.Adapters.Admin.EmployeDataAdapter;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeData.Datum;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeData.EmployeData;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeDataActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    EmployeDataAdapter employeDataAdapter;
    ArrayList<Datum> datumArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe_data);
         recyclerView = findViewById(R.id.recyclerView);
         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Employee Data");
        }


        get_employee_data();
    }

    private void get_employee_data(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading .. Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<EmployeData> employeDataCall = RetrofitClient.getmInstance().getApi().get_employee_data();
        employeDataCall.enqueue(new Callback<EmployeData>() {
            @Override
            public void onResponse(Call<EmployeData> call, Response<EmployeData> response) {
                progressDialog.dismiss();
                EmployeData employeData = response.body();

                if (employeData != null){
                    if (employeData.getStatus()){
                        datumArrayList.addAll(employeData.getData());
                     EmployeDataAdapter employeDataAdapter = new EmployeDataAdapter(getApplicationContext(),datumArrayList);
                     recyclerView.setAdapter(employeDataAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<EmployeData> call, Throwable t) {
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
