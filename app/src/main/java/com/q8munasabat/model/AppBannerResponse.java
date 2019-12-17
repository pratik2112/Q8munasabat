package com.q8munasabat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppBannerResponse {
    @SerializedName("error")
    public String error;
    @SerializedName("data")
    public List<DataEntity> data;
    @SerializedName("result")
    public String result;

    public static class DataEntity {
        @SerializedName("status")
        public String status;
        @SerializedName("modifieddate")
        public String modifieddate;
        @SerializedName("createddate")
        public String createddate;
        @SerializedName("modifiedby")
        public String modifiedby;
        @SerializedName("addedby")
        public String addedby;
        @SerializedName("priority")
        public String priority;
        @SerializedName("link")
        public String link;
        @SerializedName("file")
        public String file;
        @SerializedName("smallfile")
        public String smallfile;
        @SerializedName("type")
        public String type;
        @SerializedName("id")
        public String id;
    }
}