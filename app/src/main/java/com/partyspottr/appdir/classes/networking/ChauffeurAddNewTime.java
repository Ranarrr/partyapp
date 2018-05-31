package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ranarrr on 09-Apr-18.
 *
 * @author Ranarrr
 */

public class ChauffeurAddNewTime extends AsyncTask<Void, Void, Integer> {
    private ProgressDialog progressDialog;
    private JSONObject info;

    public ChauffeurAddNewTime(Activity activity, long timeto, int passasjerer) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);

        try {
            info = new JSONObject();
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("timefrom", new Date().getTime());
            info.put("timeto", timeto);
            info.put("capacity", passasjerer);
            info.put("brukernavnElem", Bruker.get().getBrukernavn());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Adding new time.."); // TODO : Fix translation
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("add_new_time", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject(params);
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
        if(integer == 1) {
            try {
                Bruker.get().getChauffeur().setChauffeur_time_from(info.getLong("timefrom"));
                Bruker.get().getChauffeur().setChauffeur_time_to(info.getLong("timeto"));
                Bruker.get().getChauffeur().setM_capacity(info.getInt("capacity"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(progressDialog.getOwnerActivity() != null) {
                final ConstraintLayout legg_til_tid_layout = progressDialog.getOwnerActivity().findViewById(R.id.chauffeur_legg_til_tid);
                final TextView time_progress = progressDialog.getOwnerActivity().findViewById(R.id.time_progress);
                final TextView ny_tid_title = progressDialog.getOwnerActivity().findViewById(R.id.ny_tid_title);
                final ConstraintLayout timer_layout = progressDialog.getOwnerActivity().findViewById(R.id.chauffeur_timer_layout);

                if(legg_til_tid_layout != null && time_progress != null && ny_tid_title != null && timer_layout != null) {
                    legg_til_tid_layout.setVisibility(View.GONE);
                    timer_layout.setVisibility(View.VISIBLE);
                    ny_tid_title.setText("Din økt");

                    final long current = Bruker.get().getChauffeur().getChauffeur_time_to() - new Date().getTime();

                    if(current > 1800000)
                        time_progress.setTextColor(progressDialog.getContext().getResources().getColor(R.color.greentint));
                    else
                        time_progress.setTextColor(progressDialog.getContext().getResources().getColor(R.color.colorPrimary));

                    min_bil_fragment.countDownTimer = new CountDownTimer(current, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            GregorianCalendar timetoset = new GregorianCalendar();
                            timetoset.setTimeInMillis(millisUntilFinished);

                            if(millisUntilFinished < 1800000)
                                time_progress.setTextColor(progressDialog.getContext().getResources().getColor(R.color.colorPrimary));

                            time_progress.setText(String.format(Locale.ENGLISH, "%d:%02d:%02d", timetoset.get(Calendar.HOUR_OF_DAY) - 1, timetoset.get(Calendar.MINUTE),
                                    timetoset.get(Calendar.SECOND)));
                        }

                        @Override
                        public void onFinish() {
                            legg_til_tid_layout.setVisibility(View.VISIBLE);
                            timer_layout.setVisibility(View.GONE);
                            ny_tid_title.setText("Start kjøreøkt");
                        }
                    };

                    min_bil_fragment.countDownTimer.start();
                }
            }
        } else {
            Bruker.get().getChauffeur().setChauffeur_time_from(0);
            Bruker.get().getChauffeur().setChauffeur_time_to(0);
            Bruker.get().getChauffeur().setM_capacity(0);
        }

        Bruker.get().getChauffeur().LagreChauffeur();
    }
}
