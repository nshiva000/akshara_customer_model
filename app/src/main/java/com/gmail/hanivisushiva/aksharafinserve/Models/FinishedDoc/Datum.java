package com.gmail.hanivisushiva.aksharafinserve.Models.FinishedDoc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("finish_id")
    @Expose
    private String finishId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("rid")
    @Expose
    private String rid;
    @SerializedName("documents")
    @Expose
    private String documents;
    @SerializedName("documentspath")
    @Expose
    private String documentspath;
    @SerializedName("services")
    @Expose
    private String services;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("assigned")
    @Expose
    private String assigned;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("accept")
    @Expose
    private String accept;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("expire_date")
    @Expose
    private String expireDate;
    @SerializedName("date")
    @Expose
    private String date;

    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getDocumentspath() {
        return documentspath;
    }

    public void setDocumentspath(String documentspath) {
        this.documentspath = documentspath;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}