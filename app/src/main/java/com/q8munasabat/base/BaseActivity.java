/*
 * Copyright (c) 2016 Filippo Engidashet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.q8munasabat.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.q8munasabat.R;
import com.q8munasabat.backstack.BackStackEntry;
import com.q8munasabat.backstack.BackStackManager;
import com.q8munasabat.config.CommonFunctions;
import com.q8munasabat.config.Constants;

import java.util.Locale;

import butterknife.ButterKnife;

/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 9/24/2016
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    private static final String STATE_BACK_STACK_MANAGER = "back_stack_manager";
    protected BackStackManager backStackManager;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backStackManager = new BackStackManager();
        String lang = CommonFunctions.getPreference(this, Constants.languageid, "0");
        Locale locale = new Locale(lang.equalsIgnoreCase("0") ? "en" : "ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        setContentView(getContentView());
        ButterKnife.bind(this);
        onViewReady(savedInstanceState, getIntent());
    }

    @CallSuper
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        resolveDaggerDependency();
        //To be used by child activities
    }

    @Override
    protected void onDestroy() {
        ButterKnife.bind(this);
        backStackManager = null;

        //updatestatususer(Constants.offline.toString());

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_BACK_STACK_MANAGER, backStackManager.saveState());
    }

    @SuppressLint("WrongConstant")
    protected LinearLayoutManager getLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return layoutManager;
    }

    //Backstack management
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        backStackManager.restoreState(savedInstanceState.getParcelable(STATE_BACK_STACK_MANAGER));
    }

    protected boolean pushFragmentToBackStack(int hostId, @NonNull Fragment fragment) {
        try {
            BackStackEntry entry = BackStackEntry.create(getSupportFragmentManager(), fragment);
            backStackManager.push(hostId, entry);
            return true;
        } catch (Exception e) {
            Log.e("MultiBackStack", "Failed to add fragment to back stack", e);
            return false;
        }
    }

    @Nullable
    protected Fragment popFragmentFromBackStack(int hostId) {
        BackStackEntry entry = backStackManager.pop(hostId);
        return entry != null ? entry.toFragment(this) : null;
    }

    @Nullable
    protected Pair<Integer, Fragment> popFragmentFromBackStack() {
        Pair<Integer, BackStackEntry> pair = backStackManager.pop();
        return pair != null ? Pair.create(pair.first, pair.second.toFragment(this)) : null;
    }

    /**
     * @return false if back stack is missing.
     */
    protected boolean resetBackStackToRoot(int hostId) {
        return backStackManager.resetToRoot(hostId);
    }

    /**
     * @return false if back stack is missing.
     */
    protected boolean clearBackStack(int hostId) {
        return backStackManager.clear(hostId);
    }

    @Override
    public void noInternetConnectionAvailable() {
        showToast(getString(R.string.noNetworkFound));
    }

    protected void resolveDaggerDependency() {

    }

    @Override
    protected void onResume() {
        //updatestatususer(Constants.online.toString());
        super.onResume();
    }

    @Override
    protected void onPause() {
        //updatestatususer(Constants.offline.toString());
        super.onPause();
    }

    /*private void updatestatususer(String status) {
        try {
            if (CommonFunctions.getPreference(this, Constants.isLogin, false)) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.e("firebase signup done", "done");
                if (mUser != null) {
                    DatabaseReference updateData = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(mUser.getUid()).child(Constants.credentials);
                    if (updateData != null)
                        updateData.child(Constants.status).setValue(status);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    protected void showBackArrow() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public void openActivity(Class<?> calledActivity) {
        Intent myIntent = new Intent(this, calledActivity);
        this.startActivity(myIntent);
    }

    @Override
    public void showProgressDialog(String title, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            if (title != null)
                mProgressDialog.setTitle(title);
            mProgressDialog.setIcon(R.mipmap.ic_launcher);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
        }
        if (!mProgressDialog.isShowing()) {
            if (message != null)
                mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void showAlertDialog(String title, String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        if (title != null)
            dialogBuilder.setTitle(title);
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogBuilder.setCancelable(false);
        dialogBuilder.show();
    }

    protected AlertDialog.Builder showConfirmationDialog(String title, String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setMessage(msg);

        dialogBuilder.setCancelable(false);
        return dialogBuilder;
    }

    protected void showToast(String mToastMsg) {
        Toast.makeText(this, mToastMsg, Toast.LENGTH_LONG).show();
    }

    protected abstract int getContentView();
}