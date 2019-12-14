package com.q8munasabat.config;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.q8munasabat.BuildConfig;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Navneet Boghani on 7/3/16.
 */
public class CustomJsonArrayRequest extends JsonArrayRequest {
    public CustomJsonArrayRequest(int method, String url, JSONArray jsonArray, Response.Listener listener, Response.ErrorListener errorListener) {

        super(method, url, jsonArray, listener, errorListener);
        Logger.debugE("url--> " + url + " jsonobject--> " + jsonArray);
    }


    @Override
    public Map getHeaders() throws AuthFailureError {
        Map headers = new HashMap();
        //if needed access toekn
        headers.put(Constants.HTTP_ATH, BuildConfig.APPLICATION_ID);
        Logger.debugE(Constants.HTTP_ATH, BuildConfig.APPLICATION_ID);
        return headers;
    }
}