package com.partyspottr.appdir.ui.mainfragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.CountryCodes;

import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Ranarrr on 26-Feb-18.
 *
 * @author Ranarrr
 */

public class profilfragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profilfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView fornavn_etternavn = view.findViewById(R.id.fornavn_etternavn);
        TextView by = view.findViewById(R.id.profil_by);
        ImageView countryflag = view.findViewById(R.id.countryflag_profil);
        TextView oneliner = view.findViewById(R.id.profil_oneliner);

        if(Bruker.get().getTown() == null) {
            by.setText(Bruker.get().getCountry());
        } else {
            by.setText(Bruker.get().getTown());
        }

        fornavn_etternavn.setText(String.format(Locale.ENGLISH, "%s %s, %d", Bruker.get().getFornavn(), Bruker.get().getEtternavn(), Utilities.calcAge(new GregorianCalendar(Bruker.get().getYear(),
                Bruker.get().getMonth(), Bruker.get().getDay_of_month()))));

        if(Bruker.get().getOneliner() == null || Bruker.get().getOneliner().isEmpty()) {
            oneliner.setVisibility(View.GONE);
        } else {
            oneliner.setText(Bruker.get().getOneliner());
        }


        if(!Bruker.get().getCountry().equals("Dominican Republic")) { // because android studio reserves the resource name "do"
            String identifier = CountryCodes.getCountrySign(Bruker.get().getCountry()).toLowerCase();

            int resource = getActivity().getResources().getIdentifier(identifier, "drawable", getActivity().getPackageName());

            if(resource > 0) {
                Drawable drawable = getActivity().getResources().getDrawable(resource);

                countryflag.setImageDrawable(drawable);
            }
        } else {
            countryflag.setImageResource(R.drawable.dominican_republic);
        }
    }
}
