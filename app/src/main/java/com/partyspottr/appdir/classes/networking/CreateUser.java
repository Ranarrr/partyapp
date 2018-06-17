package com.partyspottr.appdir.classes.networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.SplashActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 31-Jan-18.
 *
 * @author Ranarrr
 */

public class CreateUser extends AsyncTask<Void, Void, Integer> {
    private JSONObject info;
    private ProgressDialog progressDialog;
    
    public CreateUser(Context c) {
        try {
            info = new JSONObject();
            info.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            info.put("email", Bruker.get().getEmail());
            info.put("username", Bruker.get().getBrukernavn());
            info.put("passord", Bruker.get().getPassord());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(c);
        progressDialog.setMessage(c.getResources().getString(R.string.registrerer_bruker));
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try{
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("register_user", info.toString()));
            JSONObject json = new JSONParser().get_jsonobject(params);
            if(json != null) {
                if(json.getInt("exists") == 1) {
                    return -1;
                } else if(json.getInt("exists") == 0 && json.getInt("registered") == 1) {
                    return 1;
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
            SplashActivity.mAuth.createUserWithEmailAndPassword(Bruker.get().getEmail(), Bruker.get().getPassord())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(Bruker.get().getBrukernavn());
                                ref.child("fornavn").setValue(Bruker.get().getFornavn()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        ref.child("etternavn").setValue(Bruker.get().getEtternavn());
                                        ref.child("brukernavn").setValue(Bruker.get().getBrukernavn());
                                        ref.child("premium").setValue(Bruker.get().isPremium());
                                        ref.child("loggedon").setValue(false);
                                        ref.child("country").setValue(Bruker.get().getCountry());
                                        ref.child("day_of_month").setValue(Bruker.get().getDay_of_month());
                                        ref.child("friendlist").setValue(new Gson().toJson(Bruker.get().getFriendList()));
                                        ref.child("requestlist").setValue(new Gson().toJson(Bruker.get().getRequests()));
                                        ref.child("month").setValue(Bruker.get().getMonth());
                                        ref.child("town").setValue(Bruker.get().getTown());
                                        ref.child("year").setValue(Bruker.get().getYear());
                                        ref.child("oneliner").setValue(Bruker.get().getOneliner());

                                        Bruker.get().LagreBruker();
                                        Intent intent = new Intent(progressDialog.getContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        progressDialog.getContext().startActivity(intent);
                                    }
                                });
                            } else
                                Toast.makeText(progressDialog.getContext(), "Failed to create user!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if(integer == -1) {
            Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
        }
    }
}
