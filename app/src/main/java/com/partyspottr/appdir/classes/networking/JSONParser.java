package com.partyspottr.appdir.classes.networking;

import android.support.annotation.Nullable;

import com.google.common.io.ByteStreams;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Ranarrr on 05-Nov-17.
 *
 * @author Ranarrr
 */

class JSONParser {

    JSONArray get_jsonarray(String method, List<NameValuePair> list, @Nullable File bmp) {
        try {
            return new JSONArray(makeHTTPReq(method, list, bmp));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    JSONObject get_jsonobject(String method, List<NameValuePair> list, @Nullable File bmp) {
        try {
            return new JSONObject(makeHTTPReq(method, list, bmp));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String makeHTTPReq(String method, List<NameValuePair> list, @Nullable File bmp) {
        InputStream is = null;
        StringBuilder result = new StringBuilder();
        String urlserver = "https://partyspottr.com/DBMS.php";

        if (method.equals("POST")) {
            try {
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urlserver);
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                is = defaultHttpClient.execute(httpPost).getEntity().getContent();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        } else if (method.equals("IMG")) {
            try {
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urlserver);
                MultipartEntity multipartEntity = new MultipartEntity();
                multipartEntity.addPart("image_upload", bmp);
                httpPost.setEntity(multipartEntity);
                is = defaultHttpClient.execute(httpPost).getEntity().getContent();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }

        try {
            assert is != null;

            result.append(new String(ByteStreams.toByteArray(is)));

            is.close();
        } catch (Exception e4) {
            e4.printStackTrace();
        }

        return result.toString();
    }
}