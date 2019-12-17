package com.q8munasabat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

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

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.tv_lang)
    TextView tv_lang;
    @BindView(R.id.ll_acunt)
    LinearLayout ll_acunt;
    @BindView(R.id.sw_share_phn)
    Switch sw_share_phn;
    View rootView;
    String lang;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
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
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
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
            ((DashboardActivity) getActivity()).setTitle(getString(R.string.tit_setting));
            ((DashboardActivity) getActivity()).shouldDisplayHomeUp(true);

            if (CommonFunctions.getPreference(getContext(), Constants.isLogin, false))
                ll_acunt.setVisibility(View.VISIBLE);
            else
                ll_acunt.setVisibility(View.GONE);

            if (lang.equals("0"))
                tv_lang.setText(getString(R.string.str_lang_e));
            else
                tv_lang.setText(getString(R.string.str_lang_a));

            getuserData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getuserData() {
        try {
            String membr_id = "";
            /*if (CommonFunctions.getloginresponse(getActivity()) != null)
                membr_id = CommonFunctions.getloginresponse(getActivity()).data.id;
            else*/
            membr_id = "";
            String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.userdetailURL;
            Map<String, String> mParams = new HashMap<>();
            mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
            mParams.put(Constants.languageid, lang);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.memberid, membr_id);
            mParams.put(Constants.data, String.valueOf(jsonObject));

            WebService webService = new WebService(url, new JSONObject(), false, getActivity());
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
                        /*UserDetailResponse userDetailResponse = gson.fromJson(result.toString(), UserDetailResponse.class);
                        if (userDetailResponse.data.sharephonenumber.equals("1"))
                            sw_share_phn.setChecked(true);
                        else
                            sw_share_phn.setChecked(false);*/
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

    @OnClick(R.id.tv_lang)
    public void langageClick() {
        try {
            /*Intent intent = new Intent(getActivity(), LangActivity.class);
            intent.putExtra(Constants.from, getString(R.string.tit_chos_lang));
            startActivity(intent);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_share)
    public void shareApp() {
        try {
            String shareMsg = getString(R.string.subject) + " \n ";
            String play_store = getString(R.string.google_url);
            String appName = getActivity().getPackageName();

            Intent linkIntent = new Intent(Intent.ACTION_SEND);
            linkIntent.setType("text/plain");
            linkIntent.putExtra(Intent.EXTRA_TEXT, shareMsg + play_store + appName);
            startActivity(Intent.createChooser(linkIntent, "Share Via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_chng_pwd)
    public void chngpwdClick() {
        try {
            /*FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.push_left_in,
                    R.anim.push_left_out,
                    R.anim.push_left_in,
                    R.anim.push_left_out);
            transaction.replace(R.id.frame_container, new ChangePasswordFragment());
            transaction.addToBackStack(null);
            transaction.commit();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.sw_share_phn)
    public void sharePhoneClick() {
        try {
            if (CommonFunctions.checkConnection(getActivity())) {
                String membr_id, switch_check = "";
               /* if (CommonFunctions.getloginresponse(getContext()) != null)
                    membr_id = CommonFunctions.getloginresponse(getContext()).data.id;
                else*/
                membr_id = "";
                if (sw_share_phn.isChecked())
                    switch_check = "1";
                else
                    switch_check = "0";
                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.updatephoneURL;
                Map<String, String> mParams = new HashMap<>();
                mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                mParams.put(Constants.languageid, lang);
                mParams.put(Constants.status, switch_check);
                mParams.put(Constants.member_id, membr_id);

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
                    }

                    @Override
                    public void OnFail(String error) {
                    }
                }, mParams);
            } else {
                sw_share_phn.setChecked(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_deactive)
    public void deactiveClick() {
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            dialogBuilder.setTitle(getString(R.string.str_are_u_sure));
            dialogBuilder.setMessage(getString(R.string.str_deactive_msg));
            dialogBuilder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (CommonFunctions.checkConnection(getActivity())) {
                        String membr_id = "";
                        /*if (CommonFunctions.getloginresponse(getContext()) != null)
                            membr_id = CommonFunctions.getloginresponse(getContext()).data.id;
                        else*/
                        membr_id = "";
                        String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.deactiveacuntURL;
                        Map<String, String> mParams = new HashMap<>();
                        mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                        mParams.put(Constants.languageid, lang);
                        mParams.put(Constants.member_id, membr_id);

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
                                CommonFunctions.setPreference(getActivity(), Constants.isLogin, false);
                                CommonFunctions.setPreference(getActivity(), Constants.userdata, "");
                                CommonFunctions.changeactivity(getActivity(), DashboardActivity.class);
                            }

                            @Override
                            public void OnFail(String error) {
                            }
                        }, mParams);
                    }
                }
            });
            dialogBuilder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialogBuilder.setCancelable(false);
            dialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}