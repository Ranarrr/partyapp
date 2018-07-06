package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.Participant;
import com.partyspottr.appdir.classes.Requester;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.other_ui.EventDetails;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/*
 * Created by Ranarrr on 23-Feb-18.
 */

public class RequestAdapter extends BaseAdapter {

    private List<Requester> brukerList;
    private Activity thisActivity;
    private long eventId;

    public RequestAdapter(Activity activity, List<Requester> list, long eventid) {
        eventId = eventid;
        thisActivity = activity;
        brukerList = list;
    }

    @Override
    public int getCount() {
        return brukerList.size();
    }

    @Override
    public Object getItem(int position) {
        return brukerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Requester requester = brukerList.get(position);

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(layoutInflater != null) {
                convertView = layoutInflater.inflate(R.layout.foresporsel, parent, false);
            }
        }

        if(convertView != null) {
            TextView brukernavn = convertView.findViewById(R.id.brukernavn_foresporsel);
            TextView by = convertView.findViewById(R.id.by_forsporsel);
            ImageButton accept = convertView.findViewById(R.id.request_accept);
            ImageButton reject = convertView.findViewById(R.id.request_reject);
            ImageView countryflag = convertView.findViewById(R.id.request_flag);

            brukernavn.setTypeface(MainActivity.typeface);
            by.setTypeface(MainActivity.typeface);

            if(!requester.getCountry().equals("Dominican Republic")) { // because android studio reserves the resource name "do"
                String identifier = CountryCodes.getCountrySign(requester.getCountry()).toLowerCase();

                int resource = thisActivity.getResources().getIdentifier(identifier, "drawable", thisActivity.getPackageName());

                if(resource > 0) {
                    Drawable drawable = thisActivity.getResources().getDrawable(resource);

                    countryflag.setImageDrawable(drawable);
                }
            } else
                countryflag.setImageResource(R.drawable.dominican_republic);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(thisActivity, R.style.mydatepickerdialog)
                            .setTitle("Confirm").setMessage("Are you sure you want to accept " + requester.getBrukernavn() + "?\n(This user will be notified)")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ProgressDialog progressDialog = new ProgressDialog(thisActivity, R.style.mydatepickerdialog);
                                    progressDialog.setCancelable(false);
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.setMessage("Processing..");
                                    progressDialog.show();

                                    final Event event = Bruker.get().getEventFromID(eventId);
                                    final DatabaseReference requestref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(eventId)).child("requests");

                                    Requester.removeRequest(event.getRequests(), requester.getBrukernavn());

                                    requestref.setValue(new Gson().toJson(event.getRequests())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            DatabaseReference participantref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(eventId)).child("participants");

                                            event.getParticipants().add(Participant.convertRequesterParticipant(requester));

                                            participantref.setValue(new Gson().toJson(event.getParticipants())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(thisActivity, "Successfully accepted " + requester.getBrukernavn(), Toast.LENGTH_SHORT).show();

                                                    ListView lv_requests = thisActivity.findViewById(R.id.lv_foresporsler);

                                                    if(lv_requests != null)
                                                        lv_requests.setAdapter(new RequestAdapter(thisActivity, event.getRequests(), eventId));
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(thisActivity, "Failed to accept " + requester.getBrukernavn() + ".", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.hide();
                                                    requestref.onDisconnect();
                                                }
                                            });
                                        }
                                    });
                                }
                            }).setNegativeButton("No", null)
                            .show();
                }
            });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(thisActivity, R.style.mydatepickerdialog)
                            .setTitle("Confirm").setMessage("Are you sure you want to reject " + requester.getBrukernavn() + "?\n(This user will be notified)")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ProgressDialog progressDialog = new ProgressDialog(thisActivity, R.style.mydatepickerdialog);
                                    progressDialog.setCancelable(false);
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.setMessage("Processing..");
                                    progressDialog.show();

                                    final Event event = Bruker.get().getEventFromID(eventId);
                                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(eventId)).child("requests");

                                    Requester.removeRequest(event.getRequests(), requester.getBrukernavn());

                                    ref.setValue(new Gson().toJson(event.getRequests())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            AppCompatButton details_deltaforesprsler = thisActivity.findViewById(R.id.details_delta_btn);

                                            details_deltaforesprsler.setEnabled(true);

                                            if(EventDetails.onclickDetails != null)
                                                details_deltaforesprsler.setOnClickListener(EventDetails.onclickDetails);

                                            ListView lv_requests = thisActivity.findViewById(R.id.lv_foresporsler);

                                            if(lv_requests != null)
                                                lv_requests.setAdapter(new RequestAdapter(thisActivity, event.getRequests(), eventId));

                                            Toast.makeText(thisActivity, "Successfully rejected " + requester.getBrukernavn(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(thisActivity, "Failed to reject " + requester.getBrukernavn() + ".", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.hide();
                                            ref.onDisconnect();
                                        }
                                    });
                                }
                            }).setNegativeButton("No", null)
                            .show();
                }
            });

            int age = Utilities.calcAge(new GregorianCalendar(requester.getYear(), requester.getMonth(), requester.getDay_of_month()));

            if(age > 1000)
                brukernavn.setText(String.format(Locale.ENGLISH, "%s", requester.getBrukernavn()));
            else
                brukernavn.setText(String.format(Locale.ENGLISH, "%s, %d", requester.getBrukernavn(), age));

            by.setText(requester.getTown() != null ? requester.getTown() : requester.getCountry());
        }

        return convertView;
    }
}