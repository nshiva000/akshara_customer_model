package com.gmail.hanivisushiva.aksharafinserve.Adapters.Employee;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeAssignDocuments.Datum;
import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeDeleteAssignDocuments.EmployeDeleteAssignDocuments;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Employee.EmployeUpdateAssignDocActivity;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeAssignDocAdapter extends RecyclerView.Adapter<EmployeAssignDocAdapter.EmployeAssignDocViewHolder> {

    private Context context;
    private List<Datum> datumList;

    public EmployeAssignDocAdapter(Context context, List<Datum> datumList) {
        this.context = context;
        this.datumList = datumList;
    }

    @NonNull
    @Override
    public EmployeAssignDocViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employe_assign_doc_item,viewGroup,false);
        return new EmployeAssignDocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeAssignDocViewHolder employeAssignDocViewHolder, int i) {

        final Datum datum = datumList.get(i);

        employeAssignDocViewHolder.des.setText(datum.getDescription());
        employeAssignDocViewHolder.date.setText(datum.getRemarks());
        employeAssignDocViewHolder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast("update");

                Intent intent = new Intent(context,EmployeUpdateAssignDocActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("did",datum.getDid());
                context.startActivity(intent);
            }
        });
        employeAssignDocViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_doc(datum.getDid());


            }
        });
        employeAssignDocViewHolder.download.setOnClickListener(new View.OnClickListener() {
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

    public class EmployeAssignDocViewHolder extends RecyclerView.ViewHolder{

        TextView des,no,date;
        Button update,delete,download;

        public EmployeAssignDocViewHolder(@NonNull View itemView) {
            super(itemView);

            des = itemView.findViewById(R.id.documents);
            no = itemView.findViewById(R.id.no);
            date = itemView.findViewById(R.id.date);
            update = itemView.findViewById(R.id.update);
            delete = itemView.findViewById(R.id.delete);
            download = itemView.findViewById(R.id.download);
        }
    }
    private void Toast(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    private void delete_doc(String id){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Deleting, Please Wait");
        progressDialog.show();

        Call<EmployeDeleteAssignDocuments> call = RetrofitClient.getmInstance().getApi().delete_employee_assign_docs(id);

        call.enqueue(new Callback<EmployeDeleteAssignDocuments>() {
            @Override
            public void onResponse(Call<EmployeDeleteAssignDocuments> call, Response<EmployeDeleteAssignDocuments> response) {
                progressDialog.dismiss();
                EmployeDeleteAssignDocuments employeDeleteAssignDocuments = response.body();
                if (employeDeleteAssignDocuments != null){
                    if (employeDeleteAssignDocuments.getStatus()){
                        Toast("document deleted Successfully");
                    }else {
                        Toast("document deleted failed,try again later ");
                    }
                }
            }

            @Override
            public void onFailure(Call<EmployeDeleteAssignDocuments> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }





}
