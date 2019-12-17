package com.q8munasabat.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.q8munasabat.R;
import com.q8munasabat.base.BaseActivity;
import com.q8munasabat.config.CommonFunctions;
import com.q8munasabat.config.Constants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LangActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_home)
    ImageView iv_home;
    Bundle bundle;
    String from = "";

    @BindView(R.id.tv_eng)
    TextView tv_eng;
    @BindView(R.id.tv_arabic)
    TextView tv_arabic;
    private static String lang = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        inticompnets();
        clicklistener();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_lang;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void clicklistener() {
        try {
            tv_arabic.setOnClickListener(this);
            tv_eng.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            tv_title.setText(from);

            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String strLang = defaultSharedPreferences.getString("lang", "en");
            String lang = CommonFunctions.getPreference(this, Constants.languageid, "0");
            if (!lang.equalsIgnoreCase("1")) {
                tv_eng.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0);
            }
            if (!lang.equalsIgnoreCase("0")) {
                tv_arabic.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeAppLanguage(String lang1) {

        if (lang1.equalsIgnoreCase("0")) {
            lang = "0";
        } else {
            lang = "1";
        }

        finish();
        CommonFunctions.setPreference(this, Constants.languageid, lang);
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        CommonFunctions.changeactivity(this, DashboardActivity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_arabic:
                changeAppLanguage("1");
                break;
            case R.id.tv_eng:
                changeAppLanguage("0");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        gotoback();
    }

    @OnClick({R.id.iv_home})
    public void gotoback() {
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}