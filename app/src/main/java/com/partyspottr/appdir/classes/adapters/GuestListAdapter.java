package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Participant;

import java.util.List;

import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
import static com.partyspottr.appdir.ui.ProfilActivity.typeface;

/**
 * Created by Ranarrr on 23-Feb-18.
 *
 * @author Ranarrr
 */

public class GuestListAdapter extends BaseAdapter {

    private List<Participant> GuestList;
    private Activity thisActivity;

    public GuestListAdapter(Activity activity, List<Participant> guestlist) {
        GuestList = Participant.SortParticipants(guestlist);
        thisActivity = activity;
    }

    @Override
    public int getCount() {
        return GuestList.size();
    }

    @Override
    public Object getItem(int position) {
        return GuestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Participant participant = GuestList.get(position);

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(layoutInflater != null) {
                convertView = layoutInflater.inflate(R.layout.gjest, parent, false);
            }
        }

        if(convertView != null) {
            //ImageView profilbilde = convertView.findViewById(R.id.gjest_profilbilde);
            ImageView countryflag = convertView.findViewById(R.id.countryflag_IV);
            TextView brukernavn = convertView.findViewById(R.id.gjest_brukernavn);
            TextView by_land = convertView.findViewById(R.id.gjest_by_land);
            final ImageButton more_options = convertView.findViewById(R.id.gjest_more_options);
            final LinearLayout linearLayout = convertView.findViewById(R.id.more_options_ll);

            brukernavn.setTypeface(typeface);
            by_land.setTypeface(typeface);

            if(!participant.getCountry().equals("Dominican Republic")) { // because android studio reserves the resource name "do"
                String identifier = CountryCodes.getCountrySign(participant.getCountry()).toLowerCase();

                int resource = thisActivity.getResources().getIdentifier(identifier, "drawable", thisActivity.getPackageName());

                if(resource > 0) {
                    Drawable drawable = thisActivity.getResources().getDrawable(resource);

                    countryflag.setImageDrawable(drawable);
                }
            } else {
                countryflag.setImageResource(R.drawable.dominican_republic);
            }

            more_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //linearLayout.setVisibility(linearLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });

            brukernavn.setText(participant.getBrukernavn());

            if(participant.getTown() != null) {
                by_land.setText(participant.getTown());
            } else {
                by_land.setText(participant.getCountry());
            }
        }

        return convertView;
    }
}
