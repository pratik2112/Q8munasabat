package com.q8munasabat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.q8munasabat.R;
import com.q8munasabat.activity.DashboardActivity;
import com.q8munasabat.base.BaseFragment;
import com.q8munasabat.config.CommonFunctions;
import com.q8munasabat.config.Constants;
import com.q8munasabat.config.Q8MunasabatConfig;
import com.q8munasabat.config.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordFragment extends BaseFragment {

    @BindView(R.id.edt_old_pwd)
    EditText edt_old_pwd;
    @BindView(R.id.edt_new_pwd)
    EditText edt_new_pwd;
    @BindView(R.id.edt_conf_pwd)
    EditText edt_conf_pwd;
    View rootView;
    String lang;
    boolean isSocial = false;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context); // this statement is missing
        lang = CommonFunctions.getPreference(getActivity(), Constants.languageid, "0");
        Locale locale = new Locale(lang.equalsIgnoreCase("0") ? "en" : "ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public int getContentView() {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        inticompnets();
        return rootView;
    }

    private void inticompnets() {
        try {
            ((DashboardActivity) getActivity()).setTitle(getString(R.string.tit_chng_pwd));
            ((DashboardActivity) getActivity()).shouldDisplayHomeUp(true);

            if (!CommonFunctions.getuserdetailresponse(getContext()).data.social.equalsIgnoreCase("1")) {
                isSocial = true;
                edt_old_pwd.setVisibility(View.GONE);
            } else {
                isSocial = false;
                edt_old_pwd.setVisibility(View.VISIBLE);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.btn_submit)
    public void submitPwdClick() {
        try {
            if (edt_old_pwd.getText().toString().length() == 0 && !isSocial)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_old_pwd));
            else if (edt_new_pwd.getText().toString().length() == 0)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_new_pwd));
            else if (edt_new_pwd.getText().toString().length() < 6)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_pwd1));
            else if (edt_conf_pwd.getText().toString().length() == 0)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_conf_pwd));
            else if (edt_conf_pwd.getText().toString().length() < 6)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_conf_pwd1));
            else if (!(edt_new_pwd.getText().toString()).equals(edt_conf_pwd.getText().toString()))
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_mismatch));
            else
                changePassword();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changePassword() {
        try {
            if (CommonFunctions.checkConnection(getActivity())) {
                String membr_id = "";
                if (CommonFunctions.getloginresponse(getContext()) != null)
                    membr_id = CommonFunctions.getloginresponse(getContext()).data.id;
                else
                    membr_id = "";

                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.changeURL;
                Map<String, String> mParams = new HashMap<>();
                mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                mParams.put(Constants.languageid, lang);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.type, 1);
                jsonObject.put(Constants.id, membr_id);
                jsonObject.put(Constants.oldpassword, isSocial ? "" : CommonFunctions.convertutf(edt_old_pwd.getText().toString()));
                jsonObject.put(Constants.newpassword, CommonFunctions.convertutf(edt_new_pwd.getText().toString()));
                mParams.put(Constants.data, String.valueOf(jsonObject));

                WebService webService = new WebService(url, new JSONObject(), true, getContext());
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
                            if (result.contains(Constants.success)) {
                                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                alertDialog.setTitle(getString(R.string.app_name));
                                alertDialog.setMessage(getString(R.string.pwd_success));
                                alertDialog.setCancelable(false);
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.dialog_ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                ((DashboardActivity) getActivity()).addFragment(new SettingsFragment());
                                            }
                                        });
                                alertDialog.show();
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                alertDialog.setTitle(getString(R.string.app_name));
                                alertDialog.setMessage(getString(R.string.pwd_fail));
                                alertDialog.setCancelable(false);
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.dialog_ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
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
}