package com.partyspottr.appdir.classes.networking;

import com.google.common.io.ByteStreams;
import com.partyspottr.appdir.BuildConfig;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Ranarrr on 05-Nov-17.
 *
 * @author Ranarrr
 */

class JSONParser {
    JSONArray get_jsonarray(List<NameValuePair> list) {
        try {
            return new JSONArray(makeHTTPReq(list));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    JSONObject get_jsonobject(List<NameValuePair> list) {
        try {
            return new JSONObject(makeHTTPReq(list));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String makeHTTPReq(List<NameValuePair> list) {
        InputStream is = null;
        StringBuilder result = new StringBuilder();

        try {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(BuildConfig.DBMS_URL);
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            is = defaultHttpClient.execute(httpPost).getEntity().getContent();
        } catch (IOException e3) {
            e3.printStackTrace();
        }

        try {
            result.append(new String(ByteStreams.toByteArray(is)));

            is.close();
        } catch (Exception e4) {
            e4.printStackTrace();
        }

        return result.toString();
    }
}