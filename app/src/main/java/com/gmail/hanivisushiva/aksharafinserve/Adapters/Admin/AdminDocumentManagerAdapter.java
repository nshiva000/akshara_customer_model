package com.gmail.hanivisushiva.aksharafinserve.Adapters.Admin;

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

import com.gmail.hanivisushiva.aksharafinserve.Models.AdminDeleteDocumentManager.AdminDeleteDocumentManager;
import com.gmail.hanivisushiva.aksharafinserve.Models.CustomDocs.Datum;
import com.gmail.hanivisushiva.aksharafinserve.Pages.Admin.AssignDoc;
import com.gmail.hanivisushiva.aksharafinserve.R;
import com.gmail.hanivisushiva.aksharafinserve.RetrofitClient;
import com.gmail.hanivisushiva.aksharafinserve.Welcome;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDocumentManagerAdapter extends RecyclerView.Adapter<AdminDocumentManagerAdapter.CustomDocViewHolder> {

    private List<Datum> datumList;
    private Context context;
    Datum datum;

    public AdminDocumentManagerAdapter(List<Datum> datumList, Context context) {
        this.datumList = datumList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomDocViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_doc_manager_item,viewGroup,false);
        return new CustomDocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomDocViewHolder customDocViewHolder, final int i) {
        datum = datumList.get(i);
        final String des_txt,date_txt,did_txt,path_txt;

        des_txt = datum.getDescription();
        date_txt = datum.getDate();
        path_txt = datum.getDocumentspath();
        did_txt = datum.getDid();
        final String did = datum.getDid();



       customDocViewHolder.client_id.setText(datum.getDescription());
       customDocViewHolder.name.setText(datum.getRid());
       customDocViewHolder.work_status.setText(datum.getStatus());

        customDocViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               delete_doc(did);
            }
        });

        final String url = datum.getDocumentspath();

        customDocViewHolder.download.setOnClickListener(new View.OnClickListener() {
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




       customDocViewHolder.update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context,AssignDoc.class);
               intent.putExtra("des",des_txt);
               intent.putExtra("date",date_txt);
               intent.putExtra("path",path_txt);
               intent.putExtra("did",did_txt);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);
           }
       });

    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class CustomDocViewHolder extends RecyclerView.ViewHolder{

        TextView client_id,name,work_status;
        Button update,delete,download;

        public CustomDocViewHolder(@NonNull View itemView) {
            super(itemView);


            client_id = itemView.findViewById(R.id.client_id);
           name = itemView.findViewById(R.id.name);
           work_status = itemView.findViewById(R.id.work_status);
            update = itemView.findViewById(R.id.update);
            delete = itemView.findViewById(R.id.delete);
            download = itemView.findViewById(R.id.download);
        }
    }


    private void delete_doc(String id){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting Please Wait");
        progressDialog.show();
        Call<AdminDeleteDocumentManager> call = RetrofitClient.getmInstance().getApi().delete_document_manager(id);
        call.enqueue(new Callback<AdminDeleteDocumentManager>() {
            @Override
            public void onResponse(Call<AdminDeleteDocumentManager> call, Response<AdminDeleteDocumentManager> response) {
                progressDialog.dismiss();
                AdminDeleteDocumentManager adminDeleteDocumentManager = response.body();
                if (adminDeleteDocumentManager != null){
                    if (adminDeleteDocumentManager.getStatus()){
                        Toast(adminDeleteDocumentManager.getMessage() );
                        Intent intent = new Intent(context,Welcome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }else {
                        Toast("something went wrong");
                    }
                }
            }

            @Override
            public void onFailure(Call<AdminDeleteDocumentManager> call, Throwable t) {
                progressDialog.dismiss();
                Toast("something went wrong");
            }
        });
    }

    private void Toast(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }


}
