package com.gmail.hanivisushiva.aksharafinserve.Models.YourDoc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("did")
    @Expose
    private String did;
    @SerializedName("rid")
    @Expose
    private String rid;
    @SerializedName("documents")
    @Expose
    private String documents;
    @SerializedName("documentspath")
    @Expose
    private String documentspath;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("assigned")
    @Expose
    private Object assigned;
    @SerializedName("remarks")
    @Expose
    private Object remarks;
    @SerializedName("accept")
    @Expose
    private Object accept;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("expire_date")
    @Expose
    private Object expireDate;
    @SerializedName("date")
    @Expose
    private String date;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getAssigned() {
        return assigned;
    }

    public void setAssigned(Object assigned) {
        this.assigned = assigned;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public Object getAccept() {
        return accept;
    }

    public void setAccept(Object accept) {
        this.accept = accept;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Object expireDate) {
        this.expireDate = expireDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}