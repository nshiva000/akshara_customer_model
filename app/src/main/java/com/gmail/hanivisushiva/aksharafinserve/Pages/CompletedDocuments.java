package com.gmail.hanivisushiva.aksharafinserve.Pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.gmail.hanivisushiva.aksharafinserve.FinishedDocAdapter;
import com.gmail.hanivisushiva.aksharafinserve.Models.FinishedDoc.Datum;
import com.gmail.hanivisushiva.aksharafinserve.Models.FinishedDoc.FinishedDoc;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedDocuments extends AppCompatActivity {
    String rid;
    RecyclerView recyclerView;
    ArrayList<Datum> datumList = new ArrayList<>();
    FinishedDocAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_documents);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rid = SharedPrefManager.get_mInstance(getApplicationContext()).getRid();
        getYourDoc(rid);


    }

    private void getYourDoc(String rid){
        Call<FinishedDoc> yourDocCall = RetrofitClient.getmInstance().getApi().finshed_doc(rid);

        yourDocCall.enqueue(new Callback<FinishedDoc>() {
            @Override
            public void onResponse(Call<FinishedDoc> call, Response<FinishedDoc> response) {
                FinishedDoc yourDoc = response.body();

                assert yourDoc != null;
                if (yourDoc.getStatus()){


                    int dataSize = yourDoc.getData().size();

                    for (int i = 0; i < dataSize;i++){

                        datumList.add(yourDoc.getData().get(i));


                    }

                    Log.e("listsize",datumList.size()+"");

                }








                adapter = new FinishedDocAdapter(getApplicationContext(), datumList);

                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<FinishedDoc> call, Throwable t) {



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


    private void sortArrayList() {

        Collections.sort(datumList, new Comparator<Datum>() {
            public int compare(Datum o1, Datum o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        adapter.notifyDataSetChanged();
    }
}
