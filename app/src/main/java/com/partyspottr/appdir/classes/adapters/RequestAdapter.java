package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/*
 * Created by Ranarrr on 23-Feb-18.
 */

public class RequestAdapter extends BaseAdapter {

    List<Bruker> brukerList;
    Activity thisActivity;


    public RequestAdapter(Activity activity, List<Bruker> list) {
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

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(layoutInflater != null) {
                convertView = layoutInflater.inflate(R.layout.foresporsel, parent);
            }
        }

        if(convertView != null) {

            TextView brukernavn = convertView.findViewById(R.id.brukernavn_foresporsel);
            TextView by = convertView.findViewById(R.id.by_forsporsel);

            Bruker bruker = brukerList.get(position);

            Calendar calendar = new GregorianCalendar(bruker.getYear(), bruker.getMonth(), bruker.getDay_of_month());
            int something = Calendar.getInstance().compareTo(calendar);

            brukernavn.setText(String.format(Locale.ENGLISH, "%s, %d", bruker.getBrukernavn(), 18));
            //by.setText(brukerList.get(position).get);

        }

        return convertView;
    }
}
