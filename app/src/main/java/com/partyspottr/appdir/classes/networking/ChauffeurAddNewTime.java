package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.BatchUpdateException;
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

    public ChauffeurAddNewTime(Activity activity, long timeto) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setOwnerActivity(activity);

        try {
            info = new JSONObject();
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("timefrom", new Date().getTime());
            info.put("timeto", timeto);
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
        if(integer == 1) {
            try {
                Bruker.get().getChauffeur().setChauffeur_time_from(info.getLong("timefrom"));
                Bruker.get().getChauffeur().setChauffeur_time_to(info.getLong("timeto"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(progressDialog.getOwnerActivity() != null) {
                final Button legg_til_tid_btn = progressDialog.getOwnerActivity().findViewById(R.id.legg_til_tid_btn);
                final TextView antall_passasjerer = progressDialog.getOwnerActivity().findViewById(R.id.antall_passasjerer);
                final EditText maks_passasjerer = progressDialog.getOwnerActivity().findViewById(R.id.maks_passasjerer);
                final TextView til_kl = progressDialog.getOwnerActivity().findViewById(R.id.chauffeur_til_kl);
                final EditText time_to = progressDialog.getOwnerActivity().findViewById(R.id.time_to);
                final ProgressBar time_progressbar = progressDialog.getOwnerActivity().findViewById(R.id.chauffeur_progressbar);
                final TextView legg_til_tid_title = progressDialog.getOwnerActivity().findViewById(R.id.legg_til_tid_title);
                final Button forny_tid = progressDialog.getOwnerActivity().findViewById(R.id.chauffeur_forny);
                final Button avslutt_tid = progressDialog.getOwnerActivity().findViewById(R.id.chauffeur_avslutt);

                legg_til_tid_btn.setVisibility(View.INVISIBLE);
                antall_passasjerer.setVisibility(View.INVISIBLE);
                maks_passasjerer.setVisibility(View.INVISIBLE);
                til_kl.setVisibility(View.INVISIBLE);
                time_to.setVisibility(View.INVISIBLE);

                final long time = Bruker.get().getChauffeur().getChauffeur_time_to() - Bruker.get().getChauffeur().getChauffeur_time_from();
                if(time >= Integer.MAX_VALUE) {
                    time_progressbar.setMax((int) time / 1000);
                } else {
                    time_progressbar.setMax((int) time);
                }

                final long current = Bruker.get().getChauffeur().getChauffeur_time_to() - new Date().getTime();
                if(current >= Integer.MAX_VALUE) {
                    time_progressbar.setProgress((int) current / 1000);
                } else {
                    time_progressbar.setProgress((int) current);
                }

                new CountDownTimer(current, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        GregorianCalendar timetoset = new GregorianCalendar();
                        timetoset.setTimeInMillis(millisUntilFinished);

                        legg_til_tid_title.setText(String.format(Locale.ENGLISH, "Gjenstående tid som sjåfør: %02d t %02d min %02d sek", timetoset.get(Calendar.HOUR_OF_DAY), timetoset.get(Calendar.MINUTE),
                                timetoset.get(Calendar.SECOND)));

                        time_progressbar.setProgress((int) +(millisUntilFinished - time));
                    }

                    @Override
                    public void onFinish() {
                        legg_til_tid_btn.setVisibility(View.VISIBLE);
                        antall_passasjerer.setVisibility(View.VISIBLE);
                        maks_passasjerer.setVisibility(View.VISIBLE);
                        til_kl.setVisibility(View.VISIBLE);
                        time_to.setVisibility(View.VISIBLE);

                        legg_til_tid_title.setText("Legg til tid:");
                        forny_tid.setVisibility(View.INVISIBLE);
                        avslutt_tid.setVisibility(View.INVISIBLE);
                        time_progressbar.setVisibility(View.INVISIBLE);
                    }
                }.start();
            }
        } else {
            Bruker.get().getChauffeur().setChauffeur_time_from(0);
            Bruker.get().getChauffeur().setChauffeur_time_to(0);
        }

        Bruker.get().getChauffeur().LagreChauffeur();
    }
}
