package com.gmail.hanivisushiva.aksharafinserve.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.gmail.hanivisushiva.aksharafinserve.Models.Login.Data;



public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_name";

    private static SharedPrefManager mInstance;
    private Context mCtx;


    private SharedPrefManager(Context mCtx){
        this.mCtx = mCtx;
    }

    public static  synchronized  SharedPrefManager get_mInstance(Context mCtx){
        if (mInstance == null){
            mInstance = new SharedPrefManager(mCtx);

        }
        return mInstance;
    }


    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString("token", null);
    }

    public void saveUser(Data loginData){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putString("rid",loginData.getRid());
        editor.putString("services",loginData.getSerivces());
        editor.putString("others",loginData.getOthers());
        editor.putString("image",loginData.getImage());
        editor.putString("client_code",loginData.getClientCode());

        editor.putString("name",loginData.getName());
        editor.putString("password",loginData.getPassword());
        editor.putString("email",loginData.getEmail());
        editor.putString("contact_person",loginData.getContactPerson());
        editor.putString("phone",loginData.getPhone());

        editor.putString("message",loginData.getMessage());
        editor.putString("role",loginData.getRole());
        editor.putString("status",loginData.getStatus());
        editor.putString("date",loginData.getDate());
        editor.putString("service_agreement",loginData.getServiceAgreement());

        editor.putString("mopt",loginData.getMotp());
        editor.putString("motpver",loginData.getMotpver());
        editor.putString("eotp",loginData.getEotp());
        editor.putString("eotpver",loginData.getEotpver());
        editor.putBoolean("login_status",true);


        editor.apply();
        editor.commit();

    }



    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("login_status", false);

    }

    public String getRid(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("rid","");

    }


    public String getEmail(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("email","");

    }


    public String getRole(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("role","");

    }

    public String getImage(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("image","");

    }

    public String getName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("name","");

    }

    public String getMobile(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone","");

    }

    public String getContactPerson(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("contact_person","");

    }




  /*  public Login getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
          return new Login(
                  sharedPreferences.getString("email",null),
                  sharedPreferences.getString("password",null),
                  sharedPreferences.getString("uid",null),
                  sharedPreferences.getBoolean("status",false)

          );
    }

    */

    public void clear(){
        String store_token = getDeviceToken();
        String name = getName();
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        saveDeviceToken(store_token);
        Log.e("name",name);
        Log.e("name",store_token);
    }
}
