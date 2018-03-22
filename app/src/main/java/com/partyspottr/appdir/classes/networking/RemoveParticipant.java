package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ListView;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Participant;
import com.partyspottr.appdir.classes.adapters.GuestListAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ranarrr on 23-Feb-18.
 *
 * @author Ranarrr
 */

public class RemoveParticipant extends AsyncTask<Void, Void, Integer> {

    private JSONObject eventidanduser;
    private ProgressDialog progressDialog;
    private List<Participant> participantList;

    public RemoveParticipant(Activity activity, long eventid, String user, List<Participant> list) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);
        participantList = list;
        try {
            eventidanduser = new JSONObject();
            eventidanduser.put("user", user);
            eventidanduser.put("eventId", eventid);
            eventidanduser.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("remove_participant", eventidanduser.toString()));
            JSONObject json = new JSONParser().get_jsonobject("POST", params, null);
            if(json != null) {
                if(json.getInt("success") == 1) {
                    return 1;
                } else {
                    return -1;
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(integer == 1) {
            try {
                Toast.makeText(progressDialog.getContext(), String.format(Locale.ENGLISH, "Removed %s!", eventidanduser.getString("user")), Toast.LENGTH_SHORT).show();
                participantList.remove(Participant.getParticipantByUsername(participantList, eventidanduser.getString("user")));
                if(progressDialog.getOwnerActivity() != null) {
                    ListView lv_guestlist = progressDialog.getOwnerActivity().findViewById(R.id.lv_gjesteliste);

                    if(lv_guestlist != null)
                        lv_guestlist.setAdapter(new GuestListAdapter(progressDialog.getOwnerActivity(), eventidanduser.getLong("eventId"), participantList));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(integer == -1) {
            Toast.makeText(progressDialog.getContext(), "Failed to remove participant.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
        }
    }
}
