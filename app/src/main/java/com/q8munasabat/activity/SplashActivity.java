package com.q8munasabat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.q8munasabat.R;
import com.q8munasabat.config.CommonFunctions;
import com.q8munasabat.config.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {

    private static String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initcompnets();
    }

    private void initcompnets() {
        try {
            int SPLASH_TIME_OUT = 1500;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!CommonFunctions.getPreference(SplashActivity.this, Constants.isLogin, false)) {
                        Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                        intent.putExtra(Constants.from, getString(R.string.tit_home));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                        intent.putExtra(Constants.from, getString(R.string.tit_home));
                        startActivity(intent);
                    }
                }
            }, SPLASH_TIME_OUT);
            printHashKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e(TAG, "hashKey: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }
}