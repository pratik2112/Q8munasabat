package com.q8munasabat.config;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.q8munasabat.R;
import com.q8munasabat.utills.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class WebService {

    String serviceUrl = "";
    JSONObject jsonObject;
    JSONArray jsonArray;
    boolean ishowPrograssBar = true;
    Context context;

    OnResult onResult;

    public WebService(String serviceUrl, JSONObject jsonObject, boolean ishowPrograssBar, Context context) {
        this.serviceUrl = serviceUrl;
        this.jsonObject = jsonObject;
        this.ishowPrograssBar = ishowPrograssBar;
        this.context = context;
        this.jsonArray = new JSONArray();
    }

    public interface OnResult {
        public void OnSuccess(JSONObject result);

        public void OnSuccess(JSONArray result);

        public void OnSuccess(String result);

        public void OnFail(String error);
    }

    public WebService(String serviceUrl, JSONObject jsonObject, Context context) {
        this.serviceUrl = serviceUrl;
        this.jsonObject = jsonObject;
        this.context = context;

    }

    public void getData(int method, OnResult onResult) {
        try {
            this.onResult = onResult;
            if (ishowPrograssBar)
                CommonFunctions.createProgressBar(context, context.getString(R.string.msg_please_wait));
            CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(method, serviceUrl, jsonObject, RSJsonObjectListener, REsErrorListener);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Logger.debugE("jsonObjReq url--->", serviceUrl.toString() + " jsondata\n " + jsonObject.toString());
            if (AppController.getInstance() != null)
                AppController.getInstance().addToRequestQueue(jsonObjReq);
            else
                onResult.OnFail(context.getString(R.string.msg_server_error));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData(OnResult onResult, boolean isJsonArray) {
        try {
            this.onResult = onResult;
            if (ishowPrograssBar)
                CommonFunctions.createProgressBar(context, context.getString(R.string.msg_please_wait));
            CustomJsonArrayRequest jsonObjReq = new CustomJsonArrayRequest(Request.Method.POST, serviceUrl, jsonArray, RSJsonArrayListener, REsErrorListener);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Logger.debugE("jsonObjReq url--->", serviceUrl.toString() + " jsondata\n " + jsonArray);
            if (AppController.getInstance() != null)
                AppController.getInstance().addToRequestQueue(jsonObjReq);
            else
                onResult.OnFail(context.getString(R.string.msg_server_error));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData(OnResult onResult, boolean isJsonArray, final Map<String, String> mParams) {
        try {
            this.onResult = onResult;
            if (ishowPrograssBar)
                CommonFunctions.createProgressBar(context, context.getString(R.string.msg_please_wait));
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST, serviceUrl, RSStingObjectListener, REsErrorListener) {
                @Override
                public Map<String, String> getParams() {

                    return mParams;
                }
            };
            /*CustomJsonArrayRequest jsonObjReq = new CustomJsonArrayRequest(Request.Method.POST, serviceUrl, jsonArray, RSJsonArrayListener, REsErrorListener) {
                @Override
                public Map<String, String> getParams() {

                    return mParams;
                }
            };*/
            Log.e("data pass in hashmap", mParams.toString());
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Logger.debugE("jsonObjReq url--->", serviceUrl.toString() + " jsondata\n " + jsonArray);
            if (AppController.getInstance() != null)
                AppController.getInstance().addToRequestQueue(jsonObjReq);
            else
                onResult.OnFail(context.getString(R.string.msg_server_error));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData(OnResult onResult) {
        try {
            this.onResult = onResult;
            if (ishowPrograssBar)
                CommonFunctions.createProgressBar(context, context.getString(R.string.msg_please_wait));
            CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST, serviceUrl, jsonObject, RSJsonObjectListener, REsErrorListener);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Logger.debugE("jsonObjReq url--->", serviceUrl.toString() + " jsondata\n " + jsonObject);
            if (AppController.getInstance() != null)
                AppController.getInstance().addToRequestQueue(jsonObjReq);
            else
                onResult.OnFail(context.getString(R.string.msg_server_error));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData(OnResult onResult, final Map<String, String> mParams) {
        try {
            this.onResult = onResult;
            if (ishowPrograssBar)
                CommonFunctions.createProgressBar(context, context.getString(R.string.msg_please_wait));
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST, serviceUrl, RSStingObjectListener, REsErrorListener) {
                @Override
                public Map<String, String> getParams() {

                    return mParams;
                }
            };

            ///CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST, serviceUrl, jsonObject, RSJsonObjectListener, REsErrorListener);
            Log.e("data pass in hashmap", mParams.toString());
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Logger.debugE("jsonObjReq url--->", serviceUrl.toString() + " jsondata\n " + jsonObject);
            if (AppController.getInstance() != null)
                AppController.getInstance().addToRequestQueue(jsonObjReq);
            else
                onResult.OnFail(context.getString(R.string.msg_server_error));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Response.Listener<JSONArray> RSJsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            try {
                if (ishowPrograssBar)
                    CommonFunctions.destroyProgressBar();
                Logger.debugE("RSJsonArrayListener Response--->", response.toString());
                onResult.OnSuccess(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Response.Listener<JSONObject> RSJsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                if (ishowPrograssBar)
                    CommonFunctions.destroyProgressBar();
                Logger.debugE("RSJsonObjectListener Response--->", response.toString());
                onResult.OnSuccess(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    Response.ErrorListener REsErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            try {
                if (ishowPrograssBar)
                    CommonFunctions.destroyProgressBar();
                VolleyLog.d("", "Error: " + error.getMessage());
                onResult.OnFail(context.getString(R.string.msg_server_error));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Response.Listener<String> RSStingObjectListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String Response) {
            try {
                if (ishowPrograssBar)
                    CommonFunctions.destroyProgressBar();
                Logger.debugE("RSJsonObjectListener Response--->", Response.toString());
                onResult.OnSuccess(Response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}