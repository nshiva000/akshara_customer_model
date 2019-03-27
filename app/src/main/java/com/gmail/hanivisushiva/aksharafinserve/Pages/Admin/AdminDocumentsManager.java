package com.gmail.hanivisushiva.aksharafinserve.Pages.Admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.gmail.hanivisushiva.aksharafinserve.Adapters.Admin.AdminDocumentManagerAdapter;
import com.gmail.hanivisushiva.aksharafinserve.Models.CustomDocs.CustomDocs;
import com.gmail.hanivisushiva.aksharafinserve.Models.CustomDocs.Datum;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDocumentsManager extends AppCompatActivity {

    ArrayList<Datum> datumList = new ArrayList<>();
    AdminDocumentManagerAdapter adminDocumentManagerAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_documents);


        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Documents Manager");
        }

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please Wait..");
        progressDialog.show();
        Call<CustomDocs> customDocsCall = RetrofitClient.getmInstance().getApi().get_custom_docs();
        customDocsCall.enqueue(new Callback<CustomDocs>() {
            @Override
            public void onResponse(Call<CustomDocs> call, Response<CustomDocs> response) {
                progressDialog.dismiss();
                CustomDocs customDocs = response.body();

                if (customDocs != null){
                    if (customDocs.getStatus()){
                        for (int i = 0; i<customDocs.getData().size();i++){
                            datumList.add(customDocs.getData().get(i));
                        }


                        adminDocumentManagerAdapter = new AdminDocumentManagerAdapter(datumList,AdminDocumentsManager.this);

                        //setting adapter to recyclerview
                        recyclerView.setAdapter(adminDocumentManagerAdapter);
                    }
                }


            }

            @Override
            public void onFailure(Call<CustomDocs> call, Throwable t) {
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
