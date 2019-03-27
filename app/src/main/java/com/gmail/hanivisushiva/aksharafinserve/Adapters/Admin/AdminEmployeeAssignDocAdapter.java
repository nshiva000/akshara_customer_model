package com.gmail.hanivisushiva.aksharafinserve.Adapters.Admin;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.gmail.hanivisushiva.aksharafinserve.Models.AssignDoc.Datum;
import com.gmail.hanivisushiva.aksharafinserve.R;

import java.util.List;

public class AdminEmployeeAssignDocAdapter extends RecyclerView.Adapter<AdminEmployeeAssignDocAdapter.YourDocViewHolder> {

    private Context context;
    private List<Datum> yourDocList;
    DownloadManager downloadManager;

    public AdminEmployeeAssignDocAdapter(Context context, List<Datum> yourDocList) {
        this.context = context;
        this.yourDocList = yourDocList;
    }

    @NonNull
    @Override
    public YourDocViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.customer_your_doc_item, null);
        return new YourDocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourDocViewHolder yourDocViewHolder, int i) {

        Log.e("size",yourDocList.size()+"");
         yourDocViewHolder.des.setText(yourDocList.get(i).getDescription());
         yourDocViewHolder.no.setText(i+1+"");
         yourDocViewHolder.date.setText(yourDocList.get(i).getDate());
        final String url = yourDocList.get(i).getDocumentspath();


         yourDocViewHolder.download.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                 Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                 browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 context.startActivity(browserIntent);

               /*
                 DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                 Uri uri = Uri.parse(url);
                 DownloadManager.Request request = new DownloadManager.Request(uri);
                 request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                 Long ref = downloadManager.enqueue(request);
                 Toast.makeText(context.getApplicationContext(),
                         "Your file is now downloading...", Toast.LENGTH_LONG).show();

                         )*/

             }
         });
    }

    @Override
    public int getItemCount() {
        return yourDocList.size();
    }

    public class YourDocViewHolder extends RecyclerView.ViewHolder{
        TextView des,no,date;
        RelativeLayout card;
        ImageView download;
        public YourDocViewHolder(@NonNull View itemView) {
            super(itemView);

            des = itemView.findViewById(R.id.documents);
            no = itemView.findViewById(R.id.no);
            date = itemView.findViewById(R.id.date);
            card = itemView.findViewById(R.id.card);
            download = itemView.findViewById(R.id.download);
        }
    }
}
