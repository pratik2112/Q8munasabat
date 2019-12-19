package com.q8munasabat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OtpResponse {

    @SerializedName("otp")
    public String otp;

    @SerializedName("error")
    public boolean error;

    @SerializedName("data")
    public List<String> data;

    @SerializedName("result")
    public String result;
}