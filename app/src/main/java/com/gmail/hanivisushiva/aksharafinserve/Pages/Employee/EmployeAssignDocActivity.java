package com.gmail.hanivisushiva.aksharafinserve.Pages.Employee;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.Adapters.Employee.EmployeAssignDocAdapter;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeAssignDocuments.Datum;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeAssignDocuments.EmployeAssignDocuments;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeAssignDocActivity extends AppCompatActivity {

    RecyclerView recyclerView;


    ArrayList<Datum> datumArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe_assign_doc);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Assign Documents");

        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        get_assign_doc();
    }

    private void get_assign_doc(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait, Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String id = SharedPrefManager.get_mInstance(getApplicationContext()).getRid();
        Log.e("user Id",id);
        Call<EmployeAssignDocuments> datumCall = RetrofitClient.getmInstance().getApi().get_employee_assign_docs(id);
        datumCall.enqueue(new Callback<EmployeAssignDocuments>() {
            @Override
            public void onResponse(Call<EmployeAssignDocuments> call, Response<EmployeAssignDocuments> response) {
                EmployeAssignDocuments employeAssignDocuments = response.body();

                if (employeAssignDocuments != null){
                    if (employeAssignDocuments.getStatus()){
                        datumArrayList.addAll(employeAssignDocuments.getData());
                        EmployeAssignDocAdapter employeAssignDocAdapter = new EmployeAssignDocAdapter(EmployeAssignDocActivity.this,datumArrayList);
                        recyclerView.setAdapter(employeAssignDocAdapter);
                    }else {
                        Toast("No documents to show");
                    }



                }else {
                    Toast("server reponding null value");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<EmployeAssignDocuments> call, Throwable t) {
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

    private void Toast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }


}
