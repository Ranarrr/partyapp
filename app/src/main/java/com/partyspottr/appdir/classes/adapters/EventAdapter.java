package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Event;
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
        } else {
            eventList = listOfEvents;
        }
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
            if(layoutInflater != null) {
                convertView = layoutInflater.inflate(R.layout.event, parent, false);
            }
        }

        if(convertView != null) {
            TextView arrangementNavn = convertView.findViewById(R.id.eventText);
            TextView stedText = convertView.findViewById(R.id.stedText);
            TextView hostText = convertView.findViewById(R.id.hostText);
            TextView datoText = convertView.findViewById(R.id.datoText);
            ImageView bildeIListe = convertView.findViewById(R.id.imageView2);

            arrangementNavn.setTypeface(typeface);
            stedText.setTypeface(typeface);
            hostText.setTypeface(typeface);
            datoText.setTypeface(typeface);

            final Event event = eventList.get(position);

            //final Bitmap bmp = BitmapFactory.decodeByteArray(event.get, 0, event.getImagebyte().length);

            //bildeIListe.setImageBitmap(bmp);

            stedText.setText(eventList.get(position).getCountry());
            GregorianCalendar datefrom = new GregorianCalendar();
            datefrom.setTimeInMillis(event.getDatefrom());
            datoText.setText(String.format(Locale.ENGLISH, "%d %s %d", datefrom.get(Calendar.DAY_OF_MONTH), datefrom.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                    thisActivity.getResources().getConfiguration().locale).toLowerCase(), datefrom.get(Calendar.YEAR)));
            hostText.setText(eventList.get(position).getHostStr());
            arrangementNavn.setText(eventList.get(position).getNameofevent());

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
