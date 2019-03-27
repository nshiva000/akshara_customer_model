package com.gmail.hanivisushiva.aksharafinserve.Pages.Customer;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.gmail.hanivisushiva.aksharafinserve.Models.YourDoc.Datum;
import com.gmail.hanivisushiva.aksharafinserve.Models.YourDoc.YourDoc;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;
import com.gmail.hanivisushiva.aksharafinserve.Adapters.Customer.YouDocAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourDocuments extends AppCompatActivity {
   String rid;
   RecyclerView recyclerView;
   ArrayList<Datum> datumList = new ArrayList<>();
    YouDocAdapter adapter;

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
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please Wait");
        progressDialog.show();
        Call<YourDoc> yourDocCall = RetrofitClient.getmInstance().getApi().yourDocuments(rid);

        yourDocCall.enqueue(new Callback<YourDoc>() {
            @Override
            public void onResponse(Call<YourDoc> call, Response<YourDoc> response) {
                YourDoc yourDoc = response.body();

                assert yourDoc != null;
                if (yourDoc.getStatus()){


                    int dataSize = yourDoc.getData().size();

                    for (int i = 0; i < dataSize;i++){

                        datumList.add(yourDoc.getData().get(i));


                    }

                    Log.e("listsize",datumList.size()+"");

                }

                adapter = new YouDocAdapter(YourDocuments.this, datumList);

                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<YourDoc> call, Throwable t) {

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


//    private void sortArrayList() {
//
//        Collections.sort(datumList, new Comparator<Datum>() {
//            public int compare(Datum o1, Datum o2) {
//                if (o1.getDate() == null || o2.getDate() == null)
//                    return 0;
//                return o2.getDate().compareTo(o1.getDate());
//            }
//        });
//
//        adapter.notifyDataSetChanged();
//    }
}
