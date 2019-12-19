package com.q8munasabat.activity;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.q8munasabat.R;
import com.q8munasabat.base.BaseActivity;
import com.q8munasabat.config.CommonFunctions;
import com.q8munasabat.config.Constants;
import com.q8munasabat.config.Q8MunasabatConfig;
import com.q8munasabat.config.WebService;
import com.q8munasabat.model.LoginResponse;
import com.q8munasabat.model.OtpResponse;
import com.q8munasabat.model.SignupResponse;
import com.q8munasabat.model.UserDetailResponse;
import com.q8munasabat.uploadfile.MultiPartRequest;
import com.q8munasabat.uploadfile.Template;
import com.q8munasabat.uploadfile.VolleySingleton;
import com.q8munasabat.utills.OtpEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodeVerifyActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_home)
    ImageView iv_home;
    Bundle bundle;
    String from, firstname, lastname, email, password, countrycode, mobileno, otpvalue, tempProfile, lang, path, temp_userid;
    Uri imageuri;
    @BindView(R.id.tv_otp_msg)
    TextView tv_otp_msg;
    @BindView(R.id.tv_otp_code)
    TextView tv_otp_code;
    @BindView(R.id.tv_resend)
    TextView tv_resend;
    @BindView(R.id.tv_seond_remain)
    TextView tv_seond_remain;
    @BindView(R.id.et_otp)
    OtpEditText et_otp;
    SignupResponse signupResponse;
    LoginResponse loginResponse;
    UserDetailResponse userDetailResponse;

    private ArrayList<Image> images = new ArrayList<>();
    VolleySingleton volleySingleton;
    private RequestQueue mRequest;
    MultiPartRequest mMultiPartRequest;
    OtpResponse otpResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        inticompnets();
    }

    @Override
    protected int getContentView() {
        lang = CommonFunctions.getPreference(this, Constants.languageid, "0");
        Locale locale = new Locale(lang.equalsIgnoreCase("0") ? "en" : "ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        return R.layout.activity_code_verify;
    }

    private void inticompnets() {
        try {
            ButterKnife.bind(this);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            volleySingleton = new VolleySingleton(this);
            if (getIntent().getExtras() != null) {
                bundle = getIntent().getExtras();
                if (bundle.containsKey(Constants.from)) {
                    from = bundle.getString(Constants.from);
                }
                if (bundle.containsKey(Constants.firstname)) {
                    firstname = bundle.getString(Constants.firstname);
                }
                if (bundle.containsKey(Constants.lastname)) {
                    lastname = bundle.getString(Constants.lastname);
                }
                if (bundle.containsKey(Constants.email)) {
                    email = bundle.getString(Constants.email);
                }
                if (bundle.containsKey(Constants.password)) {
                    password = bundle.getString(Constants.password);
                }
                if (bundle.containsKey(Constants.countrycode)) {
                    countrycode = bundle.getString(Constants.countrycode);
                }
                if (bundle.containsKey(Constants.mobileno)) {
                    mobileno = bundle.getString(Constants.mobileno);
                }
                if (bundle.containsKey(Constants.otpvalue)) {
                    otpvalue = bundle.getString(Constants.otpvalue);
                }
                if (bundle.containsKey(Constants.path)) {
                    path = bundle.getString(Constants.path);
                }
                if (bundle.containsKey(Constants.imageuri)) {
                    String imageuri1 = bundle.getString(Constants.imageuri);
                    imageuri = Uri.parse(imageuri1);
                }
                if (bundle.containsKey(Constants.image)) {
                    Type listOfdoctorType = new TypeToken<List<Image>>() {
                    }.getType();
                    images = new Gson().fromJson(getIntent().getExtras().getString(Constants.image), listOfdoctorType);
                }
            }
            tv_title.setText(from);
            iv_home.setVisibility(View.VISIBLE);
            String phoneno = countrycode + " " + mobileno;
            tv_otp_code.setText(countrycode);
            tv_otp_msg.setText(mobileno);

            new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    tv_seond_remain.setText("00:" + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    tv_seond_remain.setVisibility(View.GONE);
                    tv_resend.setVisibility(View.VISIBLE);
                    tv_resend.setClickable(true);
                    tv_resend.setEnabled(true);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_resend)
    public void resendClick() {
        try {
            if (CommonFunctions.checkConnection(this)) {
                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.getotpURL;
                Map<String, String> mParams = new HashMap<>();
                mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                mParams.put(Constants.languageid, lang);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.mobileno, countrycode + mobileno);
                jsonObject.put(Constants.languageid, lang);
                mParams.put(Constants.data, String.valueOf(jsonObject));

                WebService webService = new WebService(url, new JSONObject(), true, this);
                webService.getData(new WebService.OnResult() {
                    @Override
                    public void OnSuccess(JSONObject result) {
                    }

                    @Override
                    public void OnSuccess(JSONArray result) {

                    }

                    @Override
                    public void OnSuccess(String result) {
                        try {
                            Gson gson = new Gson();
                            et_otp.setText("");
                            otpResponse = gson.fromJson(result.toString(), OtpResponse.class);
                            otpvalue = otpResponse.otp;
                            tv_seond_remain.setVisibility(View.VISIBLE);
                            tv_resend.setVisibility(View.GONE);
                            tv_resend.setClickable(false);
                            tv_resend.setEnabled(false);
                            new CountDownTimer(60000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    tv_seond_remain.setText("00:" + millisUntilFinished / 1000);
                                }

                                public void onFinish() {
                                    tv_seond_remain.setVisibility(View.GONE);
                                    tv_resend.setVisibility(View.VISIBLE);
                                    tv_resend.setClickable(true);
                                    tv_resend.setEnabled(true);
                                }
                            }.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnFail(String error) {
                    }
                }, mParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_verify)
    public void verifyClick() {
        try {
            if (otpvalue.equalsIgnoreCase(et_otp.getText().toString())) {
                registerUser();
            } else {
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_invalid_otp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerUser() {
        try {
            if (CommonFunctions.checkConnection(this)) {
                CommonFunctions.createProgressBar(this, getString(R.string.msg_please_wait));

                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.registerURL;

                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.firstname, firstname);
                jsonObject.put(Constants.lastname, lastname);
                jsonObject.put(Constants.email, email);
                jsonObject.put(Constants.password, password);
                jsonObject.put(Constants.countrycode, countrycode);
                jsonObject.put(Constants.mobileno, mobileno);
                jsonObject.put(Constants.device_token, CommonFunctions.getPreference(this, Constants.device_token, ""));
                jsonObject.put(Constants.device_udid, CommonFunctions.getDeviceUID(this));
                mRequest = volleySingleton.getInstance().getRequestQueue();
                mRequest.start();
                mMultiPartRequest = new MultiPartRequest(new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            CommonFunctions.destroyProgressBar();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        //check done com add
                        CommonFunctions.destroyProgressBar();
                        try {
                            Gson gson = new Gson();
                            signupResponse = gson.fromJson(response.toString(), SignupResponse.class);
                            temp_userid = String.valueOf(signupResponse.memberid);
                            if (signupResponse.result.equalsIgnoreCase(Constants.success)) {
                                CommonFunctions.setPreference(CodeVerifyActivity.this, Constants.isLogin, true);
                                // getLoginUser();
                                // attamptfirebase(); // correct code
                                getuserData(temp_userid);
                            } else
                                showAlertDialog(getString(R.string.app_name), signupResponse.error);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, images, jsonObject, url, Constants.apikey, lang, path, imageuri);
                mMultiPartRequest.setTag("MultiRequest");
                mMultiPartRequest.setRetryPolicy(new DefaultRetryPolicy(Template.VolleyRetryPolicy.SOCKET_TIMEOUT, Template.VolleyRetryPolicy.RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                mRequest.add(mMultiPartRequest);
            }
        } catch (Exception e) {
            CommonFunctions.destroyProgressBar();
            e.printStackTrace();
        }
    }

    /*private void attamptfirebase() {
        try {
            if (mAuth != null) {
                CommonFunctions.createProgressBar(this, getString(R.string.msg_please_wait));
                mAuth.createUserWithEmailAndPassword(email, Constants.passwordfirebase)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                CommonFunctions.destroyProgressBar();
                                try {
                                    if (task.isSuccessful()) {
                                        sendFileFirebase();
                                        *//*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();*//*
                                        Log.e("firebase signup done", "done");

                                    } else {
                                        // Log.w(TAG, "linkWithCredential:failure", task.getException());
                                        Toast.makeText(CodeVerifyActivity.this, "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("firebase signup not done", "errot" + task.getException().getMessage());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }
        } catch (Exception e) {
            CommonFunctions.destroyProgressBar();
            e.printStackTrace();
        }
    }

    private void sendFileFirebase() {
        try {
            Uri file = Uri.fromFile(new File(images.get(0).getPath()));
            CommonFunctions.createProgressBar(this, getString(R.string.msg_please_wait));
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("usersProfilePics").child(mUser.getUid());
            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            try {
                                CommonFunctions.destroyProgressBar();

                                riversRef.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                try {
                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                                    UserModelFireBase user = new UserModelFireBase(Constants.online, firstname + " " + lastname, email, mUser.getUid(), "", uri.toString(), CommonFunctions.getPreference(CodeVerifyActivity.this, Constants.device_token, ""));

                                                    databaseReference.child("users").child(mUser.getUid()).child("credentials").setValue(user);
                                                    getuserData(temp_userid);
                                                    //getLoginUser();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            try {
                                CommonFunctions.destroyProgressBar();
                                Toast.makeText(CodeVerifyActivity.this, R.string.msg_server_error, Toast.LENGTH_SHORT).show();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunctions.destroyProgressBar();
        }
    }*/

    public void getuserData(String temp_userid) {
        try {
            String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.userdetailURL;
            Map<String, String> mParams = new HashMap<>();
            mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
            mParams.put(Constants.languageid, lang);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.memberid, temp_userid);
            mParams.put(Constants.data, String.valueOf(jsonObject));

            WebService webService = new WebService(url, new JSONObject(), true, this);
            webService.getData(new WebService.OnResult() {
                @Override
                public void OnSuccess(JSONObject result) {
                }

                @Override
                public void OnSuccess(JSONArray result) {

                }

                @Override
                public void OnSuccess(String result) {
                    try {
                        Gson gson = new Gson();
                        userDetailResponse = gson.fromJson(result.toString(), UserDetailResponse.class);
                        LoginResponse loginResponse = new LoginResponse();
                        loginResponse.email = userDetailResponse.data.email;
                        loginResponse.error = userDetailResponse.error;
                        loginResponse.result = userDetailResponse.result;

                        LoginResponse.DataEntity dataEntity = new LoginResponse.DataEntity();
                        dataEntity.id = temp_userid;
                        dataEntity.name = userDetailResponse.data.firstname + " " + userDetailResponse.data.lastname;

                        loginResponse.data = dataEntity;
                        loginResponse.data.registerbanner = userDetailResponse.data.registerbanner;
                        CommonFunctions.setPreference(CodeVerifyActivity.this, Constants.isLogin, true);
                        CommonFunctions.setPreference(getApplicationContext(), Constants.userdata, gson.toJson(loginResponse));
                        //loginUser(true);
                        // firebaselogin(); // right code
                        CommonFunctions.changeactivity(CodeVerifyActivity.this, DashboardActivity.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnFail(String error) {
                }
            }, mParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void firebaselogin() {
        try {
            if (mAuth != null) {
                CommonFunctions.createProgressBar(this, getString(R.string.msg_please_wait));
                mAuth.signInWithEmailAndPassword(email, Constants.passwordfirebase)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                CommonFunctions.destroyProgressBar();
                                try {
                                    if (task.isSuccessful()) {
                                        //sendFileFirebase();
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                        Log.e("firebase signup done", "done");

                                    } else {
                                        // Log.w(TAG, "linkWithCredential:failure", task.getException());
                                        Toast.makeText(CodeVerifyActivity.this, "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("firebase signup done", "errot" + task.getException().getMessage());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                CommonFunctions.changeactivity(CodeVerifyActivity.this, WelcomeActivity.class);
                            }
                        });
            }
        } catch (Exception e) {
            CommonFunctions.destroyProgressBar();
            e.printStackTrace();
        }
    }*/

    @OnClick({R.id.iv_home})
    public void gotoback() {
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}