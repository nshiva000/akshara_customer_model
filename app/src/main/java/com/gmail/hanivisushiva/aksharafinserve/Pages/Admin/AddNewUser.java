package com.gmail.hanivisushiva.aksharafinserve.Pages.Admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


import com.gmail.hanivisushiva.aksharafinserve.FileUtils;
import com.gmail.hanivisushiva.aksharafinserve.MainActivity;
import com.gmail.hanivisushiva.aksharafinserve.Models.AddNewUserModel.AddNewUserModel;
import com.gmail.hanivisushiva.aksharafinserve.Models.Services.Services;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.squareup.picasso.Picasso;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewUser extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 6384;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 124;


    private MultipartBody.Part multi_part_file;

    TextView mItemSelected;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    HashMap<String,String> stringDatumHashMap = new HashMap<>();
    Button btnChoose,mOrder,submit;
    ArrayList<String> items = new ArrayList<>();


    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    private Uri imageUri;
    ImageView imageView;

    EditText name,email,password,phone,contact_person,message,others;
    TextInputLayout name_layout,email_layout,password_layout,phone_layout,contact_person_layout,message_layout,others_layout;

    String name_txt,email_txt,password_txt,phone_txt,contact_person_txt,message_txt,agreement_txt,services_txt,others_txt;



    RequestBody others_r,name_r,email_r,password_r,phone_r,person_r,message_r,agreement_r,services_r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);
        imageUri = null;

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add New User");

        }

        imageView = findViewById(R.id.imageView);
        name = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        contact_person = findViewById(R.id.contact_person);
        message = findViewById(R.id.message);
        others = findViewById(R.id.others);



        name_layout = findViewById(R.id.user_name_layout);
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);
        phone_layout = findViewById(R.id.phone_layout);
        contact_person_layout = findViewById(R.id.contact_person_layout);
        message_layout = findViewById(R.id.message_layout);
        others_layout = findViewById(R.id.others_layout);




        radioSexGroup = findViewById(R.id.radio);

        submit = findViewById(R.id.submit);
        btnChoose = findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Display the file chooser dialog
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
                contact_person_txt = contact_person.getText().toString().trim();
                message_txt = message.getText().toString().trim();
                others_txt = others.getText().toString().trim();




                // get selected radio button from radioGroup
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);
               // Log.e("submit",radioSexButton.getText()+"");
                agreement_txt = radioSexButton.getText().toString();



                if (TextUtils.isEmpty(name_txt)) {
                    name_layout.setError("Name Required");
                    name_layout.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password_txt)) {
                    password_layout.setError("Password Required");
                    password_layout.requestFocus();
                    return;
                }


                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_txt).matches()) {
                    email_layout.setError("Enter a valid Email");
                    email_layout.requestFocus();
                    return;
                }


                if (TextUtils.isEmpty(email_txt)) {
                    email_layout.setError("User Name is Required");
                    email_layout.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(contact_person_txt)) {
                    contact_person_layout.setError("Contact Person is Required");
                    contact_person_layout.requestFocus();
                    return;
                }


                if (TextUtils.isEmpty(phone_txt)) {
                    phone_layout.setError("Phone No. is Required");
                    phone_layout.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(services_txt)) {
                    Toast.makeText(getApplicationContext(),"Select Services",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(agreement_txt)) {
                    Toast.makeText(getApplicationContext(),"Select radio button",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imageUri == null) {
                    Toast.makeText(getApplicationContext(),"Please select Image",Toast.LENGTH_SHORT).show();
                    return;
                }




                multi_part_file = prepareFilePart("image",imageUri);
                others_r = createPartFromString(others_txt);
                services_r = createPartFromString(services_txt);
                agreement_r = createPartFromString(agreement_txt);
                name_r = createPartFromString(name_txt);
                password_r = createPartFromString(password_txt);
                email_r = createPartFromString(email_txt);
                person_r = createPartFromString(contact_person_txt);
                phone_r = createPartFromString(phone_txt);
                message_r = createPartFromString(message_txt);




               send();


            }
        });

        get_services();
    }


    private void send(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading, Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.e("submit",name_txt+email_txt+password_txt+phone_txt+contact_person_txt+message_txt+agreement_txt+services_txt+others_txt+imageUri);



        Call<AddNewUserModel> responseBodyCall = RetrofitClient.getmInstance().getApi().add_new_user(services_r,others_r,agreement_r,name_r,password_r,email_r,person_r,phone_r,message_r,multi_part_file);
        responseBodyCall.enqueue(new Callback<AddNewUserModel>() {
            @Override
            public void onResponse(Call<AddNewUserModel> call, Response<AddNewUserModel> response) {
                progressDialog.dismiss();
                AddNewUserModel addNewUser = response.body();



                if (addNewUser != null) {
                    // Log.e("submit",addNewUser.getMessage());
                    if (addNewUser.getStatus()) {
                        showAlertSuccess();
                        Log.e("submit", addNewUser.getMessage());
                    }else {
                        ShowAlertError();
                        Toast.makeText(getApplicationContext(),addNewUser.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    ShowAlertError();
                    Toast.makeText(getApplicationContext(),"500 error, server problem",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddNewUserModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                ShowAlertError();

               Log.e("Error",t.getMessage());
            }
        });
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



    private void get_services(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please wait");
        progressDialog.show();
        Call<Services> servicesCall = RetrofitClient.getmInstance().getApi().get_admin_services();

        servicesCall.enqueue(new Callback<Services>() {
            @Override
            public void onResponse(Call<Services> call, Response<Services> response) {
                progressDialog.dismiss();


                Services services = response.body();

                if (services.getStatus()) {

                    for (int i = 0;i<services.getData().size();i++){
                        items.add(services.getData().get(i).getServices());
                        stringDatumHashMap.put(services.getData().get(i).getServices(),services.getData().get(i).getId());
                    }

                    mOrder = findViewById(R.id.btnOrder);
                    mItemSelected = (TextView) findViewById(R.id.tvItemSelected);

                    listItems = items.toArray(new String[0]);
                    checkedItems = new boolean[listItems.length];

                    mOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddNewUser.this);
                            mBuilder.setTitle(R.string.dialog_title);
                            mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            }
//                        } else if (mUserItems.contains(position)) {
//                            mUserItems.remove(position);
//                        }
                                    if (isChecked) {
                                        mUserItems.add(position);
                                    } else {
                                        mUserItems.remove((Integer.valueOf(position)));
                                    }
                                }
                            });

                            mBuilder.setCancelable(false);
                            mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    String item = "";
                                    for (int i = 0; i < mUserItems.size(); i++) {
                                        item = item + stringDatumHashMap.get(listItems[mUserItems.get(i)]);
                                        if (i != mUserItems.size() - 1) {
                                            item = item + ",";
                                        }
                                    }
                                    mItemSelected.setText(item);

                                    services_txt = item;
                                }
                            });

                            mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    for (int i = 0; i < checkedItems.length; i++) {
                                        checkedItems[i] = false;
                                        mUserItems.clear();
                                        mItemSelected.setText("");
                                    }
                                }
                            });

                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();
                        }
                    });


                }

            }

            @Override
            public void onFailure(Call<Services> call, Throwable t) {
                progressDialog.dismiss();

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
            int hasCallPermission = ContextCompat.checkSelfPermission(AddNewUser.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                // Ask for permission
                // need to request permission
                if (ActivityCompat.shouldShowRequestPermissionRationale(AddNewUser.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // explain
                    showMessageOKCancel(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(AddNewUser.this,
                                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    // if denied then working here
                } else {
                    // Request for permission
                    ActivityCompat.requestPermissions(AddNewUser.this,
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
                    Toast.makeText(AddNewUser.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewUser.this);
        final AlertDialog dialog = builder.setMessage("You need to grant access to Read External Storage")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(AddNewUser.this, android.R.color.holo_blue_light));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(AddNewUser.this, android.R.color.holo_red_light));
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