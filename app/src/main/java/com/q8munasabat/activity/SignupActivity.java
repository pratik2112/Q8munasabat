package com.q8munasabat.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.EmptyPermissionRequestErrorListener;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.q8munasabat.R;
import com.q8munasabat.base.BaseActivity;
import com.q8munasabat.config.CommonFunctions;
import com.q8munasabat.config.Constants;
import com.q8munasabat.config.Q8MunasabatConfig;
import com.q8munasabat.config.WebService;
import com.q8munasabat.imagepicker.ImagePicker;
import com.q8munasabat.model.CheckExistResponse;
import com.q8munasabat.model.OtpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_home)
    ImageView iv_home;
    Bundle bundle;
    String lang, from = "";

    @BindView(R.id.iv_pro_pic)
    ImageView iv_pro_pic;
    @BindView(R.id.iv_pro_pic1)
    PorterShapeImageView iv_pro_pic1;
    @BindView(R.id.ll_upload)
    LinearLayout ll_upload;
    @BindView(R.id.edt_fname)
    EditText edt_fname;
    @BindView(R.id.edt_lname)
    EditText edt_lname;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    @BindView(R.id.edt_pwd)
    EditText edt_pwd;
    @BindView(R.id.edt_conf_pwd)
    EditText edt_conf_pwd;

    private ArrayList<Image> images = new ArrayList<>();
    CheckExistResponse checkExistResponse;
    OtpResponse otpResponse;
    int checkImage = 0;
    private int CROP_PIC = 1010;
    private String path = null;
    Uri imageuri;
    File profilefile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inticompnets();
    }

    @Override
    protected int getContentView() {
        lang = CommonFunctions.getPreference(this, Constants.languageid, "0");
        Locale locale = new Locale(lang.equalsIgnoreCase("0") ? "en" : "ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        return R.layout.activity_signup;
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
            iv_home.setVisibility(View.VISIBLE);

            if (lang.equalsIgnoreCase("1")) {
                edt_fname.setGravity(Gravity.RIGHT);
                edt_lname.setGravity(Gravity.RIGHT);
                edt_email.setGravity(Gravity.RIGHT);
                edt_pwd.setGravity(Gravity.RIGHT);
                edt_conf_pwd.setGravity(Gravity.RIGHT);
            } else {
                edt_fname.setGravity(Gravity.LEFT);
                edt_lname.setGravity(Gravity.LEFT);
                edt_email.setGravity(Gravity.LEFT);
                edt_pwd.setGravity(Gravity.LEFT);
                edt_conf_pwd.setGravity(Gravity.LEFT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.iv_pro_pic})
    public void cameraClick() {
        try {
            checkPermission();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPermission() {
        try {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .withListener(new MultiplePermissionsListener() {

                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            openCamera();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .withErrorListener(new EmptyPermissionRequestErrorListener())
                    .check();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        try {
            final CharSequence[] items;

            items = new CharSequence[2];
            items[0] = "Camera";
            items[1] = "Gallery";

            android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(this);
            alertdialog.setTitle("Add Image");
            alertdialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Camera")) {
                        ImagePicker.Companion.with(SignupActivity.this)
                                .crop()                //Crop Square image(Optional)
                                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                //.maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .cameraOnly()
                                .start();
                    } else if (items[item].equals("Gallery")) {
                        ImagePicker.Companion.with(SignupActivity.this)
                                .crop()                //Crop Square image(Optional)
                                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                //.maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .galleryOnly()
                                .start();
                    }
                }
            });
            alertdialog.show();
            /*images.clear();
            ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
                    .setToolbarColor("#212121")         //  Toolbar color
                    .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                    .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                    .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                    .setProgressBarColor("#4CAF50")     //  ProgressBar color
                    .setBackgroundColor("#212121")      //  Background color
                    .setCameraOnly(false)               //  Camera mode
                    .setMultipleMode(true)              //  Select multiple images or single image
                    .setFolderMode(true)                //  Folder mode
                    .setShowCamera(true)                //  Show camera button
                    .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                    .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                    .setDoneTitle("Done")               //  Done button title
                    .setLimitMessage("You have reached selection limit")    // Selection limit message
                    .setMaxSize(1)//  Max images can be selected
                    .setSavePath(String.valueOf(SavePath.DEFAULT))         //  Image capture folder name
                    .setSelectedImages(images)          //  Selected images
                    .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                    .setRequestCode(100)                //  Set request code, default Config.RC_PICK_IMAGES
                    .setKeepScreenOn(true)              //  Keep screen on when selecting images
                    .start();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.reqcode2404) {
                if (data != null) {
                    imageuri = data.getData();
                    profilefile = new File(imageuri.getEncodedPath());
                    checkImage = 1;
                    ll_upload.setVisibility(View.GONE);
                    iv_pro_pic1.setVisibility(View.VISIBLE);
                    iv_pro_pic1.setImageURI(data.getData());
                    path = profilefile.getAbsolutePath();
                    Image image = new Image(new Random().nextInt(), profilefile.getName(), profilefile.getAbsolutePath());
                    images.add(image);
                    Log.e("data", data.getData().toString());
                }
            }
        }
        /*if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            performCrop(Uri.fromFile(new File(images.get(0).getPath())));
        }
        if (requestCode == CROP_PIC) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                checkImage = 1;
                Bitmap thePic = extras.getParcelable("data");
                Uri croppedImage = getImageUri(this, thePic);
                path = getRealPathFromURIPath(croppedImage, this);
                images.get(0).setPath(path);
                ll_upload.setVisibility(View.GONE);
                iv_pro_pic1.setVisibility(View.VISIBLE);
                iv_pro_pic1.setImageURI(croppedImage);
            } else {
                checkImage = 1;
                Uri croppedImage = data.getData();
                ll_upload.setVisibility(View.GONE);
                iv_pro_pic1.setVisibility(View.VISIBLE);
                iv_pro_pic1.setImageURI(croppedImage);
                path = getRealPathFromURIPath(croppedImage, this);
                images.get(0).setPath(path);
            }
        }*/
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        FileOutputStream outStream = null;
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/Q8");
        dir.mkdirs();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(outFile);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void performCrop(Uri uri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop", "true");
            /*cropIntent.putExtra("aspectX", 3);
            cropIntent.putExtra("aspectY", 2);*/
            cropIntent.putExtra("outputX", 512);
            cropIntent.putExtra("outputY", 512);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, CROP_PIC);
        } catch (ActivityNotFoundException anfe) {
            Toast toast = Toast.makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @OnClick(R.id.btn_signup)
    public void signupClick() {
        try {
            String emailvalid = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (checkImage == 0)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_camera));
            else if (edt_fname.getText().toString().length() == 0)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_fname));
            else if (edt_lname.getText().toString().length() == 0)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_lname));
            else if (edt_email.getText().toString().length() == 0)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_email));
            else if (edt_mobile.getText().toString().length() == 0 || edt_mobile.getText().toString().length() < 8)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_mobil));
            else if (edt_pwd.getText().toString().length() == 0)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_pwd));
            else if (edt_pwd.getText().toString().length() < 6)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_pwd1));
            else if (edt_conf_pwd.getText().toString().length() == 0)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_conf_pwd));
            else if (edt_conf_pwd.getText().toString().length() < 6)
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_conf_pwd1));
            else if (!edt_email.getText().toString().trim().matches(emailvalid))
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_valid_email));
            else if (!(edt_pwd.getText().toString()).equals(edt_conf_pwd.getText().toString()))
                showAlertDialog(getString(R.string.app_name), getString(R.string.err_mismatch));
            else
                checkExistEmail();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkExistEmail() {
        try {
            if (CommonFunctions.checkConnection(this)) {
                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.checkexistURL;
                Map<String, String> mParams = new HashMap<>();
                mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                mParams.put(Constants.languageid, lang);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.email, edt_email.getText().toString().trim());
                jsonObject.put(Constants.type, "1");
                jsonObject.put(Constants.check, "1");
                mParams.put(Constants.data, String.valueOf(jsonObject));

                WebService webService = new WebService(url, new JSONObject(), true, this);
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
                            checkExistResponse = gson.fromJson(result.toString(), CheckExistResponse.class);
                            if (checkExistResponse.result.equals("fail")) {
                                showAlertDialog(getString(R.string.app_name), checkExistResponse.error);
                            } else {
                                checkExistPhone();
                            }
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

    private void checkExistPhone() {
        try {
            if (CommonFunctions.checkConnection(this)) {
                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.checkexistURL;
                Map<String, String> mParams = new HashMap<>();
                mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                mParams.put(Constants.languageid, lang);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.email, edt_mobile.getText().toString());
                jsonObject.put(Constants.type, "1");
                jsonObject.put(Constants.check, "2");
                mParams.put(Constants.data, String.valueOf(jsonObject));

                WebService webService = new WebService(url, new JSONObject(), true, this);
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
                            checkExistResponse = gson.fromJson(result.toString(), CheckExistResponse.class);
                            if (checkExistResponse.result.equals("fail")) {
                                showAlertDialog(getString(R.string.app_name), checkExistResponse.error);
                            } else {
                                getOtpValue();
                            }
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

    private void getOtpValue() {
        try {
            if (CommonFunctions.checkConnection(this)) {
                String url = Q8MunasabatConfig.WEBURL + Q8MunasabatConfig.getotpURL;
                String mobileNo = /*ccp_cuntry_code.getSelectedCountryCodeWithPlus()*/ "+965" + edt_mobile.getText().toString();
                Map<String, String> mParams = new HashMap<>();
                mParams.put(Constants.apikey, Q8MunasabatConfig.APIKEY);
                mParams.put(Constants.languageid, lang);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.mobileno, mobileNo);
                jsonObject.put(Constants.languageid, lang);
                mParams.put(Constants.data, String.valueOf(jsonObject));

                WebService webService = new WebService(url, new JSONObject(), true, this);
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
                            otpResponse = gson.fromJson(result.toString(), OtpResponse.class);
                            Intent intent = new Intent(SignupActivity.this, CodeVerifyActivity.class);
                            intent.putExtra(Constants.from, getString(R.string.tit_code_verify));

                            intent.putExtra(Constants.firstname, CommonFunctions.convertutf(edt_fname.getText().toString()));
                            intent.putExtra(Constants.lastname, CommonFunctions.convertutf(edt_lname.getText().toString()));
                            intent.putExtra(Constants.email, edt_email.getText().toString().trim());
                            intent.putExtra(Constants.password, CommonFunctions.convertutf(edt_pwd.getText().toString().trim()));
                            intent.putExtra(Constants.countrycode, "+965");
                            intent.putExtra(Constants.mobileno, edt_mobile.getText().toString());
                            intent.putExtra(Constants.otpvalue, otpResponse.otp);
                            intent.putExtra(Constants.image, new Gson().toJson(images));
                            intent.putExtra(Constants.path, path);
                            intent.putExtra(Constants.imageuri, imageuri.toString());

                            startActivity(intent);
                            Log.e("OTP Value -->", otpResponse.otp);
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