package com.gmail.hanivisushiva.aksharafinserve.Pages.Employee;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeUpdateAssignDocuments.EmployeUpdateAssignDocuments;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeUpdateAssignDocActivity extends AppCompatActivity {
    String did;
    Spinner spinner_ready,spinner_status;
    Button update;
    TextView title;

    ArrayList<String> ready_list = new ArrayList<>();
    ArrayList<String> status_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe_update_assign_doc);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Update Assign Documents");

        }

        spinner_ready = findViewById(R.id.ready_spin);
        spinner_status = findViewById(R.id.status_spin);
        title = findViewById(R.id.title);
        update = findViewById(R.id.update_btn);





        status_list.add("Completed");
        status_list.add("inprocess");

        ready_list.add("Accepted");
        ready_list.add("Rejected");


        ArrayAdapter<String> status_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,status_list);
        status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status.setAdapter(status_adapter);

        ArrayAdapter<String> ready_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ready_list);
        ready_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ready.setAdapter(ready_adapter);

        Intent intent = getIntent();
        did = intent.getStringExtra("did");
        Log.e("did",did);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ready_val = spinner_ready.getSelectedItem().toString();
                String status_val = spinner_status.getSelectedItem().toString();
                update_assign_doc(did,ready_val,status_val);


            }
        });




    }

    private void update_assign_doc(String did,String ready,String status){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Please Wait ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<EmployeUpdateAssignDocuments> call = RetrofitClient.getmInstance().getApi().update_employee_assign_docs(did,ready,status);

        call.enqueue(new Callback<EmployeUpdateAssignDocuments>() {
            @Override
            public void onResponse(Call<EmployeUpdateAssignDocuments> call, Response<EmployeUpdateAssignDocuments> response) {
                progressDialog.dismiss();
                EmployeUpdateAssignDocuments employeUpdateAssignDocuments = response.body();

                if (employeUpdateAssignDocuments != null){
                    if (employeUpdateAssignDocuments.getStatus()){
                        Toast("updated successfully");
                        ShowSuccessAlert();
                    }else {
                        Toast("Not updated");
                    }
                }
            }

            @Override
            public void onFailure(Call<EmployeUpdateAssignDocuments> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }


    private void Toast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShowSuccessAlert(){

        new android.app.AlertDialog.Builder(this)
                .setMessage("Updated Successfully")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();

    }
}
