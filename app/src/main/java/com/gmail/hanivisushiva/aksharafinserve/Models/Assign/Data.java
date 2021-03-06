package com.gmail.hanivisushiva.aksharafinserve.Models.Assign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

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
    private String assigned;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("accept")
    @Expose
    private Object accept;
    @SerializedName("status")
    @Expose
    private String status;
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

    public Object getAccept() {
        return accept;
    }

    public void setAccept(Object accept) {
        this.accept = accept;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Data{" +
                "did='" + did + '\'' +
                ", rid='" + rid + '\'' +
                ", documents='" + documents + '\'' +
                ", documentspath='" + documentspath + '\'' +
                ", description='" + description + '\'' +
                ", assigned='" + assigned + '\'' +
                ", remarks='" + remarks + '\'' +
                ", accept=" + accept +
                ", status='" + status + '\'' +
                ", expireDate=" + expireDate +
                ", date='" + date + '\'' +
                '}';
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