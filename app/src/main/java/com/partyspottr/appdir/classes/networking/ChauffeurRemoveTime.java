package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.ui.mainfragments.bilchildfragments.min_bil_fragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class ChauffeurRemoveTime extends AsyncTask<Void, Void, Integer> {
    private ProgressDialog progressDialog;
    private JSONObject info;

    public ChauffeurRemoveTime(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);

        try {
            info = new JSONObject();
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("brukernavnElem", Bruker.get().getBrukernavn());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Cancelling time..");
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("remove_time", info.toString()));
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

        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        progressDialog.hide();

        if(integer == 1) {
            if(progressDialog.getOwnerActivity() != null) {
                TextView ny_tid_title = progressDialog.getOwnerActivity().findViewById(R.id.ny_tid_title);
                ConstraintLayout legg_til_tid = progressDialog.getOwnerActivity().findViewById(R.id.chauffeur_legg_til_tid);
                ConstraintLayout timer_layout = progressDialog.getOwnerActivity().findViewById(R.id.chauffeur_timer_layout);
                final Button start_ny_tid = progressDialog.getOwnerActivity().findViewById(R.id.chauffeur_start);

                min_bil_fragment.countDownTimer.cancel();

                ny_tid_title.setText("Start kjøreøkt");
                legg_til_tid.setVisibility(View.VISIBLE);
                timer_layout.setVisibility(View.GONE);

                start_ny_tid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(min_bil_fragment.passasjerer == 0 || min_bil_fragment.timer_tid == 0 || min_bil_fragment.minutter_tid == -1)
                            return;

                        GregorianCalendar to = new GregorianCalendar();
                        to.setTimeInMillis(System.currentTimeMillis());

                        to.add(Calendar.HOUR_OF_DAY, min_bil_fragment.timer_tid);

                        if(min_bil_fragment.minutter_tid > 0)
                            to.add(Calendar.MINUTE, min_bil_fragment.minutter_tid);

                        ChauffeurAddNewTime chauffeurAddNewTime = new ChauffeurAddNewTime(progressDialog.getOwnerActivity(), to.getTimeInMillis(), min_bil_fragment.passasjerer);
                        chauffeurAddNewTime.execute();
                    }
                });
            }

            Bruker.get().getChauffeur().setChauffeur_time_from(0);
            Bruker.get().getChauffeur().setChauffeur_time_to(0);
            Bruker.get().getChauffeur().setM_capacity(0);
        } else {
            Toast.makeText(progressDialog.getContext(), "Failed to cancel time!", Toast.LENGTH_SHORT).show();
        }
    }
}
