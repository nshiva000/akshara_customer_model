package com.gmail.hanivisushiva.aksharafinserve;


import com.gmail.hanivisushiva.aksharafinserve.Models.AStatus.AStatus;
import com.gmail.hanivisushiva.aksharafinserve.Models.AddNewUserModel.AddNewUserModel;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminCompletedDocuments.AdminCompletedDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminDeleteCompletedDocuments.AdminDeleteCompletedDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminDeleteDocumentManager.AdminDeleteDocumentManager;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminEnquiries.AdminEnquiries;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminLatestUpdates.AdminLatestUpdates;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminPhoneMsg.AdminPhoneMsg;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminUpdateDocumentManager.AdminUpdateDocumentManager;
import com.gmail.hanivisushiva.aksharafinserve.Models.AllEmploye.AllEmploye;
import com.gmail.hanivisushiva.aksharafinserve.Models.Assign.Assign;
import com.gmail.hanivisushiva.aksharafinserve.Models.AssignDoc.AssignDocData;
import com.gmail.hanivisushiva.aksharafinserve.Models.CustomDocs.CustomDocs;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeAssignDocuments.EmployeAssignDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeCompletedDocuments.EmployeCompletedDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeData.EmployeData;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeDeleteAssignDocuments.EmployeDeleteAssignDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeDeleteCompletedDocuments.EmployeDeleteCompletedDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeMsgUser.EmployeMsgUser;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeUpdateAssignDocuments.EmployeUpdateAssignDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeUploadFinishedDoc.EmployeUploadFinishedDoc;
import com.gmail.hanivisushiva.aksharafinserve.Models.FinishedDoc.FinishedDoc;
import com.gmail.hanivisushiva.aksharafinserve.Models.Login.Login;
import com.gmail.hanivisushiva.aksharafinserve.Models.MessageUser.MessageUser;
import com.gmail.hanivisushiva.aksharafinserve.Models.SendTokenModel.SendTokenModel;
import com.gmail.hanivisushiva.aksharafinserve.Models.Services.Services;
import com.gmail.hanivisushiva.aksharafinserve.Models.UserDataModel.UserDataModel;
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
    @POST("logout.php")
    Call<SendTokenModel> log_out(
            @Field("rid") String rid
    );

    @FormUrlEncoded
    @POST("update_token.php")
    Call<SendTokenModel> send_token_to_server(
            @Field("rid") String rid,
            @Field("token") String token
    );




    @FormUrlEncoded
    @POST("login.php")
    Call<Login> user_login(
            @Field("txt_email") String username,
            @Field("txt_password") String password
    );


    @Multipart
    @POST("uploaddocs.php")
    Call<AddNewUserModel> uploadMultiple(
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
            @Field("txtName") String name,
            @Field("txtEmail") String email,
            @Field("txtPhone") String phone,
            @Field("txtRemarks") String remarks
    );

    @FormUrlEncoded
    @POST("finisheddocuments.php")
    Call<FinishedDoc> finshed_doc(
            @Field("rid") String rid
    );


    @POST("a_employees.php")
    Call<AllEmploye> get_all_employees();


    @POST("a_custdocs.php")
    Call<CustomDocs> get_custom_docs();

    @POST("a_status.php")
    Call<AStatus> get_status();

//    @FormUrlEncoded
//    @POST("a_assign.php")
//    Call<Assign> assign(
//            @Field("did") String did,
//            @Field("txtRemark") String remarks,
//            @Field("status") String status,
//            @Field("assigned") String assigned_to
//    );

    @FormUrlEncoded
    @POST("e_assigned.php")
    Call<AssignDocData> get_assign_doc(
            @Field("eid") String eid
    );



    @POST("a_services.php")
    Call<Services> get_admin_services();


    @Multipart
    @POST("a_adduser.php")
    Call<AddNewUserModel> add_new_user(
            @Part("drpService") RequestBody services,
            @Part("txtothers") RequestBody others,
            @Part("radServiceAgreement") RequestBody agreement,
            @Part("txtName") RequestBody name,
            @Part("txtPassword") RequestBody password,
            @Part("txtEmail") RequestBody email,
            @Part("txtPerson") RequestBody person,
            @Part("txtPhone") RequestBody phone,
            @Part("txtMessage") RequestBody message,
            @Part MultipartBody.Part file);


    @Multipart
    @POST("a_addemployee.php")
    Call<AddNewUserModel> add_new_employee(
            @Part("txtName") RequestBody name,
            @Part("txtPassword") RequestBody password,
            @Part("txtEmail") RequestBody email,
            @Part("txtPhone") RequestBody phone,
            @Part MultipartBody.Part file);



    @POST("a_employees.php")
    Call<EmployeData> get_employee_data();


    @POST("a_users.php")
    Call<UserDataModel> get_user_data();

    @FormUrlEncoded
    @POST("a_smessage.php")
    Call<AdminPhoneMsg> send_phone_msg(
            @Field("drpProcess") String value,
            @Field("txtRemarks") String remarks
    );


    @FormUrlEncoded
    @POST("a_umessage.php")
    Call<MessageUser> send_msg_user(
            @Field("drpProcess") String value,
            @Field("txtRemarks") String remarks
    );


    @POST("a_completeddocuments.php")
    Call<AdminCompletedDocuments> get_admin_completed_documents();


    @FormUrlEncoded
    @POST("a_completeddocumentsdel.php")
    Call<AdminDeleteCompletedDocuments> delete_admin_completed_documents(
            @Field("rid") String finish_id
    );


    @FormUrlEncoded
    @POST("a_custdocsdel.php")
    Call<AdminDeleteDocumentManager> delete_document_manager(
            @Field("did") String did
    );


    @FormUrlEncoded
    @POST("a_custdocsupdate.php ")
    Call<AdminUpdateDocumentManager> update_complete_document_manager(
            @Field("did") String did,
            @Field("txtRemark") String remarks,
            @Field("drpStatus") String status,
            @Field("drpAssigned") String assigned_to,
            @Field("dates") String date
    );


    @Multipart
    @POST("a_upfinishdocs.php")
    Call<AddNewUserModel> admin_upload_finish_documents(
            @Part("description") RequestBody description,
            @Part("rid") RequestBody rid,
            @Part("size") RequestBody size,
            @Part("Users") RequestBody users,
            @Part("drpService") RequestBody services,
            @Part List<MultipartBody.Part> files);


    @POST("a_enquiries.php")
    Call<AdminEnquiries> get_admin_enquiries();


    @FormUrlEncoded
    @POST("a_latestupdate.php")
    Call<AdminLatestUpdates> admin_latest_updates(
            @Field("txtName") String name,
            @Field("txtDescription") String description,
            @Field("txtUrl") String txturl
    );



    //employee apies


    @FormUrlEncoded
    @POST("e_assigned.php")
    Call<EmployeAssignDocuments> get_employee_assign_docs(
            @Field("eid") String id
    );


    @FormUrlEncoded
    @POST("e_docsupdate.php")
    Call<EmployeUpdateAssignDocuments> update_employee_assign_docs(
            @Field("did") String finish_id,
            @Field("drpReady") String drop_ready,
            @Field("drpStatus") String drop_status
    );

    @FormUrlEncoded
    @POST("e_docsdel.php")
    Call<EmployeDeleteAssignDocuments> delete_employee_assign_docs(
            @Field("did") String id
    );


    @Multipart
    @POST("e_upfinishdocs.php")
    Call<EmployeUploadFinishedDoc> employee_upload_finish_documents(
            @Part("size") RequestBody size,
            @Part("rid") RequestBody rid,
            @Part("Users") RequestBody users,
            @Part("drpService") RequestBody services,
            @Part("txtMessage") RequestBody description,
            @Part List<MultipartBody.Part> files);


    @FormUrlEncoded
    @POST("e_compdocuments.php")
    Call<EmployeCompletedDocuments> get_employe_completed_docs(
            @Field("eid") String id
    );

    @FormUrlEncoded
    @POST("e_compdocumentsdel.php")
    Call<EmployeDeleteCompletedDocuments> delete_employe_completed_docs(
            @Field("did") String id
    );


    @FormUrlEncoded
    @POST("e_umessage.php")
    Call<EmployeMsgUser> employee_msg_user(
            @Field("drpProcess") String drop_process,
            @Field("txtRemarks") String remarks,
            @Field("rid") String rid
    );


   /*
    @FormUrlEncoded
    e_assigned.php


    http://glowindiablowindia.com/weboffice/api/e_docsdel.php



    a_completeddocuments.php
    @POST("prelims")
    Call<ExamList> exam_list(
            @Field("sid") String sid
    );

    yourdocs.php
    enquiry.php finisheddocuments.php


    @GET("prelims/{qid}")
    Call<List<QuestionsList>> get_all_questions(@Path("qid") String qid);

    a_employees.php



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
