package com.gmail.hanivisushiva.aksharafinserve;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //https://aksharafinserv.com/weboffice/api/


    //http://glowindiablowindia.com/weboffice/api/

    public static final String BASE_URL = "http://aksharafinserv.com/weboffice/api/";
    private static  RetrofitClient mInstance;
    private Retrofit retrofit;


    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(160, TimeUnit.SECONDS)
            .readTimeout(160, TimeUnit.SECONDS)
            .writeTimeout(160, TimeUnit.SECONDS)
            .build();



          //httpClient.connectionSpecs(Collections.singletonList(spec))

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static synchronized RetrofitClient getmInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;

    }

    public Api getApi(){
        return retrofit.create(Api.class);
    }
}
