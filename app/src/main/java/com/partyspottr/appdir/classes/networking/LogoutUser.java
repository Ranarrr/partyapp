package com.partyspottr.appdir.classes.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.ProfilActivity;
import com.partyspottr.appdir.ui.SplashActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 09-Feb-18.
 *
 * @author Ranarrr
 */

public class LogoutUser extends AsyncTask<Void, Void, Integer> {

    private ProgressDialog progressDialog;
    private JSONObject info;

    public LogoutUser(Activity activity) {
        if(!Bruker.get().isLoggetpa()) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            cancel(true);
        }

        if(SplashActivity.mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            cancel(true);
        }

        try {
            info = new JSONObject();
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("username", Bruker.get().getBrukernavn());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(activity, R.style.mydatepickerdialog);
        progressDialog.setOwnerActivity(activity);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage(progressDialog.getContext().getResources().getString(R.string.logging_out));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("logout_user", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject(params);
            if(json != null) {
                if(json.getInt("success") == 1) {
                    if(ProfilActivity.valueEventListener != null && ProfilActivity.ref != null)
                        ProfilActivity.ref.removeEventListener(ProfilActivity.valueEventListener);

                    Bruker.get().StopParsingChauffeurs();
                    Bruker.get().StopParsingBrukerChauffeur();
                    Bruker.get().StopParsingEvents();
                    Bruker.get().StopParsingBrukerInfo();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(Bruker.get().getBrukernavn());
                    ref.child("loggedon").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                if(SplashActivity.mAuth.getCurrentUser() != null)
                                    SplashActivity.mAuth.signOut();
                        }
                    });

                    Bruker.get().setLoggetpa(false);

                    return 1;
                } else {
                    return 0;
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
            Bruker.get().setPassord("");
            Bruker.get().LagreBruker();

            Intent intent = new Intent(progressDialog.getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            progressDialog.getContext().startActivity(intent);

            progressDialog.dismiss();

            if(progressDialog.getOwnerActivity() != null)
                progressDialog.getOwnerActivity().finish();
        } else {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.failed_logout), Toast.LENGTH_SHORT).show();
        }
    }
}
