package com.q8munasabat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

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

public class MoreFragment extends BaseFragment implements View.OnClickListener {

    View rootView;
    String lang;

    @BindView(R.id.ll_my_ads)
    LinearLayout ll_my_ads;
    @BindView(R.id.ll_advterus)
    LinearLayout ll_advterus;
    @BindView(R.id.ll_notify)
    LinearLayout ll_notify;
    @BindView(R.id.ll_aboutus)
    LinearLayout ll_aboutus;
    @BindView(R.id.ll_tc)
    LinearLayout ll_tc;
    @BindView(R.id.ll_privacypolicy)
    LinearLayout ll_privacypolicy;
    @BindView(R.id.ll_setting)
    LinearLayout ll_setting;
    @BindView(R.id.ll_logout)
    LinearLayout ll_logout;

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
        clicklistener();
        return rootView;
    }

    private void inticompnets() {
        try {
            ((DashboardActivity) getActivity()).setTitle(getString(R.string.menu_title_5));

            if (CommonFunctions.getPreference(getActivity(), Constants.isLogin, false))
                ll_logout.setVisibility(View.VISIBLE);
            else ll_logout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clicklistener() {
        try {
            ll_my_ads.setOnClickListener(this);
            ll_advterus.setOnClickListener(this);
            ll_notify.setOnClickListener(this);
            ll_aboutus.setOnClickListener(this);
            ll_tc.setOnClickListener(this);
            ll_privacypolicy.setOnClickListener(this);
            ll_setting.setOnClickListener(this);
            ll_logout.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_my_ads:
                break;
            case R.id.ll_advterus:
                FragmentTransaction transAWU = getFragmentManager().beginTransaction();
                transAWU.setCustomAnimations(
                        R.anim.push_left_in,
                        R.anim.push_left_out,
                        R.anim.push_left_in,
                        R.anim.push_left_out);
                transAWU.replace(R.id.frame_container, new AdvertiseWithUsFragment());
                transAWU.addToBackStack(null);
                transAWU.commit();
                break;
            case R.id.ll_notify:
                FragmentTransaction transNotify = getFragmentManager().beginTransaction();
                transNotify.setCustomAnimations(
                        R.anim.push_left_in,
                        R.anim.push_left_out,
                        R.anim.push_left_in,
                        R.anim.push_left_out);
                transNotify.replace(R.id.frame_container, new NotificationFragment());
                transNotify.addToBackStack(null);
                transNotify.commit();
                break;
            case R.id.ll_aboutus:
                FragmentTransaction transAbout = getFragmentManager().beginTransaction();
                transAbout.setCustomAnimations(
                        R.anim.push_left_in,
                        R.anim.push_left_out,
                        R.anim.push_left_in,
                        R.anim.push_left_out);
                transAbout.replace(R.id.frame_container, new AboutusFragment());
                transAbout.addToBackStack(null);
                transAbout.commit();
                break;
            case R.id.ll_tc:
                FragmentTransaction transTerm = getFragmentManager().beginTransaction();
                transTerm.setCustomAnimations(
                        R.anim.push_left_in,
                        R.anim.push_left_out,
                        R.anim.push_left_in,
                        R.anim.push_left_out);
                transTerm.replace(R.id.frame_container, new TermsFragment());
                transTerm.addToBackStack(null);
                transTerm.commit();
                break;
            case R.id.ll_privacypolicy:
                FragmentTransaction transPrivacy = getFragmentManager().beginTransaction();
                transPrivacy.setCustomAnimations(
                        R.anim.push_left_in,
                        R.anim.push_left_out,
                        R.anim.push_left_in,
                        R.anim.push_left_out);
                transPrivacy.replace(R.id.frame_container, new PrivacyFragment());
                transPrivacy.addToBackStack(null);
                transPrivacy.commit();
                break;
            case R.id.ll_setting:
                FragmentTransaction transSet = getFragmentManager().beginTransaction();
                transSet.setCustomAnimations(
                        R.anim.push_left_in,
                        R.anim.push_left_out,
                        R.anim.push_left_in,
                        R.anim.push_left_out);
                transSet.replace(R.id.frame_container, new SettingsFragment());
                transSet.addToBackStack(null);
                transSet.commit();
                break;
            case R.id.ll_logout:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setTitle(getString(R.string.str_are_u_sure));
                dialogBuilder.setMessage(getString(R.string.str_logout));
                dialogBuilder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logoutUser();
                    }
                });
                dialogBuilder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialogBuilder.setCancelable(false);
                dialogBuilder.show();
                break;
        }
    }

    private void logoutUser() {
        try {
            if (CommonFunctions.checkConnection(getActivity())) {
                String membr_id = "";
                if (CommonFunctions.getloginresponse(getActivity()) != null)
                    membr_id = CommonFunctions.getloginresponse(getActivity()).data.id;

                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.logoutURL;
                Map<String, String> mParams = new HashMap<>();
                mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                mParams.put(Constants.languageid, lang);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.memberid, membr_id);
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
                            Gson gson = new Gson();
                            CommonFunctions.setPreference(getActivity(), Constants.isLogin, false);
                            CommonFunctions.setPreference(getActivity(), Constants.userdata, "");
                            CommonFunctions.changeactivity(getActivity(), DashboardActivity.class);
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