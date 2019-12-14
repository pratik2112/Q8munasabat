package com.q8munasabat.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

public class AdvertiseWithUsFragment extends BaseFragment {

    @BindView(R.id.edt_fname)
    EditText edt_fname;
    @BindView(R.id.edt_email)
    EditText edt_email;
    /*@BindView(R.id.ccp_cuntry_code)
    CountryCodePicker ccp_cuntry_code;*/
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    View rootView;
    String lang;
    //CheckExistResponse checkExistResponse;

    public static AdvertiseWithUsFragment newInstance() {
        return new AdvertiseWithUsFragment();
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
        rootView = inflater.inflate(R.layout.fragment_advertise_with_us, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        inticompnets();
        return rootView;
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

    private void inticompnets() {
        try {
            ((DashboardActivity) getActivity()).setTitle(getString(R.string.menu_title_7));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_submit)
    public void submitClick() {
        try {
            if (edt_fname.getText().toString().length() == 0)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_name));
            else if (edt_email.getText().toString().length() == 0)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_email));
            else if (edt_mobile.getText().toString().length() == 0 || edt_mobile.getText().toString().length() < 8)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_mobil));
            else
                advertiseUs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void advertiseUs() {
        try {
            if (CommonFunctions.checkConnection(getActivity())) {
                String membr_id = "";
               /* if (CommonFunctions.getloginresponse(getContext()) != null)
                    membr_id = CommonFunctions.getloginresponse(getContext()).data.id;
                else*/
                    membr_id = "";
                CommonFunctions.createProgressBar(getActivity(), getString(R.string.msg_please_wait));
                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.advertiseURL;
                Map<String, String> mParams = new HashMap<>();
                mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                mParams.put(Constants.languageid, lang);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.memberid, membr_id);
                jsonObject.put(Constants.name, edt_fname.getText().toString());
                jsonObject.put(Constants.mobileno, /*ccp_cuntry_code.getSelectedCountryCodeWithPlus()*/"+965" + edt_mobile.getText().toString());
                jsonObject.put(Constants.email, edt_email.getText().toString());
                mParams.put(Constants.data, String.valueOf(jsonObject));

                WebService webService = new WebService(url, new JSONObject(), true, getActivity());
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
                            CommonFunctions.destroyProgressBar();
                            Gson gson = new Gson();
                            /*checkExistResponse = gson.fromJson(result.toString(), CheckExistResponse.class);
                            showAlertDialog(getString(R.string.app_name), checkExistResponse.error);*/
                            ((DashboardActivity) getActivity()).addFragment(new HomeFragment());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnFail(String error) {
                        CommonFunctions.destroyProgressBar();
                    }
                }, mParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}