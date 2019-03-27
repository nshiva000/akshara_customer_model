package com.gmail.hanivisushiva.aksharafinserve;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.Models.Login.Login;
import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText email,password;
    String email_txt,password_txt;
    Button login_btn;

    TextInputLayout email_layout,password_layout;



    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.get_mInstance(getApplicationContext()).isLoggedIn()){
            Intent intent = new Intent(MainActivity.this,Welcome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





//
//        FirebaseMessaging.getInstance().subscribeToTopic("weather")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        //String msg = getString(R.string.msg_subscribed);
//                        if (!task.isSuccessful()) {
//                            // msg = getString(R.string.msg_subscribe_failed);
//                        }
//                        //Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, "jsfbhv", Toast.LENGTH_SHORT).show();
//                    }
//                });


        if (!haveNetworkConnection()){

            new AlertDialog.Builder(this)
                    .setMessage("No Internet, Want to exit app")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }





        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);



        login_btn = findViewById(R.id.login_btn);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_txt = email.getText().toString().trim();
                password_txt = password.getText().toString().trim();

                if (TextUtils.isEmpty(email_txt)) {
                    email_layout.setError("Email Required");
                    email_layout.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_txt).matches()) {
                    email_layout.setError("Enter a valid Email");
                    email_layout.requestFocus();
                    return;
                }

                email_layout.setErrorEnabled(true);
                email_layout.setError(null);


                if (TextUtils.isEmpty(password_txt)) {
                    password_layout.setError("Password Required");
                    password_layout.requestFocus();
                    return;
                }


                password_layout.setErrorEnabled(true);
                password_layout.setError(null);

                userLogin(email_txt,password_txt);


            }
        });



    }


    private void userLogin(String email,String password){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading, Please Wait");
        progressDialog.show();


        Call<Login> loginCall = RetrofitClient.getmInstance().getApi().user_login(email,password);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login login = response.body();

                if (login != null){
                    if (login.getStatus()){
                        SharedPrefManager.get_mInstance(getApplicationContext()).saveUser(login.getData());

                        Intent intent = new Intent(MainActivity.this,Welcome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    }else {
                        ToastMsg("Email or Password does not match");
                        email_layout.setError("Email or Password does not match");
                        email_layout.requestFocus();

                        password_layout.setError("");
                        password_layout.requestFocus();
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.e("error_login",t.toString());
                progressDialog.dismiss();
                ToastMsg("server not Responding"+t.getLocalizedMessage());
            }
        });





    }

    private void ToastMsg(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }



    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
