package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.ProfilActivity;
import com.partyspottr.appdir.ui.other_ui.EventDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.partyspottr.appdir.ui.MainActivity.typeface;

/**
 * Created by Ranarrr on 12-Feb-18.
 *
 * @author Ranarrr
 */

public class EventAdapter extends BaseAdapter {
    private Activity thisActivity;
    private List<Event> eventList;

    public EventAdapter(Activity activity, List<Event> listOfEvents) {
        thisActivity = activity;
        if(listOfEvents == null || listOfEvents.size() == 0) {
            eventList = new ArrayList<>();
            eventList.add(new Event("", "", "", "€€££$$"));
        } else
            eventList = listOfEvents;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(eventList.get(0).getHostStr().equals("€€££$$")) {
            LayoutInflater layoutInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(layoutInflater != null) {
                convertView = layoutInflater.inflate(R.layout.empty_eventlist, parent, false);
                TextView tom_liste = convertView.findViewById(R.id.empty_list);
                tom_liste.setTypeface(typeface);
                return convertView;
            }
        }

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(layoutInflater != null)
                convertView = layoutInflater.inflate(R.layout.event, parent, false);
        }

        if(convertView != null) {
            TextView arrangementNavn = convertView.findViewById(R.id.eventText);
            TextView stedText = convertView.findViewById(R.id.stedText);
            TextView aldersgrenseText = convertView.findViewById(R.id.aldersgrenseText);
            TextView datoText = convertView.findViewById(R.id.datoText);
            final ImageView bildeIListe = convertView.findViewById(R.id.imageView2);
            TextView kategori = convertView.findViewById(R.id.event_kategori);

            arrangementNavn.setTypeface(typeface);
            stedText.setTypeface(typeface);
            aldersgrenseText.setTypeface(typeface);
            datoText.setTypeface(typeface);
            kategori.setTypeface(MainActivity.typeface);

            final Event event = eventList.get(position);

            if(event.isHasimage()) {
                if(Bruker.getEventImages().containsKey(event.getHostStr() + "_" + event.getNameofevent()))
                    bildeIListe.setImageBitmap(Bruker.getEventImages().get(event.getHostStr() + "_" + event.getNameofevent()));
                else {
                    StorageReference picRef = ProfilActivity.storage.getReference().child(event.getHostStr() + "_" + event.getNameofevent());

                    picRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            bildeIListe.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            Bruker.AddEventImage(event.getHostStr() + "_" + event.getNameofevent(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            bildeIListe.setImageDrawable(thisActivity.getResources().getDrawable(R.drawable.error_loading_image));
                        }
                    });
                }
            } else
                bildeIListe.setBackgroundColor(thisActivity.getResources().getColor(R.color.verylightgrey));

            String firstletter = event.getCategory().toString().substring(0, 1).toUpperCase();
            kategori.setText(String.format(Locale.ENGLISH, "%s%s", firstletter, event.getCategory().toString().toLowerCase().substring(1, event.getCategory().toString().length())));
            stedText.setText(event.getCountry());
            GregorianCalendar datefrom = new GregorianCalendar();
            datefrom.setTimeInMillis(event.getDatefrom());
            datoText.setText(String.format(Locale.ENGLISH, "%d %s %d", datefrom.get(Calendar.DAY_OF_MONTH), datefrom.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                    thisActivity.getResources().getConfiguration().locale).toLowerCase(), datefrom.get(Calendar.YEAR)));
            aldersgrenseText.setText(String.format(Locale.ENGLISH, "%d+", event.getAgerestriction()));
            arrangementNavn.setText(event.getNameofevent());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(thisActivity, EventDetails.class);
                    intent.putExtra("eventid", event.getEventId());
                    thisActivity.startActivity(intent);

                }
            });
        }

        return convertView;
    }

}
