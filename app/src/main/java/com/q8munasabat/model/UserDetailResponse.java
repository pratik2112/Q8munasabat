package com.q8munasabat.model;

import com.google.gson.annotations.SerializedName;

public class UserDetailResponse {

    @SerializedName("error")
    public String error;
    @SerializedName("data")
    public DataEntity data;
    @SerializedName("result")
    public String result;

    public static class DataEntity {
        @SerializedName("social")
        public String social;
        @SerializedName("password")
        public String password;
        @SerializedName("sharephonenumber")
        public String sharephonenumber;
        @SerializedName("instagramurl")
        public String instagramurl;
        @SerializedName("googleurl")
        public String googleurl;
        @SerializedName("facebookurl")
        public String facebookurl;
        @SerializedName("websiteurl")
        public String websiteurl;
        @SerializedName("address")
        public String address;
        @SerializedName("image")
        public String image;
        @SerializedName("mobileno")
        public String mobileno;
        @SerializedName("countrycode")
        public String countrycode;
        @SerializedName("email")
        public String email;
        @SerializedName("lastname")
        public String lastname;
        @SerializedName("firstname")
        public String firstname;
        @SerializedName("registerbanner")
        public String registerbanner;
    }
}