package com.gmail.hanivisushiva.aksharafinserve.Pages.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.Adapters.Admin.AdminCompletedDocumentsAdapter;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminCompletedDocuments.AdminCompletedDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminCompletedDocuments.Datum;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Welcome;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCompletdDocumnetsActivity extends AppCompatActivity {

    ArrayList<Datum> datumArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    AdminCompletedDocumentsAdapter adminCompletedDocumentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_completd_documnets);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Completed Documents");

        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        get_completed_documents();
    }


    private void get_completed_documents(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading, please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<AdminCompletedDocuments> adminCompletdDocumnetsActivityCall = RetrofitClient.getmInstance().getApi().get_admin_completed_documents();

        adminCompletdDocumnetsActivityCall.enqueue(new Callback<AdminCompletedDocuments>() {
            @Override
            public void onResponse(Call<AdminCompletedDocuments> call, Response<AdminCompletedDocuments> response) {
                progressDialog.dismiss();
                AdminCompletedDocuments adminCompletedDocuments = response.body();
                if (adminCompletedDocuments != null){
                    if (adminCompletedDocuments.getStatus()){
                      datumArrayList.addAll(adminCompletedDocuments.getData());
                      AdminCompletedDocumentsAdapter adminCompletedDocumentsAdapter = new AdminCompletedDocumentsAdapter(AdminCompletdDocumnetsActivity.this,datumArrayList);
                      recyclerView.setAdapter(adminCompletedDocumentsAdapter);
                    }else {
                       Toast("No documents to display");
                    }
                }
            }

            @Override
            public void onFailure(Call<AdminCompletedDocuments> call, Throwable t) {
                progressDialog.dismiss();
                Toast("server not responding Please try again later");
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(AdminCompletdDocumnetsActivity.this,Welcome.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void Toast(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
