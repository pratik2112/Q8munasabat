package com.q8munasabat.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

public class NotificationFragment extends BaseFragment {

    View rootView;
    @BindView(R.id.rv_category)
    RecyclerView rv_category;
    @BindView(R.id.tv_empty)
    TextView tv_empty;

    /*NotificationResponse notificationResponse;
    NotificationAdapter notificationAdapter;*/
    String lang;

    @Override
    public int getContentView() {
        return 0;
    }

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        inticompnets();
        return rootView;
    }

    private void inticompnets() {
        try {
            ((DashboardActivity) getActivity()).setTitle(getString(R.string.menu_title_8));
            ((DashboardActivity) getActivity()).shouldDisplayHomeUp(true);
            getTranscation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTranscation() {
        try {
            if (CommonFunctions.checkConnection(getActivity())) {
                String membr_id = "";
                /*if (CommonFunctions.getloginresponse(getContext()) != null)
                    membr_id = CommonFunctions.getloginresponse(getContext()).data.id;
                else*/
                membr_id = "";
                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.getnotoficationuser;
                Map<String, String> mParams = new HashMap<>();
                mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                mParams.put(Constants.languageid, lang);
                mParams.put(Constants.data, new JSONObject().put(Constants.memberid, membr_id).toString());

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
                            /*notificationResponse = gson.fromJson(result.toString(), NotificationResponse.class);
                            setadapterTrans(notificationResponse);*/
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

    /*private void setadapterTrans(NotificationResponse transcationResponse) {
        try {
            if (transcationResponse.data == null || transcationResponse.data.size() == 0) {
                tv_empty.setVisibility(View.VISIBLE);
                tv_empty.setText(getString(R.string.no_record_found));
                rv_category.setVisibility(View.GONE);
            } else {
                tv_empty.setVisibility(View.GONE);
                rv_category.setVisibility(View.VISIBLE);
                notificationAdapter = new NotificationAdapter(getContext(), transcationResponse, null);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rv_category.setLayoutManager(mLayoutManager);
                rv_category.setItemAnimator(new DefaultItemAnimator());
                rv_category.setAdapter(notificationAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}