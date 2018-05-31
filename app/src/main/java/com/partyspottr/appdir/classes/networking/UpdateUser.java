package com.partyspottr.appdir.classes.networking;

import android.os.AsyncTask;

import com.partyspottr.appdir.classes.Bruker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class UpdateUser extends AsyncTask<Void, Void, Void> {
    public UpdateUser() {}

    @Override
    protected Void doInBackground(Void... voids) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("update_user", Bruker.get().BrukerToJSON()));
        new JSONParser().get_jsonobject(params);

        return null;
    }
}
