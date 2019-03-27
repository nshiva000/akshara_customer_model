package com.gmail.hanivisushiva.aksharafinserve.Models.AddNewUserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddNewUserModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AddNewUserModel{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}