package com.gmail.hanivisushiva.aksharafinserve.Adapters.Employee;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeCompletedDocuments.Datum;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeCompletedDocuments.EmployeCompletedDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeDeleteCompletedDocuments.EmployeDeleteCompletedDocuments;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Welcome;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeCompletedDocAdapter extends RecyclerView.Adapter<EmployeCompletedDocAdapter.EmployeCompletedDocViewHolder> {


    private Context context;
    private List<Datum> datumList;

    public EmployeCompletedDocAdapter(Context context, List<Datum> datumList) {
        this.context = context;
        this.datumList = datumList;
    }

    @NonNull
    @Override
    public EmployeCompletedDocViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employe_completed_document_item,viewGroup,false);
        return new EmployeCompletedDocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeCompletedDocViewHolder employeCompletedDocViewHolder, int i) {

        final Datum datum = datumList.get(i);
//
//
//
//

        Log.e("employee data",datum.getClientCode()+"---"+datum.getDocuments());
        employeCompletedDocViewHolder.documents.setText(datum.getDocuments());
        employeCompletedDocViewHolder.client_code.setText(datum.getClientCode());
        employeCompletedDocViewHolder.name.setText(datum.getName());
        employeCompletedDocViewHolder.users.setText(datum.getUserId());

        employeCompletedDocViewHolder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_doc(datum.getFinishId());
            }
        });

        employeCompletedDocViewHolder.download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = datum.getDocumentspath();
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

    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class EmployeCompletedDocViewHolder extends RecyclerView.ViewHolder{

        Button delete_btn,download_btn;
        TextView name,users,client_code,documents;

        public EmployeCompletedDocViewHolder(@NonNull View itemView) {
            super(itemView);

            delete_btn = itemView.findViewById(R.id.delete_btn);
            download_btn = itemView.findViewById(R.id.download_btn);
            documents = itemView.findViewById(R.id.documents);
            name = itemView.findViewById(R.id.name);
            users = itemView.findViewById(R.id.users);
            client_code = itemView.findViewById(R.id.client_code);
        }
    }


    private void Toast(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    private void delete_doc(String id){
        Toast("document deleted successfully");
        Call<EmployeDeleteCompletedDocuments> call = RetrofitClient.getmInstance().getApi().delete_employe_completed_docs(id);
        call.enqueue(new Callback<EmployeDeleteCompletedDocuments>() {
            @Override
            public void onResponse(Call<EmployeDeleteCompletedDocuments> call, Response<EmployeDeleteCompletedDocuments> response) {
                EmployeDeleteCompletedDocuments employeDeleteCompletedDocuments = response.body();
                if (employeDeleteCompletedDocuments != null){
                    if (employeDeleteCompletedDocuments.getStatus()){
                      Toast("document deleted successfully");

                      Intent intent = new Intent(context, Welcome.class);
                      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                      context.startActivity(intent);
                    }else {
                        Toast("document not Deleted, please try later ");
                    }
                }
            }

            @Override
            public void onFailure(Call<EmployeDeleteCompletedDocuments> call, Throwable t) {
                Toast("document not Deleted,server error please try later ");
            }
        });
    }



}
