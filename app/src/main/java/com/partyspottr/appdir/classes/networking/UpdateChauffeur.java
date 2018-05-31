package com.partyspottr.appdir.classes.networking;

import android.os.AsyncTask;

import com.partyspottr.appdir.classes.Bruker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class UpdateChauffeur extends AsyncTask<Void, Void, Void> {
    public UpdateChauffeur() {}

    @Override
    protected Void doInBackground(Void... voids) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("update_chauffeur", Bruker.get().getChauffeur().ChauffeurJSONString()));
        new JSONParser().get_jsonobject(params);

        return null;
    }
}
