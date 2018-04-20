package com.partyspottr.appdir.ui.mainfragments;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.CountryCodes;
import com.partyspottr.appdir.classes.networking.LogoutUser;
import com.partyspottr.appdir.ui.MainActivity;

import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Ranarrr on 26-Feb-18.
 *
 * @author Ranarrr
 */

public class profilfragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profilfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView fornavn_etternavn = view.findViewById(R.id.fornavn_etternavn);
        TextView by = view.findViewById(R.id.profil_by);
        ImageView countryflag = view.findViewById(R.id.countryflag_profil);
        TextView oneliner = view.findViewById(R.id.profil_oneliner);
        TextView title = view.findViewById(R.id.brukernavn_profil);
        final TextView logout = view.findViewById(R.id.log_out_txt);

        fornavn_etternavn.setTypeface(MainActivity.typeface);
        by.setTypeface(MainActivity.typeface);
        oneliner.setTypeface(MainActivity.typeface);
        logout.setTypeface(MainActivity.typeface);
        title.setTypeface(MainActivity.typeface);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getActivity().getResources().getString(R.string.logg_ut))
                        .setMessage("Er du sikker på at du vil logge ut?") // TODO: Translation
                        .setPositiveButton(getActivity().getResources().getString(R.string.nei), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .setNegativeButton(getActivity().getResources().getString(R.string.ja), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogoutUser logoutUser = new LogoutUser(getActivity());
                                logoutUser.execute();
                            }
                        })
                        .show();

            }
        });

        logout.setPaintFlags(logout.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if(Bruker.get().getTown() == null || Bruker.get().getTown().isEmpty()) {
            by.setText(Bruker.get().getCountry());
        } else {
            by.setText(String.format(Locale.ENGLISH, "%s, %s", Bruker.get().getCountry(), Bruker.get().getTown()));
        }

        fornavn_etternavn.setText(String.format(Locale.ENGLISH, "%s %s, %d", Bruker.get().getFornavn(), Bruker.get().getEtternavn(), Utilities.calcAge(new GregorianCalendar(Bruker.get().getYear(),
                Bruker.get().getMonth(), Bruker.get().getDay_of_month()))));

        if(Bruker.get().getOneliner() == null || Bruker.get().getOneliner().isEmpty()) {
            oneliner.setVisibility(View.GONE);
        } else {
            oneliner.setText(Bruker.get().getOneliner());
        }

        if(Bruker.get().getCountry() != null) {
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
}
