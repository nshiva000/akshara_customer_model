package com.gmail.hanivisushiva.aksharafinserve.Pages.Admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.FileUtils;
import com.gmail.hanivisushiva.aksharafinserve.MainActivity;
import com.gmail.hanivisushiva.aksharafinserve.Models.AddNewUserModel.AddNewUserModel;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewEmployee extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 6384;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 124;

    private MultipartBody.Part multi_part_file;
    private Uri imageUri;
    private ImageView imageView;


    Button choose,submit;
    EditText name,email,password,phone;
    TextInputLayout name_layout,email_layout,password_layout,phone_layout;
    String name_txt,email_txt,password_txt,phone_txt;

    RequestBody name_r,email_r,password_r,phone_r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_employee);


        imageUri = null;

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add New Employee");
        }


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);


        name_layout = findViewById(R.id.name_layout);
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);
        phone_layout = findViewById(R.id.phone_layout);



        choose = findViewById(R.id.choose);
        submit = findViewById(R.id.submit);
        imageView = findViewById(R.id.imageView);



        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (askForPermission())
                    showChooser();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                name_txt = name.getText().toString().trim();
                email_txt = email.getText().toString().trim();
                password_txt = password.getText().toString().trim();
                phone_txt = phone.getText().toString().trim();




                if (TextUtils.isEmpty(name_txt)) {
                    name_layout.setError("Name is required");
                    name_layout.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password_txt)) {
                    password_layout.setError("Password is Required");
                    password_layout.requestFocus();
                    return;
                }


                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_txt).matches()) {
                    email_layout.setError("Enter a valid email");
                    email_layout.requestFocus();
                    return;
                }


                if (TextUtils.isEmpty(email_txt)) {
                    email_layout.setError("Email is required");
                    email_layout.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(phone_txt)) {
                    phone_layout.setError("Phone no.is required");
                    phone_layout.requestFocus();
                    return;
                }

                if (imageUri == null) {
                    Toast.makeText(getApplicationContext(),"Please select Profile",Toast.LENGTH_SHORT).show();
                    return;
                }


                Log.e("submit",name_txt+email_txt+password_txt+phone_txt);

                Send();

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void Send(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();




        multi_part_file = prepareFilePart("image",imageUri);
        name_r = createPartFromString(name_txt);
        password_r = createPartFromString(password_txt);
        email_r = createPartFromString(email_txt);
        phone_r = createPartFromString(phone_txt);

        Call<AddNewUserModel> addNewUserModelCall = RetrofitClient.getmInstance().getApi().add_new_employee(name_r,password_r,email_r,phone_r,multi_part_file);

        addNewUserModelCall.enqueue(new Callback<AddNewUserModel>() {
            @Override
            public void onResponse(Call<AddNewUserModel> call, Response<AddNewUserModel> response) {
                progressDialog.dismiss();

                AddNewUserModel addNewUserModel = response.body();

                if (addNewUserModel != null){
                    if (addNewUserModel.getStatus()){
                        showAlertSuccess();
                        Toast.makeText(getApplicationContext(),"uploaded Success",Toast.LENGTH_SHORT).show();
                    }else {
                        ShowAlertError();
                        Toast.makeText(getApplicationContext(),"uploaded failed",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    ShowAlertError();
                    Toast.makeText(getApplicationContext(),"Internal error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddNewUserModel> call, Throwable t) {
                progressDialog.dismiss();
                ShowAlertError();
                Log.e("submit",t.getMessage());
            }
        });


    }


    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (data != null) {
                    if (data.getData() != null) {
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        final Uri uri = data.getData();
                        imageUri = uri;

                        Picasso.get().load(imageUri).into(imageView);
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            Log.d("Single File Selected", path);

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void showAlertSuccess(){
        new android.app.AlertDialog.Builder(this)
                .setMessage("User Added Successfully")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void ShowAlertError(){
        new android.app.AlertDialog.Builder(this)
                .setMessage("Something Went wrong, please try again later")
                .setCancelable(true)
                .setPositiveButton("Ok",null)
                .show();
    }



    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(Objects.requireNonNull(getContentResolver().getType(fileUri))),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    /**
     *  Runtime Permission
     */
    private boolean askForPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            int hasCallPermission = ContextCompat.checkSelfPermission(AddNewEmployee.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                // Ask for permission
                // need to request permission
                if (ActivityCompat.shouldShowRequestPermissionRationale(AddNewEmployee.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // explain
                    showMessageOKCancel(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(AddNewEmployee.this,
                                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    // if denied then working here
                } else {
                    // Request for permission
                    ActivityCompat.requestPermissions(AddNewEmployee.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }

                return false;
            } else {
                // permission granted and calling function working
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    showChooser();
                } else {
                    // Permission Denied
                    Toast.makeText(AddNewEmployee.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewEmployee.this);
        final AlertDialog dialog = builder.setMessage("You need to grant access to Read External Storage")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(AddNewEmployee.this, android.R.color.holo_blue_light));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(AddNewEmployee.this, android.R.color.holo_red_light));
            }
        });

        dialog.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
