package com.q8munasabat.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.q8munasabat.R;
import com.q8munasabat.activity.DashboardActivity;
import com.q8munasabat.base.BaseFragment;
import com.q8munasabat.config.CommonFunctions;
import com.q8munasabat.config.Constants;

import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;
    @BindView(R.id.frame_container1)
    FrameLayout frameLayout;
    View rootView;
    Bundle b;
    private int curr_tabPosition;
    public JSONObject jsonFilter = new JSONObject();
    String lang;
    final long DELAY_MS = 3000;
    //AppBannerResponse appBannerResponse;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setHasOptionsMenu(false);
        //RecentFragment.ismenushow = false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(false);
        inticompnets();
        //((DashboardActivity)getActivity()).blacnkTitleBack();
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

    @Override
    public int getContentView() {
        return 0;
    }

    private void inticompnets() {
        try {
            ((DashboardActivity) getActivity()).setTitle(getString(R.string.tit_home));
            setTabFragmentAdapter();
            setupTabIcons();

            /*getBannerList();

            addFragment(new CategoryFragment());
            tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int textColor;
                    textColor = R.color.colorAccent;
                    curr_tabPosition = tab.getPosition();
                    switch (tab.getPosition()) {
                        case 0:
                            setSelectedTabChanges(tab, getString(R.string.tab_title_1), textColor);
                            setUnSelectedTabChanges(1);
                            setUnSelectedTabChanges(2);
                            addFragment(new CategoryFragment());
                            break;
                        case 1:
                            setSelectedTabChanges(tab, getString(R.string.tab_title_2), textColor);
                            setUnSelectedTabChanges(0);
                            setUnSelectedTabChanges(2);

                            RecentFragment recentFragment = new RecentFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.filterarray, jsonFilter.toString());
                            recentFragment.setArguments(bundle);
                            addFragment(recentFragment);
                            break;
                        case 2:
                            setSelectedTabChanges(tab, getString(R.string.tab_title_3), textColor);
                            setUnSelectedTabChanges(0);
                            setUnSelectedTabChanges(1);

                            MapHFragment mapHFragment = new MapHFragment();
                            Bundle bundlemap = new Bundle();
                            bundlemap.putString(Constants.filterarray, jsonFilter.toString());
                            mapHFragment.setArguments(bundlemap);
                            addFragment(mapHFragment);
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    int textColor = R.color.addresstype_bg2;
                    switch (tab.getPosition()) {
                        case 0:
                            setSelectedTabChanges(tab, getString(R.string.tab_title_1), textColor);
                            break;
                        case 1:
                            setSelectedTabChanges(tab, getString(R.string.tab_title_2), textColor);
                            break;
                        case 2:
                            setSelectedTabChanges(tab, getString(R.string.tab_title_3), textColor);
                            break;
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    Log.d("onTabReselected", "onTabReselected");

                    int textColor;
                    textColor = R.color.red;
                    curr_tabPosition = tab.getPosition();
                    switch (tab.getPosition()) {
                        case 0:
                            setSelectedTabChanges(tab, getString(R.string.tab_title_1), textColor);
                            setUnSelectedTabChanges(1);
                            setUnSelectedTabChanges(2);
                            addFragment(new CategoryFragment());
                            break;
                    }
                }
            });*/
        } catch (Exception e) {
            Log.d("main fragment", "" + e.toString());
        }
    }

    /*private void getBannerList() {
        try {
            String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.appBanerURL;
            Map<String, String> mParams = new HashMap<>();
            mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
            mParams.put(Constants.languageid, lang);

            WebService webService = new WebService(url, new JSONObject(), false, getContext());
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
                        appBannerResponse = gson.fromJson(result.toString(), AppBannerResponse.class);
                        setBannerAdapter();
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

    private void setBannerAdapter() {
        try {
            for (int i = 0; i < appBannerResponse.data.size(); i++) {
                CCImageAdapter mAdapter = new CCImageAdapter(appBannerResponse.data, getActivity(), bannerClick);
                info_view_pager.setAdapter(mAdapter);
                info_view_pager.setInterval(DELAY_MS);
                info_view_pager.setCycle(true);
                info_view_pager.setStopScrollWhenTouch(true);
                info_view_pager.startAutoScroll();

                info_view_pager.setClipToPadding(false);
                info_view_pager.setPadding(0, 0, 0, 0);
                info_view_pager.setPageMargin(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    CCImageAdapter.AdapterCallback bannerClick = new CCImageAdapter.AdapterCallback() {
        @Override
        public void onBannerClick(String bannerUrl) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bannerUrl));
                startActivity(browserIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };*/

    public void setTabFragmentAdapter() {
        for (int i = 0; i < 3; i++) {
            tab_layout.addTab(tab_layout.newTab());
        }
    }

    public void setupTabIcons() {
        View tabOne = (View) LayoutInflater.from(getContext()).inflate(R.layout.tab_title, null);
        TextView txtOne = (TextView) tabOne.findViewById(R.id.tv_title);
        txtOne.setText(getString(R.string.tab_title_1));
        txtOne.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        tab_layout.getTabAt(0).setCustomView(tabOne);

        View tabTwo = (View) LayoutInflater.from(getContext()).inflate(R.layout.tab_title, null);
        TextView txtTwo = (TextView) tabTwo.findViewById(R.id.tv_title);
        txtTwo.setText(getString(R.string.tab_title_2));
        txtTwo.setTextColor(ContextCompat.getColor(getContext(), R.color.addresstype_bg2));
        tab_layout.getTabAt(1).setCustomView(tabTwo);

        View tabThree = (View) LayoutInflater.from(getContext()).inflate(R.layout.tab_title, null);
        TextView txtThree = (TextView) tabThree.findViewById(R.id.tv_title);
        txtThree.setText(getString(R.string.tab_title_3));
        txtThree.setTextColor(ContextCompat.getColor(getContext(), R.color.addresstype_bg2));
        tab_layout.getTabAt(2).setCustomView(tabThree);
    }

    public void setSelectedTabChanges(TabLayout.Tab tab, String name, int color) {
        View view = tab.getCustomView();
        assert view != null;
        view.setAlpha(1);
        TextView tabOne = (TextView) view.findViewById(R.id.tv_title);
        tabOne.setTextColor(ContextCompat.getColor(getContext(), color));
        tabOne.setText(name);
    }

    public void setUnSelectedTabChanges(int pos) {
        View view = tab_layout.getTabAt(pos).getCustomView();
        assert view != null;
        view.setAlpha(1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                b = data.getExtras();
                if (requestCode == Constants.reqcode501) {
                    if (b.containsKey(Constants.filterarray)) {
                        jsonFilter = new JSONObject(data.getStringExtra(Constants.filterarray));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFragment(Fragment myfragement) {
        try {
            /*getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, myfragement, null);
            ft.commitAllowingStateLoss();*/
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.push_left_in,
                    R.anim.push_left_out,
                    R.anim.push_left_in,
                    R.anim.push_left_out);
            transaction.replace(R.id.frame_container1, myfragement);
            transaction.disallowAddToBackStack();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}