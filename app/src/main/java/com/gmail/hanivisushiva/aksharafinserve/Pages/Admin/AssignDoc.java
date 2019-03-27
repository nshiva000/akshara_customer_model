package com.gmail.hanivisushiva.aksharafinserve.Pages.Admin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.DatePickerFragment;
import com.gmail.hanivisushiva.aksharafinserve.Models.AStatus.AStatus;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminUpdateDocumentManager.AdminUpdateDocumentManager;
import com.gmail.hanivisushiva.aksharafinserve.Models.AllEmploye.AllEmploye;
import com.gmail.hanivisushiva.aksharafinserve.Models.Assign.Assign;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Welcome;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignDoc extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String des_txt,date_txt,did_txt,path_txt,employee_id,status_get,remarks_txt,string_date;
    ArrayList<String> status = new ArrayList<>();
    ArrayList<String> employees = new ArrayList<>();
    HashMap<String,String> emp_id = new HashMap<>();

    Spinner emp_spin,status_spin;
    TextView remarks,select_date_txt;
    Button send,date;
    String curent_date = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_doc);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        emp_spin = findViewById(R.id.spin_employee);
        status_spin = findViewById(R.id.spin_status);
        send = findViewById(R.id.send);
        date = findViewById(R.id.date);
        select_date_txt = findViewById(R.id.date_txt);

        remarks = findViewById(R.id.remarks);

        get_status();
        get_employee();

        Intent intent = getIntent();
        did_txt = intent.getStringExtra("did");
        des_txt = intent.getStringExtra("des");
        date_txt = intent.getStringExtra("date");
        path_txt = intent.getStringExtra("path");



        String urlStr = path_txt;

//        String fileName = urlStr.substring(urlStr.lastIndexOf('/')+1, urlStr.length());
//        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
//        String fileExtension = urlStr.substring(urlStr.lastIndexOf("."));

//        Log.i("File Name", fileName);
//        Log.i("File Name ex", fileNameWithoutExtension);
//        Log.i("File Extension", fileExtension);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(),"date picker");

            }
        });









        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(status_spin.getSelectedItemPosition() == 0){
                    TextView errorText = (TextView)status_spin.getSelectedView();
                    errorText.setError("Select Status");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                }else if (emp_spin.getSelectedItemPosition() == 0){

                    TextView errorText = (TextView)emp_spin.getSelectedView();
                    errorText.setError("Select Employee");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select Employee");
                }else {

                    remarks_txt = remarks.getText().toString().trim();
                    if (TextUtils.isEmpty(remarks_txt)) {
                        remarks.setError("Please enter Remarks");
                        remarks.requestFocus();
                        return;
                    }

                    employee_id = emp_id.get(emp_spin.getSelectedItem().toString());
                    status_get = status_spin.getSelectedItem().toString();

                    Log.e("values",remarks_txt+employee_id+status_get+did_txt);



                    if (curent_date.isEmpty()){
                        Toast("Select the date");
                        return;
                    }


                    assign(did_txt,remarks_txt,status_get,employee_id,curent_date);
                }





            }
        });


    }




    private void get_status(){

        Call<AStatus> aStatusCall = RetrofitClient.getmInstance().getApi().get_status();
        aStatusCall.enqueue(new Callback<AStatus>() {
            @Override
            public void onResponse(Call<AStatus> call, Response<AStatus> response) {
                AStatus aStatus = response.body();

                if (aStatus.getStatus()){
                    status.add("Select Status");
                    status.addAll(aStatus.getData());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AssignDoc.this,android.R.layout.simple_dropdown_item_1line,status);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    status_spin.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<AStatus> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);



        String date = month + "/" + dayOfMonth + "/" + year;

        curent_date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());
        select_date_txt.setText(curent_date);

    }

    private void get_employee(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading, Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<AllEmploye> allEmployeCall = RetrofitClient.getmInstance().getApi().get_all_employees();
        allEmployeCall.enqueue(new Callback<AllEmploye>() {
            @Override
            public void onResponse(Call<AllEmploye> call, Response<AllEmploye> response) {
                progressDialog.dismiss();
                AllEmploye allEmploye = response.body();

                if (allEmploye.getStatus()){
                    employees.add("Select Employee");
                    for (int i = 0; i<allEmploye.getData().size();i++){
                        emp_id.put(allEmploye.getData().get(i).getName(),allEmploye.getData().get(i).getRid());
                        employees.add(allEmploye.getData().get(i).getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AssignDoc.this,android.R.layout.simple_dropdown_item_1line,employees);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    emp_spin.setAdapter(adapter);

                }



            }

            @Override
            public void onFailure(Call<AllEmploye> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }


    private void assign(String s_did,String s_remarks,String s_status,String s_assigned_to,String date){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading, Please Wait");
        progressDialog.show();
        Call<AdminUpdateDocumentManager> assignCall = RetrofitClient.getmInstance().getApi().update_complete_document_manager(s_did,s_remarks,s_status,s_assigned_to,date);

        assignCall.enqueue(new Callback<AdminUpdateDocumentManager>() {
            @Override
            public void onResponse(Call<AdminUpdateDocumentManager> call, Response<AdminUpdateDocumentManager> response) {
                progressDialog.dismiss();
                AdminUpdateDocumentManager assign = response.body();
                if (assign.getStatus()){
                   Log.e("response",assign.getMessage().toString());
                    Toast.makeText(getApplicationContext(),"Assigned Successfully",Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(AssignDoc.this,Welcome.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Failed to Assign",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<AdminUpdateDocumentManager> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed to Assign",Toast.LENGTH_SHORT).show();
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
