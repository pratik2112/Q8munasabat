package com.q8munasabat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.q8munasabat.R;
import com.q8munasabat.base.BaseActivity;
import com.q8munasabat.config.CommonFunctions;
import com.q8munasabat.config.Constants;
import com.q8munasabat.config.Q8MunasabatConfig;
import com.q8munasabat.config.WebService;
import com.q8munasabat.model.LoginResponse;
import com.q8munasabat.model.UserDetailResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_pass)
    EditText edt_pass;
    private String verifiedEmail = "";
    Bundle bundle;
    String from = "", lang;
    LoginResponse loginResponse;
    // LastLoginResponse lastLoginResponse;
    UserDetailResponse userDetailResponse;
    //FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return R.layout.activity_login;
    }

    private void inticompnets() {
        try {
            ButterKnife.bind(this);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }

            if (getIntent().getExtras() != null) {
                bundle = getIntent().getExtras();
                if (bundle.containsKey(Constants.from)) {
                    from = bundle.getString(Constants.from);
                }
            }

            if (lang.equalsIgnoreCase("1")) {
                edt_email.setGravity(Gravity.RIGHT);
                edt_pass.setGravity(Gravity.RIGHT);
            } else {
                edt_email.setGravity(Gravity.LEFT);
                edt_pass.setGravity(Gravity.LEFT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 100) {
            if (resultCode == -1) {
                try {
                    this.verifiedEmail = intent.getStringExtra("authAccount");
                    this.edt_email.setText(this.verifiedEmail);
                    this.edt_email.setSelection(this.edt_email.getText().length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick(R.id.btn_login)
    public void loginClick() {
        try {
            String emailvalid = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (edt_email.getText().toString().trim().length() == 0) {
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_email_mobil));
            } else if (edt_pass.getText().toString().trim().length() == 0) {
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_pwd));
            } /*else if (!edt_email.getText().toString().trim().matches(emailvalid)) {
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_valid_email));
            } */ else {
                loginUser();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                        //loginUser(true);
                        // firebaselogin(); // right code
                        CommonFunctions.changeactivity(LoginActivity.this, DashboardActivity.class);
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

    private void loginUser() {
        try {
            if (CommonFunctions.checkConnection(this)) {
                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.loginURL;
                Map<String, String> mParams = new HashMap<>();
                mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                mParams.put(Constants.languageid, lang);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.type, 1);
                if (TextUtils.isDigitsOnly(edt_email.getText().toString().trim())) {
                    jsonObject.put(Constants.email, "");
                    jsonObject.put(Constants.mobileno, edt_email.getText().toString().trim());
                } else {
                    jsonObject.put(Constants.email, edt_email.getText().toString().trim());
                    jsonObject.put(Constants.mobileno, "");
                }
                jsonObject.put(Constants.password, CommonFunctions.convertutf(edt_pass.getText().toString()));
                jsonObject.put(Constants.device_token, CommonFunctions.getPreference(this, Constants.device_token, ""));
                jsonObject.put(Constants.device_udid, CommonFunctions.getDeviceUID(this));
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
                            loginResponse = gson.fromJson(result.toString(), LoginResponse.class);
                            if (result.contains(Constants.success)) {
                                CommonFunctions.setPreference(LoginActivity.this, Constants.isLogin, true);
                                CommonFunctions.setPreference(getApplicationContext(), Constants.userdata, gson.toJson(loginResponse));
                                getuserData(loginResponse.data.id);
                                //firebaselogin(isSocial);
                            } else if (loginResponse.error.contains("Your account deactivated by user.")) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                                dialogBuilder.setTitle(getString(R.string.str_deactive1));
                                dialogBuilder.setMessage(getString(R.string.str_deactive2));
                                dialogBuilder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        CommonFunctions.hideSoftKeyboard(LoginActivity.this, edt_email);
                                        sendActiveMail(loginResponse.email);
                                    }
                                });
                                dialogBuilder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                dialogBuilder.setCancelable(false);
                                dialogBuilder.show();
                            } else {
                                showAlertDialog(getString(R.string.app_name), loginResponse.error);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnFail(String error) {
                        showAlertDialog(getString(R.string.app_name), loginResponse.error);
                    }
                }, mParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendActiveMail(String emailsocial) {
        try {
            if (CommonFunctions.checkConnection(this)) {
                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.sendactivemailURL;
                Map<String, String> mParams = new HashMap<>();
                mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                mParams.put(Constants.languageid, lang);
                mParams.put(Constants.member_email, emailsocial);

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
                            showAlertDialog(getString(R.string.app_name), getString(R.string.str_deactive3));
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

    /*private void firebaselogin() {
        try {
            if (mAuth != null) {
                CommonFunctions.createProgressBar(this, getString(R.string.msg_please_wait));
                mAuth.signInWithEmailAndPassword(emailID, Constants.passwordfirebase)
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

                                        DatabaseReference updateData = FirebaseDatabase.getInstance()
                                                .getReference("users")
                                                .child(mUser.getUid()).child(Constants.credentials);
                                        updateData.child(Constants.device_token).setValue(CommonFunctions.getPreference(LoginActivity.this, Constants.device_token, ""));
                                    } else {
                                        if (!TextUtils.isDigitsOnly(edt_email.getText().toString().trim())) {
                                            // Log.w(TAG, "linkWithCredential:failure", task.getException());
                                            Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("firebase signup done", "errot" + task.getException().getMessage());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                CommonFunctions.changeactivity(LoginActivity.this, DashboardActivity.class);
                            }
                        });
            }
        } catch (Exception e) {
            CommonFunctions.destroyProgressBar();
            e.printStackTrace();
        }
    }*/

    @OnClick(R.id.tv_sign_up)
    public void sigunupClick() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        intent.putExtra(Constants.from, getString(R.string.tit_signup));
        startActivity(intent);
    }

    /* @OnClick(R.id.tv_forgot)
    public void forgotClick() {
        Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
        intent.putExtra(Constants.from, getString(R.string.tit_forgt));
        startActivity(intent);
    }*/

    @Override
    public void onBackPressed() {
        finish();
    }
}