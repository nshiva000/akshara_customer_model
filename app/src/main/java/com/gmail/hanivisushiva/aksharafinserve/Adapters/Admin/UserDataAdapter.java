package com.gmail.hanivisushiva.aksharafinserve.Adapters.Admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.hanivisushiva.aksharafinserve.Models.UserDataModel.Datum;
import com.gmail.hanivisushiva.aksharafinserve.R;

import java.util.List;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.UserDataViewHolder> {

    private Context context;
    private List<Datum> datumList;

    public UserDataAdapter(Context context, List<Datum> datumList) {
        this.context = context;
        this.datumList = datumList;
    }

    @NonNull
    @Override
    public UserDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_data_item,viewGroup,false);
        return new UserDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDataViewHolder userDataViewHolder, int i) {

        Datum datum = datumList.get(i);
        userDataViewHolder.name.setText(datum.getName());
        userDataViewHolder.email.setText(datum.getEmail());
        if (datum.getClientCode() != null){
                userDataViewHolder.client_code.setText(datum.getClientCode());

        }

        userDataViewHolder.phone.setText(datum.getPhone());
        userDataViewHolder.contact_person.setText(datum.getContactPerson());

    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class UserDataViewHolder extends RecyclerView.ViewHolder{

        TextView name,client_code,email,contact_person,phone;

        public UserDataViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            client_code = itemView.findViewById(R.id.client_id);
            email = itemView.findViewById(R.id.email);
            contact_person = itemView.findViewById(R.id.contact_person);
            phone = itemView.findViewById(R.id.phone);
        }
    }
}
