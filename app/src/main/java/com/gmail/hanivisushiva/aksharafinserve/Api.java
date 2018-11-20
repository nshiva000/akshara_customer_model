package com.gmail.hanivisushiva.aksharafinserve;


import com.gmail.hanivisushiva.aksharafinserve.Models.FinishedDoc.FinishedDoc;
import com.gmail.hanivisushiva.aksharafinserve.Models.Login.Login;
import com.gmail.hanivisushiva.aksharafinserve.Models.YourDoc.YourDoc;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface Api {

    @FormUrlEncoded
    @POST("login.php")
    Call<Login> user_login(
            @Field("txt_email") String username,
            @Field("txt_password") String password
    );


    @Multipart
    @POST("uploaddocs.php")
    Call<ResponseBody> uploadMultiple(
            @Part("description") RequestBody description,
            @Part("rid") RequestBody rid,
            @Part("size") RequestBody size,
            @Part List<MultipartBody.Part> files);


    @FormUrlEncoded
    @POST("yourdocs.php")
    Call<YourDoc> yourDocuments(
            @Field("rid") String rid
    );

    @FormUrlEncoded
    @POST("enquiry.php")
    Call<ResponseBody> enquiry(
            @Field("rid") String rid,
            @Field("txtName") String email,
            @Field("txtEmail") String name,
            @Field("txtPhone") String phone,
            @Field("txtRemarks") String remarks
    );

    @FormUrlEncoded
    @POST("finisheddocuments.php")
    Call<FinishedDoc> finshed_doc(
            @Field("rid") String rid
    );







    @GET
    Call<ResponseBody> downloadFileWithDynamicUrl(@Url String fileUrl);



   /*
    @FormUrlEncoded
    @POST("prelims")
    Call<ExamList> exam_list(
            @Field("sid") String sid
    );

    yourdocs.php
    enquiry.php finisheddocuments.php


    @GET("prelims/{qid}")
    Call<List<QuestionsList>> get_all_questions(@Path("qid") String qid);



    @FormUrlEncoded
    @POST("submittest")
    Call<Submit> submit_answers(
            @Field("sid") String sid,
            @Field("tid") String tid,
            @Field("answers") String ans
    );

    @FormUrlEncoded
    @POST("editprofile")
    Call<Edit_response> edit_profile(
            @Field("sid") String sid,
            @Field("email") String email
    );

   /* @GET("exam_process.php")
    Call<List<TestList>> getExamList();


    @GET("exam_list.php")
    Call<List<Question>> getQuetions(@Query("exam_id") String id);

    */



}
