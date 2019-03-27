package com.gmail.hanivisushiva.aksharafinserve.Adapters.Admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.hanivisushiva.aksharafinserve.Models.EmployeData.Datum;
import com.gmail.hanivisushiva.aksharafinserve.R;

import java.util.List;

public class EmployeDataAdapter extends RecyclerView.Adapter<EmployeDataAdapter.EmployeDataViewHolder> {

    private Context context;
    private List<Datum> datumList;

    public EmployeDataAdapter(Context context, List<Datum> datumList) {
        this.context = context;
        this.datumList = datumList;
    }

    @NonNull
    @Override
    public EmployeDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_data_item,viewGroup,false);
        return new EmployeDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeDataViewHolder employeDataViewHolder, int i) {

        Datum datum = datumList.get(i);
      employeDataViewHolder.name.setText(datum.getName());
        employeDataViewHolder.email.setText(datum.getEmail());
        employeDataViewHolder.phone.setText(datum.getPhone());
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class EmployeDataViewHolder extends RecyclerView.ViewHolder{
         TextView name,email,phone;
        public EmployeDataViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
        }
    }
}
