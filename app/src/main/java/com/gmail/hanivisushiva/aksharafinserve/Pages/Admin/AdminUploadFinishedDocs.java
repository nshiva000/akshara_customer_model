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
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.FileUtils;
import com.gmail.hanivisushiva.aksharafinserve.InternetConnection;
import com.gmail.hanivisushiva.aksharafinserve.Models.AddNewUserModel.AddNewUserModel;
import com.gmail.hanivisushiva.aksharafinserve.Models.Services.Services;
import com.gmail.hanivisushiva.aksharafinserve.Models.UserDataModel.Datum;
import com.gmail.hanivisushiva.aksharafinserve.Models.UserDataModel.UserDataModel;
import com.gmail.hanivisushiva.aksharafinserve.MyAdapter;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;
import com.gmail.hanivisushiva.aksharafinserve.Welcome;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUploadFinishedDocs extends AppCompatActivity {

    private static final String TAG = "upload_tag";
    private static final int REQUEST_CODE = 6384;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 124;

    private View parentView;
    private ListView listView;
    private ProgressBar mProgressBar;
    private Button btnChoose, btnUpload,clear_btn;
    private EditText upload_des;



    TextView mItemSelected;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    HashMap<String,String> stringDatumHashMap = new HashMap<>();
    ArrayList<String> items = new ArrayList<>();
    Button mOrder;
    String services_txt;


    HashMap<String,Datum> user_data_map = new HashMap<>();
    ArrayList<String> users_names = new ArrayList<>();
    Spinner spinner_user;





    private String rid;

    private ArrayList<Uri> arrayList = new ArrayList<>();
    MyAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload_finished_docs);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        parentView = findViewById(R.id.parent_layout);
        listView = findViewById(R.id.listView);
        mProgressBar = findViewById(R.id.progressBar);
        upload_des = findViewById(R.id.upload_des);
        clear_btn = findViewById(R.id.clear_files);


        get_Users();
        get_services();


        spinner_user = findViewById(R.id.user_spin);

        rid = SharedPrefManager.get_mInstance(getApplicationContext()).getRid();

        btnChoose = findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Display the file chooser dialog
                if (askForPermission())
                    showChooser();
            }
        });

        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String des_upload = upload_des.getText().toString();
                String spin_txt = spinner_user.getSelectedItemPosition()+"";
                Log.e("jdsb",spin_txt+"---"+user_data_map.get(spin_txt).getRid());


                if (TextUtils.isEmpty(des_upload)) {
                    upload_des.setError("Please enter username");
                    upload_des.requestFocus();
                    return;
                }


                if (TextUtils.isEmpty(spin_txt)) {
                    Toast.makeText(getApplicationContext(),"Select the user",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(services_txt)) {
                    Toast.makeText(getApplicationContext(),"Select the Services",Toast.LENGTH_SHORT).show();
                    return;
                }




                if (arrayList.size() == 0){
                    Toast.makeText(getApplicationContext(),"please files To upload",Toast.LENGTH_SHORT).show();
                }else {
                    uploadImagesToServer(des_upload,rid,user_data_map.get(spin_txt).getRid(),services_txt);

                    Log.e("sbv",des_upload+"---"+rid+"---"+"---"+user_data_map.get(spin_txt).getRid()+"---"+services_txt);
                }

            }
        });



        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                MyAdapter mAdapter = new MyAdapter(AdminUploadFinishedDocs.this, arrayList);
                listView.setAdapter(mAdapter);

            }
        });
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
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(AdminUploadFinishedDocs.this);
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




    private void get_Users(){

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading,Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<UserDataModel> userDataModelCall = RetrofitClient.getmInstance().getApi().get_user_data();

        userDataModelCall.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                UserDataModel userDataModel = response.body();

                if (userDataModel != null){
                    if (userDataModel.getStatus()){
                        for (int i = 0;i<userDataModel.getData().size();i++){
                            users_names.add(userDataModel.getData().get(i).getName());
                            user_data_map.put(i+"",userDataModel.getData().get(i));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminUploadFinishedDocs.this,android.R.layout.simple_dropdown_item_1line,users_names);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_user.setAdapter(adapter);
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
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
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if(data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        int currentItem = 0;
                        while(currentItem < count) {
                            Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
                            //do something with the image (save it to some directory or whatever you need to do with it here)
                            currentItem = currentItem + 1;
                            Log.d("Uri Selected", imageUri.toString());
                            try {
                                // Get the file path from the URI
                                String path = FileUtils.getPath(this, imageUri);
                                Log.d("Multiple File Selected", path);

                                arrayList.add(imageUri);
                                MyAdapter mAdapter = new MyAdapter(AdminUploadFinishedDocs.this, arrayList);
                                listView.setAdapter(mAdapter);

                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }
                        }
                    } else if(data.getData() != null) {
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            Log.d("Single File Selected", path);

                            arrayList.add(uri);
                            mAdapter = new MyAdapter(AdminUploadFinishedDocs.this, arrayList);
                            listView.setAdapter(mAdapter);

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadImagesToServer(String des,String rid,String user_txt,String services_txt) {
        if (InternetConnection.checkConnection(AdminUploadFinishedDocs.this)) {
            showProgress();

            // create list of file parts (photo, video, ...)
            List<MultipartBody.Part> parts = new ArrayList<>();

            if (arrayList != null) {
                // create part for file (photo, video, ...)
                for (int i = 0; i < arrayList.size(); i++) {
                    parts.add(prepareFilePart("image"+i, arrayList.get(i)));
                }
            }



            RequestBody description = createPartFromString(des);
            RequestBody user_rid = createPartFromString(rid);
            RequestBody size = createPartFromString(""+parts.size());
            RequestBody user = createPartFromString(user_txt);
            RequestBody services = createPartFromString(services_txt);



            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("uploading.. Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();


            // finally, execute the request
            Call<AddNewUserModel> call = RetrofitClient.getmInstance().getApi().admin_upload_finish_documents(description,user_rid, size,user,services,parts);

            call.enqueue(new Callback<AddNewUserModel>() {
                @Override
                public void onResponse(@NonNull Call<AddNewUserModel> call, @NonNull Response<AddNewUserModel> response) {
                    progressDialog.dismiss();
                    hideProgress();
                    Log.e("response",response.toString()+"dn cnzd vk");

                    AddNewUserModel addNewUserModel = response.body();


                    if (addNewUserModel != null){

                        Log.e("status_upload",addNewUserModel.getStatus()+"");

                        if (addNewUserModel.getStatus()){
                            ShowSuccessAlert();



                            arrayList.clear();
                            MyAdapter mAdapter = new MyAdapter(AdminUploadFinishedDocs.this, arrayList);
                            listView.setAdapter(mAdapter);


                        }else {
                            ShowErrorAlert();
                            Snackbar.make(parentView, addNewUserModel.getMessage(), Snackbar.LENGTH_LONG).show();

                            Log.e("status_upload",addNewUserModel.getMessage()+"message");
                        }

                    }else {

                        ShowErrorAlert();
                        Toast.makeText(AdminUploadFinishedDocs.this,
                                "Something Wrong! please try again", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<AddNewUserModel> call, @NonNull Throwable t) {
                    ShowErrorAlert();
                    progressDialog.dismiss();
                    hideProgress();
                    Snackbar.make(parentView, t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            hideProgress();
            Toast.makeText(AdminUploadFinishedDocs.this,
                    R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    private void ShowSuccessAlert(){

        new android.app.AlertDialog.Builder(this)
                .setMessage("Uploaded Successfully")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();

    }



    private void ShowErrorAlert(){

        new android.app.AlertDialog.Builder(this)
                .setMessage("Failed to Upload")
                .setCancelable(true)
                .setPositiveButton("Ok", null)
                .show();

    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        btnChoose.setEnabled(false);
        btnUpload.setEnabled(false);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        btnChoose.setEnabled(true);
        btnUpload.setEnabled(true);
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
            int hasCallPermission = ContextCompat.checkSelfPermission(AdminUploadFinishedDocs.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                // Ask for permission
                // need to request permission
                if (ActivityCompat.shouldShowRequestPermissionRationale(AdminUploadFinishedDocs.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // explain
                    showMessageOKCancel(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(AdminUploadFinishedDocs.this,
                                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    // if denied then working here
                } else {
                    // Request for permission
                    ActivityCompat.requestPermissions(AdminUploadFinishedDocs.this,
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
                    Toast.makeText(AdminUploadFinishedDocs.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminUploadFinishedDocs.this);
        final AlertDialog dialog = builder.setMessage("You need to grant access to Read External Storage")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(AdminUploadFinishedDocs.this, android.R.color.holo_blue_light));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(AdminUploadFinishedDocs.this, android.R.color.holo_red_light));
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
