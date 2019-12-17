package com.q8munasabat.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.q8munasabat.R;
import com.q8munasabat.base.BaseActivity;
import com.q8munasabat.config.CommonFunctions;
import com.q8munasabat.config.Constants;
import com.q8munasabat.config.Logger;
import com.q8munasabat.fragment.HomeFragment;
import com.q8munasabat.fragment.MoreFragment;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.frame_container)
    FrameLayout frameLayout;

    String lang, languageCode, from = "";
    Bundle bundle;
    boolean doubleBackToExitPressedOnce = false;
    private SharedPreferences preferences;
    double latitude_cur, longitude_cur;
    private int curr_tabPosition;

    @Override
    protected int getContentView() {
        lang = CommonFunctions.getPreference(this, Constants.languageid, "0");
        Locale locale = new Locale(lang.equalsIgnoreCase("0") ? "en" : "ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        return R.layout.activity_dashboard;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        setSupportActionBar(toolbar);
        changeSystemLanguage();
        inticompnets();
    }

    private void changeSystemLanguage() {
        if (CommonFunctions.getPreference(this, Constants.languageid, "0").equalsIgnoreCase("0")) {
            languageCode = "0";
        } else {
            languageCode = "1";
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        android.content.res.Configuration conf = getResources().getConfiguration();
        conf.locale = new Locale(languageCode);
        getResources().updateConfiguration(conf, dm);
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
            //toolbar.setTitle(from);
            toolbar_title.setText(from);

            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            final String strLang = preferences.getString(Constants.languageid, "0");

            if (!CommonFunctions.getPreference(DashboardActivity.this, Constants.isLocationDenied, false))
                checkLocationPermission();

            //changeLang();
            setTabFragmentAdapter();
            setupTabIcons();
            addFragment(new HomeFragment());

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int textColor;
                    textColor = R.color.red;
                    curr_tabPosition = tab.getPosition();
                    switch (tab.getPosition()) {
                        case 0:
                            setSelectedTabChanges(tab, R.drawable.ic_s_home, textColor);
                            setUnSelectedTabChanges(1);
                            setUnSelectedTabChanges(2);
                            setUnSelectedTabChanges(3);
                            setUnSelectedTabChanges(4);
                            addFragment(new HomeFragment());
                            break;
                        case 1:
                            if (CommonFunctions.getPreference(DashboardActivity.this, Constants.isLogin, false)) {
                                setSelectedTabChanges(tab, R.drawable.ic_s_fav, textColor);
                                setUnSelectedTabChanges(0);
                                setUnSelectedTabChanges(2);
                                setUnSelectedTabChanges(3);
                                setUnSelectedTabChanges(4);
                                addFragment(new HomeFragment()); // FavFragment
                            } else {
                                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                                intent.putExtra(Constants.from, getString(R.string.tit_login));
                                tabLayout.getTabAt(0).select();
                                startActivity(intent);
                            }
                            break;
                        case 2:
                            if (CommonFunctions.getPreference(DashboardActivity.this, Constants.isLogin, false)) {
                                setSelectedTabChanges(tab, R.drawable.ic_s_ads, textColor);
                                setUnSelectedTabChanges(0);
                                setUnSelectedTabChanges(1);
                                setUnSelectedTabChanges(3);
                                setUnSelectedTabChanges(4);
                                addFragment(new HomeFragment()); // PoseEventFragment
                            } else {
                                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                                intent.putExtra(Constants.from, getString(R.string.tit_login));
                                tabLayout.getTabAt(0).select();
                                startActivity(intent);
                            }
                            break;
                        case 3:
                            if (CommonFunctions.getPreference(DashboardActivity.this, Constants.isLogin, false)) {
                                setSelectedTabChanges(tab, R.drawable.ic_s_profile, textColor);
                                setUnSelectedTabChanges(0);
                                setUnSelectedTabChanges(1);
                                setUnSelectedTabChanges(2);
                                setUnSelectedTabChanges(4);
                                addFragment(new HomeFragment()); //ProfileFragment
                            } else {
                                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                                intent.putExtra(Constants.from, getString(R.string.tit_login));
                                tabLayout.getTabAt(0).select();
                                startActivity(intent);
                            }
                            break;
                        case 4:
                            setSelectedTabChanges(tab, R.drawable.ic_s_more, textColor);
                            setUnSelectedTabChanges(0);
                            setUnSelectedTabChanges(1);
                            setUnSelectedTabChanges(2);
                            setUnSelectedTabChanges(3);
                            addFragment(new MoreFragment());
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    int textColor = R.color.white;
                    switch (tab.getPosition()) {
                        case 0:
                            setSelectedTabChanges(tab, R.drawable.ic_home, textColor);
                            break;
                        case 1:
                            setSelectedTabChanges(tab, R.drawable.ic_fav, textColor);
                            break;
                        case 2:
                            setSelectedTabChanges(tab, R.drawable.ic_ads, textColor);
                            break;
                        case 3:
                            setSelectedTabChanges(tab, R.drawable.ic_profile, textColor);
                            break;
                        case 4:
                            setSelectedTabChanges(tab, R.drawable.ic_more, textColor);
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
                            setSelectedTabChanges(tab, R.drawable.ic_s_home, textColor);
                            setUnSelectedTabChanges(1);
                            setUnSelectedTabChanges(2);
                            setUnSelectedTabChanges(3);
                            setUnSelectedTabChanges(4);
                            addFragment(new HomeFragment());
                            break;
                    }
                }
            });

            getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
                    } else {
                        //show hamburger
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        // toolbar.setTitle("");
                    }
                }
            });
        } catch (Exception e) {
            Log.d("main activity", "" + e.toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void checkLocationPermission() {
        try {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                //Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            }

                            if (report.getDeniedPermissionResponses().size() > 0) {
                                CommonFunctions.setPreference(DashboardActivity.this, Constants.isLocationDenied, true);
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {

                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).
                    withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {
                            Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .onSameThread()
                    .check();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (f != null && f instanceof HomeFragment) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // toolbar.setTitle("");
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.msg_press_back_twice), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                if (f != null && !(f instanceof HomeFragment)) {
                    Intent intent = new Intent("onresume");
                    intent.putExtra(Constants.from, getCurrentFragment(f));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    // toolbar.setTitle("");
                }
                tabLayout.getTabAt(0).select();
            }
            gotoHome();
        }
    }

    public int getCurrentFragment(Fragment fragment) {
        int currentFragment = 0;
        try {
            if (fragment instanceof HomeFragment) {
                currentFragment = 1;
            } else if (fragment instanceof HomeFragment) {
                currentFragment = 1;
            } else if (fragment instanceof HomeFragment) {
                currentFragment = 2;
            } else if (fragment instanceof HomeFragment) {
                currentFragment = 3;
            } else if (fragment instanceof MoreFragment) {
                currentFragment = 4;
            }
        } catch (Exception e) {
            Logger.debugE(e.toString());
        }
        return currentFragment;
    }

    public void setSelectedTabChanges(TabLayout.Tab tab, int drawable, int color) {
        View view = tab.getCustomView();
        assert view != null;
        view.setAlpha(1);
        TextView tabOne = (TextView) view.findViewById(R.id.tv_title);
        tabOne.setTextColor(ContextCompat.getColor(DashboardActivity.this, color));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(null, setDrawable(drawable), null, null);
    }

    public TabLayout.Tab gettabview(int position) {
        return tabLayout.getTabAt(position);
    }

    public TabLayout gettabview1() {
        return tabLayout;
    }

    public void setUnSelectedTabChanges(int pos) {
        View view = tabLayout.getTabAt(pos).getCustomView();
        assert view != null;
        view.setAlpha(1);
    }

    public void setTabFragmentAdapter() {
        for (int i = 0; i < 5; i++) {
            tabLayout.addTab(tabLayout.newTab());
        }
    }

    public void setupTabIcons() {
        Locale locale = new Locale(lang.equalsIgnoreCase("0") ? "en" : "ar");

        View tabOne = (View) LayoutInflater.from(DashboardActivity.this).inflate(R.layout.slide_tablayout, null);
        TextView txtOne = (TextView) tabOne.findViewById(R.id.tv_title);
        String langOne = getLocaleStringResource(locale, R.string.menu_title_1, this);
        txtOne.setText(langOne);
        txtOne.setTextColor(ContextCompat.getColor(this, R.color.red));
        txtOne.setCompoundDrawablesWithIntrinsicBounds(null, setDrawable(R.drawable.ic_s_home), null, null);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        View tabTwo = (View) LayoutInflater.from(DashboardActivity.this).inflate(R.layout.slide_tablayout, null);
        TextView txtTwo = (TextView) tabTwo.findViewById(R.id.tv_title);
        String langTwo = getLocaleStringResource(locale, R.string.menu_title_2, this);
        txtTwo.setText(langTwo);
        txtTwo.setCompoundDrawablesWithIntrinsicBounds(null, setDrawable(R.drawable.ic_fav), null, null);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        View tabThree = (View) LayoutInflater.from(DashboardActivity.this).inflate(R.layout.slide_tablayout, null);
        TextView txtThree = (TextView) tabThree.findViewById(R.id.tv_title);
        String langThree = getLocaleStringResource(locale, R.string.menu_title_3, this);
        txtThree.setText(langThree);
        txtThree.setCompoundDrawablesWithIntrinsicBounds(null, setDrawable(R.drawable.ic_ads), null, null);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        View tabFour = (View) LayoutInflater.from(DashboardActivity.this).inflate(R.layout.slide_tablayout, null);
        TextView txtFour = (TextView) tabFour.findViewById(R.id.tv_title);
        String langFour = getLocaleStringResource(locale, R.string.menu_title_4, this);
        txtFour.setText(langFour);
        txtFour.setCompoundDrawablesWithIntrinsicBounds(null, setDrawable(R.drawable.ic_profile), null, null);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        View tabFive = (View) LayoutInflater.from(DashboardActivity.this).inflate(R.layout.slide_tablayout, null);
        TextView txtFive = (TextView) tabFive.findViewById(R.id.tv_title);
        String langFive = getLocaleStringResource(locale, R.string.menu_title_5, this);
        txtFive.setText(langFive);
        txtFive.setCompoundDrawablesWithIntrinsicBounds(null, setDrawable(R.drawable.ic_more), null, null);
        tabLayout.getTabAt(4).setCustomView(tabFive);
    }

    public static String getLocaleStringResource(Locale requestedLocale, int resourceId, Activity context) {
        String result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { // use latest api
            Configuration config = new Configuration(context.getResources().getConfiguration());
            config.setLocale(requestedLocale);
            result = context.createConfigurationContext(config).getText(resourceId).toString();
        } else { // support older android versions
            Resources resources = context.getResources();
            Configuration conf = resources.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = requestedLocale;
            resources.updateConfiguration(conf, null);
            // retrieve resources from desired locale
            result = resources.getString(resourceId);
            // restore original locale
            conf.locale = savedLocale;
            resources.updateConfiguration(conf, null);
        }
        return result;
    }

    public Drawable setDrawable(int resourceId) {
        Drawable drawable = this.getResources().getDrawable(resourceId);
        return drawable;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void shouldDisplayHomeUp(boolean action) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(action);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }

    public void addFragment(Fragment myfragement) {
        try {
            DashboardActivity.this.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = DashboardActivity.this.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, myfragement, null);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoHome() {
        try {
            getSupportFragmentManager().popBackStack();
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_container);
            if (curr_tabPosition == 0) {
                int textColor = R.color.red;
                setSelectedTabChanges(gettabview(curr_tabPosition), R.drawable.ic_s_home, textColor);
                gettabview1().setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.red));//put your color
                toolbar_title.setText(from);
            } else if (curr_tabPosition == 1) {
                int textColor = R.color.red;
                setSelectedTabChanges(gettabview(curr_tabPosition), R.drawable.ic_s_fav, textColor);
                gettabview1().setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.red));//put your color
            } else if (curr_tabPosition == 2) {
                int textColor = R.color.red;
                setSelectedTabChanges(gettabview(curr_tabPosition), R.drawable.ic_s_ads, textColor);
                gettabview1().setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.red));//put your color
            } else if (curr_tabPosition == 3) {
                int textColor = R.color.red;
                setSelectedTabChanges(gettabview(curr_tabPosition), R.drawable.ic_s_profile, textColor);
                gettabview1().setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.red));//put your color
            } else if (curr_tabPosition == 4) {
                int textColor = R.color.red;
                setSelectedTabChanges(gettabview(curr_tabPosition), R.drawable.ic_s_more, textColor);
                gettabview1().setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.red));//put your color
            }
        } catch (Exception e) {
            Logger.debugE(e.toString());
        }
    }

    public void setTitle(String title) {
        try {
            toolbar_title.setText(title.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}