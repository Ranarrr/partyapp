package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.Participant;
import com.partyspottr.appdir.classes.adapters.GuestListAdapter;
import com.partyspottr.appdir.enums.EventStilling;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ranarrr on 15-Feb-18.
 *
 * @author Ranarrr
 */

public class AddParticipant extends AsyncTask<Void, Void, Integer> {

    private JSONObject eventidanduser;
    private ProgressDialog progressDialog;
    private List<Participant> participantList;
    private String finish_msg;
    private String username;

    public AddParticipant(Activity activity, long eventid, @Nullable Participant participant, List<Participant> list) {
        participantList = list;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);
        try{
            eventidanduser = new JSONObject();

            if(participant == null) {
                if(Bruker.get().isPremium()) {
                    eventidanduser.put("user", new Gson().toJson(Participant.convertBrukerParticipant(Bruker.get(), EventStilling.PREMIUM)));
                } else {
                    eventidanduser.put("user", new Gson().toJson(Participant.convertBrukerParticipant(Bruker.get(), EventStilling.GJEST)));
                }

                finish_msg = "Participated!";
            } else {
                if(participant.getStilling() == null)
                    participant.setStilling(EventStilling.GJEST);

                eventidanduser.put("participant", new Gson().toJson(participant));
                finish_msg = "Added " + participant.getBrukernavn();
                username = participant.getBrukernavn();
            }

            eventidanduser.put("eventId", eventid);
            eventidanduser.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        if(finish_msg.equals("Participated!"))
            progressDialog.setMessage("Participating..");
        else
            progressDialog.setMessage("Adding user..");

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("add_participant", eventidanduser.toString()));
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
        progressDialog.hide();
        if(integer == 1) {
            Toast.makeText(progressDialog.getContext(), finish_msg, Toast.LENGTH_SHORT).show();
            if(finish_msg.equals("Participated!"))
                participantList.add(Participant.getParticipantByUsername(participantList, Bruker.get().getBrukernavn()));
            else {
                try {
                    Participant temp = new Gson().fromJson(eventidanduser.getString("participant"), Participant.type);
                    participantList.add(temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
                if(progressDialog.getOwnerActivity() != null) {
                    ListView lv_guestlist = progressDialog.getOwnerActivity().findViewById(R.id.lv_gjesteliste);
                    TextView details_antall_deltakere = progressDialog.getOwnerActivity().findViewById(R.id.details_antall_deltakere);

                    long eventid = eventidanduser.getLong("eventId");

                    if(details_antall_deltakere != null) {
                        details_antall_deltakere.setText(String.format(Locale.ENGLISH, "%d av %d skal.", participantList.size(), Bruker.get().getEventFromID(eventid).getMaxparticipants()));
                    }

                    if(lv_guestlist != null)
                        lv_guestlist.setAdapter(new GuestListAdapter(progressDialog.getOwnerActivity(), eventid, participantList));

                    Event eventtochange = Bruker.get().getEventFromID(eventid);

                    if(eventtochange != null) {
                        eventtochange.setParticipants(participantList);
                        Bruker.get().setEventByID(eventid, eventtochange);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (integer == 0){
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
        } else {
            if(finish_msg.equals("Participated!"))
                Toast.makeText(progressDialog.getContext(), "Failed to participate.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(progressDialog.getContext(), "Failed to add user.", Toast.LENGTH_SHORT).show();
        }
    }
}
