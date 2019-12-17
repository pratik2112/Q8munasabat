package com.q8munasabat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class NotificationResponse {

    @Expose
    @SerializedName("data")
    public List<DataEntity> data;
    @Expose
    @SerializedName("error")
    public String error;
    @Expose
    @SerializedName("result")
    public String result;

    public static class DataEntity {
        @Expose
        @SerializedName("createddate")
        public String createddate;
        @Expose
        @SerializedName("memeberid")
        public String memeberid;
        @Expose
        @SerializedName("message")
        public String message;
        @Expose
        @SerializedName("type")
        public String type;
        @Expose
        @SerializedName("id")
        public String id;
    }
}