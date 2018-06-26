package com.partyspottr.appdir.ui.other_ui.drikkeleker.NHIE;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.ui.MainActivity;

public class nhie_help_fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.nhie_help, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button nhie_help_continue = view.findViewById(R.id.nhie_help_continue);

        TextView rules_title = view.findViewById(R.id.nhie_help_title);
        TextView rules = view.findViewById(R.id.nhie_help_rules);

        nhie_help_continue.setTypeface(MainActivity.typeface);
        rules_title.setTypeface(MainActivity.typeface);
        rules.setTypeface(MainActivity.typeface);

        nhie_help_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();

                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.NHIE_content, new nhie_game_fragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });
    }
}
