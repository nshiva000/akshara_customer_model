package com.gmail.hanivisushiva.aksharafinserve.Pages.Admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.gmail.hanivisushiva.aksharafinserve.Adapters.Admin.AdminEnquiryAdapter;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminEnquiries.AdminEnquiries;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminEnquiries.Datum;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminEnquiryActivity extends AppCompatActivity {

    ArrayList<Datum> adminEnquiriesArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    AdminEnquiryAdapter enquiryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_enquiry);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Enquiries");

        }



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        get_enquires();
    }

    private void get_enquires(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading please Wait");
        progressDialog.show();
        Call<AdminEnquiries> call = RetrofitClient.getmInstance().getApi().get_admin_enquiries();
        call.enqueue(new Callback<AdminEnquiries>() {
            @Override
            public void onResponse(Call<AdminEnquiries> call, Response<AdminEnquiries> response) {
                progressDialog.dismiss();
                AdminEnquiries adminEnquiries = response.body();
                if (adminEnquiries != null){
                    if (adminEnquiries.getStatus()){
                      adminEnquiriesArrayList.addAll(adminEnquiries.getData());

                      AdminEnquiryAdapter adminEnquiryAdapter = new AdminEnquiryAdapter(getApplicationContext(),adminEnquiriesArrayList);
                      recyclerView.setAdapter(adminEnquiryAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<AdminEnquiries> call, Throwable t) {
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

