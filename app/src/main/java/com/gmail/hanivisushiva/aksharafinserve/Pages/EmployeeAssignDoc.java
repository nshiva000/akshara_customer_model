package com.gmail.hanivisushiva.aksharafinserve.Pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.Adapters.Admin.AdminEmployeeAssignDocAdapter;
import com.gmail.hanivisushiva.aksharafinserve.Models.AssignDoc.AssignDocData;
import com.gmail.hanivisushiva.aksharafinserve.Models.AssignDoc.Datum;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeAssignDoc extends AppCompatActivity {

    String eid;
    ArrayList<Datum> datumList = new ArrayList<>();
    AdminEmployeeAssignDocAdapter adminEmployeeAssignDocAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_employee_assign_doc);


        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        eid = SharedPrefManager.get_mInstance(getApplicationContext()).getRid();

        Call<AssignDocData> assignDocDataCall = RetrofitClient.getmInstance().getApi().get_assign_doc(eid);

        assignDocDataCall.enqueue(new Callback<AssignDocData>() {
            @Override
            public void onResponse(Call<AssignDocData> call, Response<AssignDocData> response) {
                AssignDocData assignDocData = response.body();

                if (assignDocData.getStatus()){
                    for (int i = 0; i<assignDocData.getData().size();i++){
                        datumList.add(assignDocData.getData().get(i));
                    }

                    if (assignDocData.getData().size() == 0){
                        Toast.makeText(getApplicationContext(),"there are no assigned files",Toast.LENGTH_SHORT).show();
                    }


                    adminEmployeeAssignDocAdapter = new AdminEmployeeAssignDocAdapter(getApplicationContext(),datumList);

                    //setting adapter to recyclerview
                    recyclerView.setAdapter(adminEmployeeAssignDocAdapter);
                }
            }

            @Override
            public void onFailure(Call<AssignDocData> call, Throwable t) {

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
