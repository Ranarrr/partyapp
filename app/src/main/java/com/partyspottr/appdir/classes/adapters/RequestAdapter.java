package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Requester;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.networking.RemoveEventRequest;
import com.partyspottr.appdir.ui.MainActivity;

import java.util.Calendar;
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
            } else {
                countryflag.setImageResource(R.drawable.dominican_republic);
            }

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(thisActivity)
                            .setTitle("Confirm").setMessage("Are you sure you want to accept " + requester.getBrukernavn() + "?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RemoveEventRequest removeEventRequest = new RemoveEventRequest(thisActivity, eventId, requester.getBrukernavn(), true);
                                    removeEventRequest.execute();
                                }
                            }).show();
                }
            });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(thisActivity)
                            .setTitle("Confirm").setMessage("Are you sure you want to reject " + requester.getBrukernavn() + "?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RemoveEventRequest removeEventRequest = new RemoveEventRequest(thisActivity, eventId, requester.getBrukernavn(), false);
                                    removeEventRequest.execute();
                                }
                            }).show();
                }
            });

            brukernavn.setText(String.format(Locale.ENGLISH, "%s, %d", requester.getBrukernavn(), Utilities.calcAge(new GregorianCalendar(requester.getYear(), requester.getMonth(), requester.getDay_of_month()))));

            if(requester.getTown() != null)
                by.setText(requester.getTown());
            else
                by.setText(requester.getCountry());
        }

        return convertView;
    }
}