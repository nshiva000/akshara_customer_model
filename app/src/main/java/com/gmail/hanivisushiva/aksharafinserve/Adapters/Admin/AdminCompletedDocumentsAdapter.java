package com.gmail.hanivisushiva.aksharafinserve.Adapters.Admin;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.Models.AdminCompletedDocuments.Datum;
import com.gmail.hanivisushiva.aksharafinserve.Models.AdminDeleteCompletedDocuments.AdminDeleteCompletedDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.AdminCompletdDocumnetsActivity;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCompletedDocumentsAdapter extends RecyclerView.Adapter<AdminCompletedDocumentsAdapter.AdminCompletedDocumentsViewHolder> {

    private Context context;
    private List<Datum> datumList;

    public AdminCompletedDocumentsAdapter(Context context, List<Datum> datumList) {
        this.context = context;
        this.datumList = datumList;
    }

    @NonNull
    @Override
    public AdminCompletedDocumentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_completed_documents_item,viewGroup,false);
        return new AdminCompletedDocumentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCompletedDocumentsViewHolder adminCompletedDocumentsViewHolder, int i) {

        final Datum datum = datumList.get(i);

        adminCompletedDocumentsViewHolder.doc_name.setText(datum.getDocumentspath());

        adminCompletedDocumentsViewHolder.delete_txt.setText("");
        adminCompletedDocumentsViewHolder.user.setText(datum.getRid());
        final String url = datum.getDocumentspath();
        adminCompletedDocumentsViewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long ref = downloadManager.enqueue(request);
                Toast.makeText(context.getApplicationContext(),
                        "Your file is now downloading...", Toast.LENGTH_LONG).show();
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(browserIntent);
            }
        });


        adminCompletedDocumentsViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 delete_admin_completed_documents(datum.getFinishId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class AdminCompletedDocumentsViewHolder extends RecyclerView.ViewHolder{

        TextView doc_name,delete_txt,user;
        Button download,delete;

        public AdminCompletedDocumentsViewHolder(@NonNull View itemView) {
            super(itemView);
            doc_name = itemView.findViewById(R.id.doc_name);
            delete = itemView.findViewById(R.id.delete);
            user = itemView.findViewById(R.id.user);
            download = itemView.findViewById(R.id.download);
            delete_txt = itemView.findViewById(R.id.delete_txt);
        }
    }

    private void delete_admin_completed_documents(String rid){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<AdminDeleteCompletedDocuments> adminDeleteCompletedDocumentsCall = RetrofitClient.getmInstance().getApi().delete_admin_completed_documents(rid);
        adminDeleteCompletedDocumentsCall.enqueue(new Callback<AdminDeleteCompletedDocuments>() {
            @Override
            public void onResponse(Call<AdminDeleteCompletedDocuments> call, Response<AdminDeleteCompletedDocuments> response) {
                progressDialog.dismiss();
                AdminDeleteCompletedDocuments adminDeleteCompletedDocuments = response.body();

                if (adminDeleteCompletedDocuments != null){
                    if (adminDeleteCompletedDocuments.getStatus()){
                      Toast(adminDeleteCompletedDocuments.getMessage());
                      Intent intent = new Intent(context,AdminCompletdDocumnetsActivity.class);
                      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                      context.startActivity(intent);
                    }else {
                        Toast(adminDeleteCompletedDocuments.getMessage());
                    }
                }else {
                    Toast("server Not responding please try again later");
                }


            }

            @Override
            public void onFailure(Call<AdminDeleteCompletedDocuments> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    private void Toast(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }



}
