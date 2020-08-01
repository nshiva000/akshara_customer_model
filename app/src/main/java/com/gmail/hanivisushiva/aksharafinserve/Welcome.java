package com.gmail.hanivisushiva.aksharafinserve;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.Models.SendTokenModel.SendTokenModel;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.AddNewEmployee;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.AddNewUser;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.AdminCompletdDocumnetsActivity;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.AdminEnquiryActivity;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.AdminLatestUpdatesActivity;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.AdminPhoneMsgActivity;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.AdminSendUserMsgActivity;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.AdminUploadFinishedDocs;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Customer.CompletedDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.AdminDocumentsManager;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.EmployeDataActivity;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Employee.EmployeAssignDocActivity;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Employee.EmployeCompletedActivity;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Employee.EmployeMsgUserActivity;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Employee.EmployeUploadFinishDocActivity;
import com.gmail.hanivisushiva.aksharafinserve.Pages.EmployeeAssignDoc;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Customer.Enquiry;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Profile;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Customer.Upload;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.UserDataActivity;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Customer.YourDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Welcome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView u_name,u_email;
    String role;
    CircleImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);





        if (SharedPrefManager.get_mInstance(getApplicationContext()).getDeviceToken() != null){
            sendTokenToServer(SharedPrefManager.get_mInstance(getApplicationContext()).getRid(),SharedPrefManager.get_mInstance(getApplicationContext()).getDeviceToken());
            Log.e("main",SharedPrefManager.get_mInstance(getApplicationContext()).getDeviceToken()+"not equal to null ---"+SharedPrefManager.get_mInstance(getApplicationContext()).getRid());

        }else {
            Log.e("main","token not registered");
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        role = SharedPrefManager.get_mInstance(getApplicationContext()).getRole();

        Log.e("role",role);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);employee*/


        if (role.equals("admin")){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.admin_menu);
        }

        if (role.equals("employee")){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.employee_menu);
        }



        u_name = header.findViewById(R.id.u_name);
        u_email = header.findViewById(R.id.u_email);
        imageView = header.findViewById(R.id.imageView);

        //imageView

        u_name.setText(SharedPrefManager.get_mInstance(getApplicationContext()).getName());
        u_email.setText(SharedPrefManager.get_mInstance(getApplicationContext()).getEmail());

        if (SharedPrefManager.get_mInstance(getApplicationContext()).getImage() != null){
            if (SharedPrefManager.get_mInstance(getApplicationContext()).getImage().isEmpty()){
                Picasso.get().load(R.drawable.logo).into(imageView);
            }else{
                Picasso.get().load(SharedPrefManager.get_mInstance(getApplicationContext()).getImage()).into(imageView);
            }
        }else {
            Picasso.get().load(R.drawable.logo).into(imageView);
        }


        Log.e("rid",SharedPrefManager.get_mInstance(getApplicationContext()).getRid()+"--");


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }


    private void sendTokenToServer(String rid,String token){
        Call<SendTokenModel> sendTokenModelCall = RetrofitClient.getmInstance().getApi().send_token_to_server(rid,token);
        sendTokenModelCall.enqueue(new Callback<SendTokenModel>() {
            @Override
            public void onResponse(Call<SendTokenModel> call, Response<SendTokenModel> response) {

                SendTokenModel sendTokenModel = response.body();

                if (sendTokenModel != null){
                    if (sendTokenModel.getStatus()){
                        Log.e("Token","Token updated Successfully");
                    }else {
                        Log.e("Token","Token not updated");
                    }
                }
            }

            @Override
            public void onFailure(Call<SendTokenModel> call, Throwable t) {
                Log.e("Token","Token not updated");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }


    private void log_out_user(String rid){
        Call<SendTokenModel> call = RetrofitClient.getmInstance().getApi().log_out(rid);

        call.enqueue(new Callback<SendTokenModel>() {
            @Override
            public void onResponse(Call<SendTokenModel> call, Response<SendTokenModel> response) {
                SendTokenModel sendTokenModel = response.body();
                if (sendTokenModel != null){
                    if (sendTokenModel.getStatus()){
                        toast_msg("Logout successfull");
                        SharedPrefManager.get_mInstance(getApplicationContext()).clear();
                        Intent intent = new Intent(Welcome.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else {
                        toast_msg("Somthing went wrong,try agin later");
                    }
                }
            }

            @Override
            public void onFailure(Call<SendTokenModel> call, Throwable t) {
                SharedPrefManager.get_mInstance(getApplicationContext()).clear();
                Intent intent = new Intent(Welcome.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            log_out_user(SharedPrefManager.get_mInstance(getApplicationContext()).getRid());
            return true;
        }

        if(id == R.id.privacy_policy){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://aksharafinserv.com/privacy_policy/privacy_policy.html"));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(browserIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            Intent intent = new Intent(Welcome.this,Profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if (id == R.id.nav_custom_doc) {

            Intent intent = new Intent(Welcome.this,AdminDocumentsManager.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (id == R.id.nav_upload) {


            Intent intent = new Intent(Welcome.this,Upload.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_your_doc) {
            Intent intent = new Intent(Welcome.this,YourDocuments.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_completed_doc) {

            Intent intent = new Intent(Welcome.this,CompletedDocuments.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_enquiry) {

            Intent intent = new Intent(Welcome.this,Enquiry.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if (id == R.id.nav_add_new_user) {

            Intent intent = new Intent(Welcome.this,AddNewUser.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        else if (id == R.id.nav_add_new_employee) {

            Intent intent = new Intent(Welcome.this,AddNewEmployee.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        else if (id == R.id.nav_send) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogLayout = inflater.inflate(R.layout.activity_help, null);
            builder.setView(dialogLayout);

            final AlertDialog alertDialog = builder.show();



        }else if(id == R.id.nav_assign_doc){

            Intent intent = new Intent(Welcome.this,EmployeeAssignDoc.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if(id == R.id.nav_admin_upload_finish_documents){

            Intent intent = new Intent(Welcome.this,AdminUploadFinishedDocs.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if(id == R.id.nav_admin_user_data){

            Intent intent = new Intent(Welcome.this,UserDataActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if(id == R.id.nav_admin_employee_data){

            Intent intent = new Intent(Welcome.this,EmployeDataActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (id == R.id.nav_admin_msg_user){
            Intent intent = new Intent(Welcome.this,AdminSendUserMsgActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if (id == R.id.nav_admin_phone_msg){
            Intent intent = new Intent(Welcome.this,AdminPhoneMsgActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if (id == R.id.nav_admin_completed_documents){
            Intent intent = new Intent(Welcome.this,AdminCompletdDocumnetsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        else if (id == R.id.nav_admin_enquiry){
            Intent intent = new Intent(Welcome.this,AdminEnquiryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        else if (id == R.id.nav_latest_updates){
            Intent intent = new Intent(Welcome.this,AdminLatestUpdatesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

//employee   links
        else if (id == R.id.nav_employee_assigned_doc){
            Intent intent = new Intent(Welcome.this,EmployeAssignDocActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        else if (id == R.id.nav_employee_upload){
            Intent intent = new Intent(Welcome.this,EmployeUploadFinishDocActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        else if (id == R.id.nav_employee_completed_doc){
            Intent intent = new Intent(Welcome.this,EmployeCompletedActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        else if (id == R.id.nav_employee_email_msg){
            Intent intent = new Intent(Welcome.this,EmployeMsgUserActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void toast_msg(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }





   /* private void send_admin_phone_msg_user(){

        final ProgressDialog progressDialog=new ProgressDialog(Welcome.this);
        progressDialog.setMessage("Loading,Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();



        progressDialog.dismiss();

        Button submit;
        final AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.admin_message_user, null);
        submit =  dialogLayout.findViewById(R.id.submit);
        builder.setView(dialogLayout);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast_msg("submit clicked");
            }
        });

        final AlertDialog alertDialog = builder.show();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent intent = new Intent(getApplicationContext(),Welcome.class);
                startActivity(intent);
            }
        });
    }

    */
}
