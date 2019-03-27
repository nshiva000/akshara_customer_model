package com.gmail.hanivisushiva.aksharafinserve.Pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;

public class Profile extends AppCompatActivity {


    String role,image,name,email,contact_person,mobile;

    TextView role_e,image_e,name_e,email_e,contact_e,mobile_e;
    CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        cardView = findViewById(R.id.contact_person_cardView);

        role_e = findViewById(R.id.batch);
        mobile_e = findViewById(R.id.mobile);
        name_e = findViewById(R.id.user_name);
        email_e = findViewById(R.id.email);
        contact_e = findViewById(R.id.admission);



        name = SharedPrefManager.get_mInstance(getApplicationContext()).getName();
        email = SharedPrefManager.get_mInstance(getApplicationContext()).getEmail();
        role = SharedPrefManager.get_mInstance(getApplicationContext()).getRole();
        image = SharedPrefManager.get_mInstance(getApplicationContext()).getImage();
        mobile = SharedPrefManager.get_mInstance(getApplicationContext()).getMobile();
        contact_person = SharedPrefManager.get_mInstance(getApplicationContext()).getContactPerson();


        if (role.equals("employee")){
            cardView.setVisibility(View.GONE);
        }


        name_e.setText(name);
        mobile_e.setText(mobile);
        email_e.setText(email);
        contact_e.setText(contact_person);
        role_e.setText(role);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
