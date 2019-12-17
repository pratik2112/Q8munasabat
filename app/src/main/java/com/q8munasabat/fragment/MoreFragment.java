package com.q8munasabat.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentTransaction;

import com.q8munasabat.R;
import com.q8munasabat.activity.DashboardActivity;
import com.q8munasabat.base.BaseFragment;
import com.q8munasabat.config.CommonFunctions;
import com.q8munasabat.config.Constants;

import java.util.Locale;

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
                break;
        }
    }
}