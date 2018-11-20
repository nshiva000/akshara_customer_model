package com.gmail.hanivisushiva.aksharafinserve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.Models.Login.Login;
import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText email,password;
    String email_txt,password_txt;
    Button login_btn;

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


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login_btn = findViewById(R.id.login_btn);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_txt = email.getText().toString().trim();
                password_txt = password.getText().toString().trim();


                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_txt).matches()) {
                    email.setError("Enter a valid email");
                    email.requestFocus();
                    return;
                }


                if (TextUtils.isEmpty(email_txt)) {
                    email.setError("Please enter username");
                    email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password_txt)) {
                    password.setError("Please enter Password");
                    password.requestFocus();
                    return;
                }

                ToastMsg(email_txt+"=="+password_txt);

                userLogin(email_txt,password_txt);


            }
        });



    }


    private void userLogin(String email,String password){


        Call<Login> loginCall = RetrofitClient.getmInstance().getApi().user_login(email,password);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login login = response.body();

                assert login != null;
                if (login.getStatus()){
                    SharedPrefManager.get_mInstance(getApplicationContext()).saveUser(login.getData());

                    Intent intent = new Intent(MainActivity.this,Welcome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);


                }else {
                    ToastMsg(login.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.e("error_login",t.getMessage());
            }
        });





    }

    private void ToastMsg(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
