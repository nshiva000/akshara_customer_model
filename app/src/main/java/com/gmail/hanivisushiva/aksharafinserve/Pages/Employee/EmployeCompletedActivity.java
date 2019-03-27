package com.gmail.hanivisushiva.aksharafinserve.Pages.Employee;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.Adapters.Employee.EmployeCompletedDocAdapter;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeCompletedDocuments.Datum;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeCompletedDocuments.EmployeCompletedDocuments;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EmployeCompletedActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<Datum> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe_completed);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Completed Documents");

        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        get_completed_doc();

    }


    private void get_completed_doc(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please Wait..");
        progressDialog.show();
        String id = SharedPrefManager.get_mInstance(getApplicationContext()).getRid();
        Call<EmployeCompletedDocuments> call = RetrofitClient.getmInstance().getApi().get_employe_completed_docs(id);

        call.enqueue(new Callback<EmployeCompletedDocuments>() {
            @Override
            public void onResponse(Call<EmployeCompletedDocuments> call, Response<EmployeCompletedDocuments> response) {
                progressDialog.dismiss();
                EmployeCompletedDocuments employeCompletedDocuments = response.body();

                if (employeCompletedDocuments != null){
                    if (employeCompletedDocuments.getStatus()){

                        arrayList.addAll(employeCompletedDocuments.getData());

                        EmployeCompletedDocAdapter employeCompletedDocAdapter = new EmployeCompletedDocAdapter(getApplicationContext(),arrayList);
                        recyclerView.setAdapter(employeCompletedDocAdapter);

                    }else {
                        Toast.makeText(getApplicationContext(),"No data to show",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<EmployeCompletedDocuments> call, Throwable t) {
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
