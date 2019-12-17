package com.q8munasabat.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("email")
    public String email;
    @SerializedName("error")
    public String error;
    @SerializedName("data")
    public DataEntity data;
    @SerializedName("result")
    public String result;

    public static class DataEntity {
        @SerializedName("block_by")
        public String block_by;
        @SerializedName("status")
        public String status;
        @SerializedName("otpstatus")
        public String otpstatus;
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;
        @SerializedName("registerbanner")
        public String registerbanner;
    }
}