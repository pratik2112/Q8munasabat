package com.q8munasabat.config;
/**
 * Created by navneet on 29-Sep-17.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nispok.snackbar.Snackbar;
import com.q8munasabat.R;
import com.q8munasabat.reciver.ConnectivityReceiver;
import com.q8munasabat.utills.Exiter;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.WIFI_SERVICE;
import static com.q8munasabat.config.Constants.BITMAP_SCALE;
import static com.q8munasabat.config.Constants.BLUR_RADIUS;

public class CommonFunctions {
    public static String errMessage = "";
    static String tag = "CommonFunctions :";
    static Toast toast;
    /**
     * Create Progress bar
     */
    static ProgressDialog pd;
    public static boolean slide_act_flag = true;

    /**
     * Check Internet connection available or not
     *
     * @return
     */
    public static boolean checkConnection(Activity activity) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected)
            showDialog(activity, activity.getResources().getString(R.string.msg_NO_INTERNET_RESPOND));
        return isConnected;

    }

    public static void showToast(Context context, String msg) {
        try {
            if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Showing the status in Snackbar
    public static void showSnack(Activity activity, String errMessage) {
        Snackbar.with(activity) // context
                .text(errMessage) // text to display
                .textColor(Color.RED)
                .color(Color.BLACK)
                .show(activity); // activity where it is displayed
    }

    public static String getIpaddress(Activity activity) {
        String ipAddress = "";
        try {
            WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
            ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    public static String getDeviceInfo(Activity activity) {
        String dm = "";
        try {
            dm = getDeviceMenufacture() + " " + getDeviceModel() + " " + getDeviceOSVersion() + " " + getDeviceOEMBuildNumber() + " " + getDeviceSerialNumber() + " " + applicationVersionName(activity) + " " + applicationVersion(activity) + " " + System.currentTimeMillis();
            return dm;
        } catch (Exception e) {
            e.printStackTrace();
            return dm;
        }
    }

    public static String e(String data) {
        String dm = "";
        try {
            // Sending side
            byte[] data1 = data.getBytes("UTF-8");
            dm = Base64.encodeToString(data1, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return dm;
        }
        return dm;
    }

    public static String d(String data) {
        String dm = "";
        try {
            // Sending side
            byte[] data1 = Base64.decode(data, Base64.DEFAULT);
            dm = new String(data1, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return dm;
        }
        return dm;
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        java.io.BufferedReader bufferedReader = new java.io.BufferedReader(
                new java.io.InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }

    public static boolean isNetworkAvailable(Context mContext) {
        android.net.ConnectivityManager connec = (android.net.ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            // MESSAGE TO SCREEN FOR TESTING (IF REQ)
            // Toast.makeText(this, connectionType + " connected",
            // Toast.LENGTH_SHORT).show();
            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            // System.out.println("Not Connected");
            return false;
        }
        return false;
    }

    /**
     * Convert Stream to String
     *
     * @param is
     * @return
     * @throws Exception
     */
    public static String convertStreamToString(InputStream is) throws Exception {
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is,
                "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }

    /**
     * Create Directories
     *
     * @param filePath
     * @return
     */
    public static boolean createDirs(String filePath) {
        Logger.debugE(filePath.substring(0, filePath.lastIndexOf("/")));
        java.io.File f = new java.io.File(filePath.substring(0, filePath.lastIndexOf("/")));
        Logger.debugE(f.mkdirs() + "");
        return f.mkdirs();
    }

    public static void showAlert(Context c, String title, String msg) {
        new Builder(c, R.style.AlertDialogCustom)
                .setMessage(msg)
                .setTitle(title)
                .setPositiveButton(c.getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        }).show();
    }

    @android.annotation.SuppressLint("NewApi")
    public static void showAlert(Context c, String title, String msg, int icon) {
        new Builder(c, android.app.AlertDialog.THEME_HOLO_LIGHT)
                .setMessage(msg)
                .setIcon(icon)
                .setTitle(title)
                .setPositiveButton(c.getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        }).show();
    }

    public static void showAlert(Context c, String title, String msg, int icon,
                                 DialogInterface.OnClickListener listener) {
        new Builder(c).setMessage(msg).setIcon(icon)
                .setTitle(title).setCancelable(false)
                .setPositiveButton(c.getString(android.R.string.ok), listener).show();
    }

    public static void showAlert(Context c, String title, String msg, int icon,
                                 DialogInterface.OnClickListener listener,
                                 DialogInterface.OnClickListener cancelListener) {
        new Builder(c)
                .setMessage(msg)
                .setIcon(icon)
                .setTitle(title)
                .setPositiveButton(c.getString(android.R.string.ok), listener)
                .setNegativeButton(c.getString(android.R.string.cancel), cancelListener)
                .show();
    }

    public static void showAlert(Context c, String title, String msg, int icon,
                                 DialogInterface.OnClickListener listener,
                                 DialogInterface.OnClickListener cancelListener, boolean cancalable) {
        new Builder(c)
                .setMessage(msg)
                .setIcon(icon)
                .setTitle(title)
                .setPositiveButton(c.getString(android.R.string.ok), listener)
                .setCancelable(cancalable)
                .setNegativeButton(c.getString(android.R.string.cancel), cancelListener)
                .show();
    }

    public static void showAlert(Context c, String title, String msg, int icon,
                                 DialogInterface.OnClickListener listener, boolean cancalable) {
        new Builder(c).setMessage(msg).setIcon(icon)
                .setTitle(title)
                .setPositiveButton(c.getString(android.R.string.ok), listener)
                .setCancelable(cancalable).show();
    }

    public static void setPreference(Context c, String pref, boolean val) {
        android.content.SharedPreferences.Editor e = android.preference.PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putBoolean(pref, val);
        e.commit();
    }

    public static boolean getPreference(Context context, String pref,
                                        boolean def) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(pref, def);
    }

    public static void setPreference(Context c, String pref, int val) {
        android.content.SharedPreferences.Editor e = android.preference.PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putInt(pref, val);
        e.commit();
    }

    public static int getPreference(Context context, String pref, int def) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context).getInt(
                pref, def);
    }

    public static void setPreference(Context c, String pref, float val) {
        android.content.SharedPreferences.Editor e = android.preference.PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putFloat(pref, val);
        e.commit();
    }

    public static float getPreference(Context context, String pref, float def) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context).getFloat(
                pref, def);
    }

    public static void setPreference(Context c, String pref, long val) {
        android.content.SharedPreferences.Editor e = android.preference.PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putLong(pref, val);
        e.commit();
    }

    public static long getPreference(Context context, String pref, long def) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context).getLong(
                pref, def);
    }

    public static void setPreference(Context c, String pref, double val) {
        android.content.SharedPreferences.Editor e = android.preference.PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putLong(pref, Double.doubleToLongBits(val));
        e.commit();
    }

    public static double getPreference(Context context, String pref, double def) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context).getLong(
                pref, Double.doubleToLongBits(def));
    }

    public static void setPreference(Context c, String pref, String val) {
        android.content.SharedPreferences.Editor e = android.preference.PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putString(pref, val);
        e.commit();
    }

    public static String getPreference(Context context, String pref, String def) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context)
                .getString(pref, def);
    }

    public static String title(String string) {
        String ret = "";
        StringBuffer sb = new StringBuffer();
        Matcher match = Pattern.compile("([a-z])([a-z]*)",
                Pattern.CASE_INSENSITIVE).matcher(string);
        while (match.find()) {
            match.appendReplacement(sb, match.group(1).toUpperCase()
                    + match.group(2).toLowerCase());
        }
        ret = match.appendTail(sb).toString();
        return ret;
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }

    /**
     * Enables/Disables all child views in a view group.
     *
     * @param viewGroup the view group
     * @param enabled   <code>true</code> to enable, <code>false</code> to disable the
     *                  views.
     */
    public static void enableDisableViewGroup(android.view.ViewGroup viewGroup,
                                              boolean enabled, View[] exceptionalViews) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            for (int j = 0; j < exceptionalViews.length; j++)
                if (view.getId() == exceptionalViews[j].getId()) {
                    view.setEnabled(true);
                    break;
                }
            if (view instanceof android.view.ViewGroup) {
                enableDisableViewGroup((android.view.ViewGroup) view, enabled,
                        exceptionalViews);
            }
        }
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = null;
        try {
            dialog = new ProgressDialog(mContext, R.style.MyProgressDialog);
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setProgressDrawable((new ColorDrawable(Color.GRAY)));
            dialog.setContentView(R.layout.custom_dialog_progresslayout);
        } catch (WindowManager.BadTokenException e) {
            dialog = new ProgressDialog(mContext);
        } catch (Exception e) {
            dialog = new ProgressDialog(mContext);
        }
        return dialog;
    }

    public static ProgressDialog createProgressBar(Context context,
                                                   String strMsg) {
        if (pd == null) {
            pd = new ProgressDialog(context, R.style.ProThemeOrange);
            pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pd.setMessage(strMsg);
            pd.setCancelable(false);
            pd.show();
        } else {
            pd.dismiss();
            pd = null;
            pd = new ProgressDialog(context, R.style.ProThemeOrange);
            pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pd.setMessage(strMsg);
            pd.setCancelable(false);
            pd.show();

        }
        return pd;
    }

    public static ProgressDialog createProgressBar(Context context,
                                                   String strMsg, boolean isCancelable) {
        pd = new ProgressDialog(context, R.style.ProThemeOrange);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setMessage(strMsg);
        pd.setCancelable(isCancelable);
        pd.show();
        return pd;
    }

    /**
     * Destroy progress bar
     */
    public static void destroyProgressBar() {
        if (pd != null)
            pd.dismiss();
    }

    // Region startactivity
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void changeactivity(Context context, Class<?> Act_des) {
        Intent i = new Intent(context, Act_des);

        ((Activity) context).startActivityForResult(i, 0);
        ((Activity) context).finish();
        if (GlobalVariable.slide_act_flag) {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            GlobalVariable.slide_act_flag = false;
        } else {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            GlobalVariable.slide_act_flag = true;
        }
    }
    // EndRegion

    // Region hidekeyboad
   /* public static void hideSoftKeyboard(Activity activity, android.widget.EditText e) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(e.getWindowToken(), 0);
    }*/

    // EndRegion
    // Region Toast function
    public static void ToastMessage(Activity a, String message) {
        try {
            Toast.makeText(a, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static String convertutf(String message) {
        try {
            return URLEncoder.encode(message.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return message;
        }
    }

    public static void hideSoftKeyboard(Activity activity, EditText editText) {
        try {

            View v = editText.findFocus();
            if (v == null)
                return;
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EndRegion
    //Region exit from application
    @android.annotation.SuppressLint("NewApi")
    public static void alertboxshow(final Activity activity) {
        // TODO Auto-generated method stub
        try {
            Builder builder = new Builder(
                    activity, android.app.AlertDialog.THEME_HOLO_LIGHT);
            builder.setTitle(activity.getResources().getString(R.string.app_name));
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        @android.annotation.SuppressLint("InlinedApi")
                        public void onClick(DialogInterface dialog, int which) {
                            final Intent relaunch = new Intent(activity, Exiter.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            activity.startActivity(relaunch);
                            activity.finish();
                        }
                    });
            builder.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            // TODO: handle exception
            Logger.debugE("LoginActivity", e + "");
        }
    }

    public static void showDialog(Context c, String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(c).create();
        alertDialog.setTitle(c.getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, c.getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    //EndRegion
    public static java.util.Map jsonToMap(JSONObject json) throws org.json.JSONException {
        java.util.Map<String, Object> retMap = new java.util.HashMap<String, Object>();
        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static java.util.Map toMap(JSONObject object) throws org.json.JSONException {
        java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
        java.util.Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if (value instanceof org.json.JSONArray) {
                value = toList((org.json.JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static java.util.List toList(org.json.JSONArray array) throws org.json.JSONException {
        java.util.List<Object> list = new java.util.ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof org.json.JSONArray) {
                value = toList((org.json.JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static boolean validateEmailAddress(String email) {
        final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "." + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    public static String getDeviceMenufacture() {
        String dm = "";
        try {
            dm = Build.MANUFACTURER;
            return dm;
        } catch (Exception e) {
            e.printStackTrace();
            return dm;
        }
    }

    public static String getDeviceModel() {
        String dm = "";
        try {
            dm = Build.MODEL;
            return dm;
        } catch (Exception e) {
            e.printStackTrace();
            return dm;
        }
    }

    public static String getDeviceOSVersion() {
        String dm = "";
        try {
            dm = Build.VERSION.RELEASE;
            return dm;
        } catch (Exception e) {
            e.printStackTrace();
            return dm;
        }
    }

    public static String getDeviceOEMBuildNumber() {
        String dm = "";
        try {
            dm = Build.FINGERPRINT;
            ;
            return dm;
        } catch (Exception e) {
            e.printStackTrace();
            return dm;
        }
    }

    public static String getDeviceUID(Activity activity) {
        try {
            String android_id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
            return android_id;
        } catch (Exception e) {
            Logger.debugE(e.toString());
            if (e.getMessage() == null) {
                Logger.debugE("" + " getDeviceUID : ");
            } else {
                Logger.debugE("" + " getDeviceUID : " + e.getMessage());

            }
            return "";
        }

    }

    public static String getDeviceSerialNumber() {
        String dm = "";
        try {
            dm = Build.SERIAL;
            return dm;
        } catch (Exception e) {
            e.printStackTrace();
            return dm;
        }
    }

    public static String applicationVersionName(Context context) {
        String version = "";
        int verCode;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return version;
        }
    }

    public static String ApplicationVersion(Context context) {
        String dm = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            dm = info.versionName;
            return dm;
        } catch (Exception e) {
            e.printStackTrace();
            return dm;
        }
    }

    public static String getDeviceType() {
        return "Android";
    }

    public static void displayErrorMessage(Context ctx, String title, String error) {
        Builder alert = new Builder(ctx);
        alert.setTitle(title);
        alert.setMessage(error);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // dismiss the dialog
                dialog.cancel();
            }
        });
        alert.setCancelable(true);
        alert.create().show();
    }

    public static JSONObject loadJSONFromAsset(Activity activity, String filename) {
        String json = null;
        JSONObject result = new JSONObject();
        try {
            InputStream is = activity.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            result = new JSONObject(json);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return result;
    }


    public static Date getDateFromUnixTime(String sTimestamp) {
        if (sTimestamp == null || sTimestamp.isEmpty()) {
            return null;
        }
        int iTimeStamp = 0;
        try {
            iTimeStamp = Integer.parseInt(sTimestamp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return new Date(iTimeStamp * 1000L); // *1000 is to convert seconds to milliseconds
    }

    public static boolean toBoolean(String target) {
        if (target == null) return false;
        return target.matches("(?i:^(1|true|yes|oui|vrai|y)$)");
    }

    public static int dpToPx(int dp, Activity activity) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static int getMessaerHeight(int width, int height, Activity activity) {
        try {
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            int widthD = displayMetrics.widthPixels;
            int heightD = displayMetrics.heightPixels;
            int w = width;
            int h = height;
            int mh = (int) ((h * widthD * 0.95) / w);
            return mh;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String parseDateToddMMyyyy(String inputPattern, String outputPattern, String time) {

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

    public static String applicationVersion(Context context) {
        String version = "";
        int verCode;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return version;
        }
    }

    public static void setImage(Context activity, ImageView imageView, int i) {
        Glide.with(activity)
                .load(Integer.valueOf(i))
                .into(imageView);
    }

    public static void setImageURL(Context activity, ImageView imageView, String url) {
        Glide.with(activity)
                .load(url)
                // .thumbnail(0.5f)
                .placeholder(R.drawable.ic_icon)
                .into(imageView);
    }

    /*public static LoginResponse getloginresponse(Context context) {
        LoginResponse loginResponse = null;
        loginResponse = new Gson().fromJson(CommonFunctions.getPreference(context, Constants.userdata, "").toString(), LoginResponse.class);
        return loginResponse;
    }

    public static UserDetailResponse getuserdetailresponse(Context context) {
        UserDetailResponse userDetailResponse = null;
        userDetailResponse = new Gson().fromJson(CommonFunctions.getPreference(context, Constants.userdetail, "").toString(), UserDetailResponse.class);
        return userDetailResponse;
    }*/

    public static boolean CheckGpsStatus(Activity context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void getCurrentLocation(Activity activity) {
        String TAG = "TAG";
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Location lastKnownLocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location lastKnownLocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocationGps == null) {
                if (lastKnownLocationNetwork != null)
                    Log.d(TAG, "Location (Network)" + lastKnownLocationNetwork.getLatitude() + ", " + lastKnownLocationNetwork.getLongitude());
            } else {
                if (lastKnownLocationNetwork == null)
                    Log.d(TAG, "Location (GPS) " + lastKnownLocationGps.getLatitude() + ", " + lastKnownLocationGps.getLongitude());
                else {
                    if (lastKnownLocationGps.getAccuracy() <= lastKnownLocationNetwork.getAccuracy())
                        Log.d(TAG, "Location (GPS) " + lastKnownLocationGps.getLatitude() + ", " + lastKnownLocationGps.getLongitude());
                    else
                        Log.d(TAG, "Location (Network) " + lastKnownLocationNetwork.getLatitude() + ", " + lastKnownLocationNetwork.getLongitude());
                }
            }
        }
    }

    private static boolean checkPermission(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(activity, permission);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static void changeActivity(Activity activity, Class cls, Bundle bundle, boolean isfinish, boolean isSlideOutRight, boolean isClearTop) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);

        if (isfinish) {
            activity.finish();
        }

        if (isClearTop) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        if (isSlideOutRight) {
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    public static Bitmap fastblur(Bitmap sentBitmap) {
        float scale = BITMAP_SCALE;
        int radius = BLUR_RADIUS;
        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);

    }

    public static class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

    public static boolean isValidate(String txtWebsite, String webUrl) {

        String website = txtWebsite;
        if (website.trim().length() > 0) {
            if (!website.matches(webUrl)) {
                //validation msg
                return false;
            }
        }
        return true;
    }
}