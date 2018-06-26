package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ListView;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.Requester;
import com.partyspottr.appdir.classes.adapters.RequestAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 23-Feb-18.
 *
 * @author Ranarrr
 */

public class RemoveEventRequest extends AsyncTask<Void, Void, Integer> {
    private JSONObject eventidanduser;
    private ProgressDialog progressDialog;
    private String error_msg;

    public RemoveEventRequest(Activity activity, long eventid, String user, boolean accept) {
        try {
            eventidanduser = new JSONObject();
            eventidanduser.put("user", user);
            eventidanduser.put("eventId", eventid);
            eventidanduser.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            eventidanduser.put("acceptordeny", accept ? "accept" : "deny");
        } catch(JSONException e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("remove_event_request", eventidanduser.toString()));
            JSONObject json = new JSONParser().get_jsonobject(params);
            if(json != null) {
                if(json.getInt("success") == 1) {
                    return 1;
                } else {
                    error_msg = json.getString("error_msg");
                    return -1;
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        progressDialog.hide();
        if(integer == 1) {
            try {
                long eventid = eventidanduser.getLong("eventId");
                Event eventtochange = Bruker.get().getEventFromID(eventid);

                if(eventidanduser.getString("acceptordeny").equals("accept")) {
                    Toast.makeText(progressDialog.getContext(), "Successfully accepted user.", Toast.LENGTH_SHORT).show();
                    if(progressDialog.getOwnerActivity() != null) {
                        ListView lv_requests = progressDialog.getOwnerActivity().findViewById(R.id.lv_foresporsler);

                        if(eventtochange != null) {
                            String user = eventidanduser.getString("user");
                            Requester requester = eventtochange.getRequesterByUsername(user);
                            if(requester != null) {
                                eventtochange.addRequestToParticipant(requester);
                                Requester.removeRequest(eventtochange.getRequests(), user);
                                if(lv_requests != null)
                                    lv_requests.setAdapter(new RequestAdapter(progressDialog.getOwnerActivity(), eventtochange.getRequests(), eventid));
                                Bruker.get().setEventByID(eventid, eventtochange);
                            } else {
                                GetAllEvents getAllEvents = new GetAllEvents(progressDialog.getOwnerActivity());
                                getAllEvents.execute();
                            }
                        } else {
                            GetAllEvents getAllEvents = new GetAllEvents(progressDialog.getOwnerActivity());
                            getAllEvents.execute();
                        }
                    }
                } else {
                    Toast.makeText(progressDialog.getContext(), "Successfully denied user.", Toast.LENGTH_SHORT).show();
                    if(progressDialog.getOwnerActivity() != null) {
                        ListView lv_requests = progressDialog.getOwnerActivity().findViewById(R.id.lv_foresporsler);

                        if(eventtochange != null) {
                            Requester.removeRequest(eventtochange.getRequests(), eventidanduser.getString("user"));
                            if(lv_requests != null)
                                lv_requests.setAdapter(new RequestAdapter(progressDialog.getOwnerActivity(), eventtochange.getRequests(), eventid));

                            Bruker.get().setEventByID(eventid, eventtochange);
                        } else {
                            GetAllEvents getAllEvents = new GetAllEvents(progressDialog.getOwnerActivity());
                            getAllEvents.execute();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(integer == -1) {
            if(error_msg.equals("Could not remove this request, it does not exist.")) {
                Toast.makeText(progressDialog.getContext(), "This user has already been accepted or rejected.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
