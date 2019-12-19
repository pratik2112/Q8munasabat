package com.q8munasabat.model;

import com.google.gson.annotations.SerializedName;

public class SignupResponse {
    
    @SerializedName("memberid")
    public int memberid;
    
    @SerializedName("error")
    public String error;
    
    @SerializedName("result")
    public String result;
}