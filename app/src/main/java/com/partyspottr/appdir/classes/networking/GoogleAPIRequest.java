package com.partyspottr.appdir.classes.networking;

/*
 * Created by Ranarrr on 20-Feb-18.
 */

import android.content.Context;

import com.partyspottr.appdir.classes.Bruker;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

public class GoogleAPIRequest {

    public static JSONObject makeHTTPReq(Context c, String address, int postal_code) {
        InputStream is = null;
        JSONObject json = null;
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?");
        URL urll;

        url.append(String.format(Locale.ENGLISH, "address=%s&components=postal_code:%d|country:%s&key=%s", address, postal_code,Bruker.get().getCountry(), "AIzaSyAwrHRK0OTkTj_o0bhAbAUDvqdGv9LwrxQ"));

        try {
            urll = new URL(url.toString());
            URI uri = new URI(urll.getProtocol(), urll.getUserInfo(), urll.getHost(), urll.getPort(), urll.getPath(), urll.getQuery(), urll.getRef());
            urll = uri.toURL();
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urll.toString());
            is = defaultHttpClient.execute(httpPost).getEntity().getContent();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        try {
            assert is != null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuilder.append(readLine).append("\n");
            }
            is.close();
            json = new JSONObject(stringBuilder.toString());
        } catch (Exception e4) {
            e4.printStackTrace();
        }

        return json;
    }
}
