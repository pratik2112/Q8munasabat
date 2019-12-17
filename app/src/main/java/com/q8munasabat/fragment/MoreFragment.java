package com.q8munasabat.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.q8munasabat.R;
import com.q8munasabat.activity.DashboardActivity;
import com.q8munasabat.base.BaseFragment;
import com.q8munasabat.config.CommonFunctions;
import com.q8munasabat.config.Constants;

import java.util.Locale;

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
            ((DashboardActivity) getActivity()).setTitle(getString(R.string.menu_title_5));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}