package com.q8munasabat.uploadfile;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.q8munasabat.config.Constants;
import com.q8munasabat.config.Q8MunasabatConfig;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;


public class MultiPartRequest extends Request<String> {
    MultipartEntityBuilder entity = MultipartEntityBuilder.create();

    private Response.Listener<String> mListener;
    private HttpEntity mHttpEntity;
    private IMultipartProgressListener mProgressListener;
    long contentsize = 0;

    public MultiPartRequest(Response.ErrorListener errorListener, Response.Listener listener, List<Image> file, JSONObject jsonObject, String url, String apiKey, String langId) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mHttpEntity = buildMultipartEntity(file, jsonObject, apiKey, langId);
    }

    public MultiPartRequest(Response.ErrorListener errorListener, Response.Listener listener, List<Image> file, JSONObject jsonObject, String url, String apiKey, String langId, String path, Uri uri) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mHttpEntity = buildMultipartEntity(file, jsonObject, apiKey, langId, path, uri);
    }

    public MultiPartRequest(Response.ErrorListener errorListener, Response.Listener listener, String file, JSONObject jsonObject, String url, String apiKey, String langId) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mHttpEntity = buildMultipartEntity(file, jsonObject, apiKey, langId);
        Log.e("json data", jsonObject.toString());
    }

    public MultiPartRequest(Response.ErrorListener errorListener, Response.Listener listener, List<Image> file, String videoname, String videothumb, JSONObject jsonObject, String url, String apiKey, String langId, boolean isProd, IMultipartProgressListener mProgressListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        this.mProgressListener = mProgressListener;
        mHttpEntity = buildMultipartEntity(file, videoname, videothumb, jsonObject, apiKey, langId, isProd);
        Log.e("json v data", jsonObject.toString());
    }

    private HttpEntity buildMultipartEntity(List<Image> file, JSONObject jsonObject, String apiKey, String langId) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (int i = 0; i < file.size(); i++) {
            File file1 = new File(file.get(i).getPath());
            FileBody fileBody = new FileBody(file1);
            builder.addPart(Constants.image, fileBody);
            Log.e(Constants.image, "file added");
        }
        builder.addTextBody(Constants.data, String.valueOf(jsonObject));
        builder.addTextBody(Constants.apikey, Q8MunasabatConfig.APIKEY);
        builder.addTextBody(Constants.languageid, langId);
        return builder.build();
    }

    private HttpEntity buildMultipartEntity(List<Image> file, JSONObject jsonObject, String apiKey, String langId, String path, Uri uri) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        File file1 = new File(uri.getEncodedPath());
        FileBody fileBody = new FileBody(file1);
        builder.addPart(Constants.image, fileBody);
        Log.e(Constants.image, "file added");
        builder.addTextBody(Constants.data, String.valueOf(jsonObject));
        builder.addTextBody(Constants.apikey, Q8MunasabatConfig.APIKEY);
        builder.addTextBody(Constants.languageid, langId);
        return builder.build();
    }

    private HttpEntity buildMultipartEntity(String filePath, JSONObject jsonObject, String apiKey, String langId) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        File file1 = new File(filePath);
        FileBody fileBody = new FileBody(file1);
        builder.addPart(Constants.image, fileBody);
        Log.e(Constants.image, "file added");

        builder.addTextBody(Constants.data, String.valueOf(jsonObject));
        builder.addTextBody(Constants.apikey, Q8MunasabatConfig.APIKEY);
        builder.addTextBody(Constants.languageid, langId);
        return builder.build();
    }

    private HttpEntity buildMultipartEntity(List<Image> file, String videoname, String videothumb, JSONObject jsonObject, String apiKey, String langId, boolean isProd) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (int i = 0; i < file.size(); i++) {
            File file1 = new File(file.get(i).getPath());
            FileBody fileBody = new FileBody(file1);
            int count = i + 1;
            builder.addPart(Constants.file + count, fileBody);
            // contentsize=contentsize+fileBody.getContentLength();

            Log.e(Constants.image, "file v added");
        }
        builder.addTextBody(Constants.data, String.valueOf(jsonObject));
        builder.addTextBody(Constants.apikey, Q8MunasabatConfig.APIKEY);
        builder.addTextBody(Constants.languageid, langId);

        try {
            if (videoname != null) {
                File fileVid = new File(videoname);
                FileBody videoBody = new FileBody(fileVid);
                builder.addPart(Constants.video, videoBody);
                Log.e(Constants.video, "video added");
                //    contentsize=contentsize+videoBody.getContentLength();
                File fileVidthumb = new File(videothumb);
                FileBody videothumbBody = new FileBody(fileVidthumb);
                builder.addPart(Constants.videothumb, videothumbBody);
                //  contentsize=contentsize+videothumbBody.getContentLength();
                Log.e(Constants.videothumb, "videothumb added");
            }
            entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return builder.build();
    }


    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            if (mProgressListener != null) {
                contentsize = contentsize + mHttpEntity.getContentLength();
                System.out.println("lgth = " + contentsize);
                mHttpEntity.writeTo(new CountingOutputStream(bos, contentsize,
                        mProgressListener));
            } else {
                mHttpEntity.writeTo(bos);
            }


            return bos.toByteArray();
        } catch (IOException e) {
            VolleyLog.e("" + e);
            return null;
        } catch (OutOfMemoryError e) {
            VolleyLog.e("" + e);
            return null;
        }

    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            Log.e("result of  data", response.data.toString());
            return Response.success(new String(response.data, "UTF-8"),
                    getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.success(new String(response.data),
                    getCacheEntry());
        }
    }


    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    public static class CountingOutputStream extends FilterOutputStream {
        private final IMultipartProgressListener progressListener;
        private long transferred;
        private long fileLength;


        public CountingOutputStream(final OutputStream out, long fileLength,
                                    final IMultipartProgressListener listener) {
            super(out);
            this.fileLength = fileLength;
            this.progressListener = listener;
            this.transferred = 0;
            progressListener.setmax(100);
        }

        public void write(byte[] buffer, int offset, int length) throws IOException {
            out.write(buffer, offset, length);
            if (progressListener != null) {
                this.transferred += length;

                int progress = ((int) ((transferred / (float) fileLength) * 100));


                this.progressListener.transferred(this.transferred, progress);
                Log.e("file --->length", fileLength + "");
                Log.e("file --->transferred", transferred + "");
                Log.e("file --->progress", progress + "");
            }
        }

        public void write(int oneByte) throws IOException {
            out.write(oneByte);
            if (progressListener != null) {
                this.transferred++;
                int progress = (int) ((transferred * 100.0f) / fileLength);
                this.progressListener.transferred(this.transferred, progress);
            }
        }
    }
}