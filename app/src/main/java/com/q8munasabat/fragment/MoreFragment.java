package com.q8munasabat.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class MoreFragment extends BaseFragment {

    View rootView;
    String lang;

    public static MoreFragment newInstance() {
        return new MoreFragment();
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
    public int getContentView() {
        return 0;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        inticompnets();
        return rootView;
    }

    private void inticompnets() {
        try {
            ((DashboardActivity) getActivity()).setTitle(getString(R.string.menu_title_10));
            getTermsContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String membr_id;

    private void getTermsContent() {
        try {
            if (CommonFunctions.checkConnection(getActivity())) {
                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.getmanagecontent;
                Map<String, String> mParams = new HashMap<>();
                mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                mParams.put(Constants.languageid, lang);
                /*if (CommonFunctions.getloginresponse(getContext()) != null)
                    membr_id = CommonFunctions.getloginresponse(getContext()).data.id;
                else*/
                    membr_id = "";
                mParams.put(Constants.member_id, membr_id);
                JSONObject data = new JSONObject();
                data.put(Constants.slug, Constants.tc);
                mParams.put(Constants.data, data.toString());
                WebService webService = new WebService(url, new JSONObject(), true, getContext());
                webService.getData(new WebService.OnResult() {
                    @Override
                    public void OnSuccess(JSONObject result) {
                        Log.e("", result.toString());
                    }

                    @Override
                    public void OnSuccess(JSONArray result) {
                        Log.e("", result.toString());
                    }

                    @Override
                    public void OnSuccess(String result) {
                        try {
                            Gson gson = new Gson();
                            JSONObject dataresult = new JSONObject(result);

                            if (dataresult.getString(Constants.result).equalsIgnoreCase(Constants.success)) {
                                /*if (lang.equalsIgnoreCase("0"))
                                    tv_manage_content.setText(Html.fromHtml(dataresult.getJSONObject(Constants.data).getString(Constants.description)));
                                else
                                    tv_manage_content.setText(Html.fromHtml(dataresult.getJSONObject(Constants.data).getString(Constants.arabicdescription)));*/
                            } else
                                showAlertDialog(getString(R.string.app_name), "Something went wrong, please try again later.");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnFail(String error) {
                        Log.e("", error.toString());
                    }
                }, mParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}