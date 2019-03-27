package com.gmail.hanivisushiva.aksharafinserve.Adapters.Admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.hanivisushiva.aksharafinserve.Models.AdminEnquiries.Datum;
import com.gmail.hanivisushiva.aksharafinserve.R;

import java.util.List;

public class AdminEnquiryAdapter extends RecyclerView.Adapter<AdminEnquiryAdapter.AdminEnquiryViewHolder> {
    private Context context;
    private List<Datum> datumList;

    public AdminEnquiryAdapter(Context context, List<Datum> datumList) {
        this.context = context;
        this.datumList = datumList;
    }

    @NonNull
    @Override
    public AdminEnquiryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_enquiry_item,viewGroup,false);
        return new AdminEnquiryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminEnquiryViewHolder adminEnquiryViewHolder, int i) {

        Datum datum = datumList.get(i);
        adminEnquiryViewHolder.customer_id.setText(datum.getCustomerid());
        adminEnquiryViewHolder.name.setText(datum.getName());
        adminEnquiryViewHolder.email.setText(datum.getEmail());
        adminEnquiryViewHolder.phone.setText(datum.getPhone());
        adminEnquiryViewHolder.message.setText(datum.getMessage());
        adminEnquiryViewHolder.date.setText(datum.getDate());

    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class AdminEnquiryViewHolder extends RecyclerView.ViewHolder{

        TextView customer_id,name,email,phone,message,date;

        public AdminEnquiryViewHolder(@NonNull View itemView) {
            super(itemView);

            customer_id = itemView.findViewById(R.id.customer_id);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);
        }
    }
}
