package com.partyspottr.appdir.ui.other_ui.drikkeleker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.ui.other_ui.drikkeleker.NHIE.nhie_help_fragment;

public class NeverHaveIEver extends AppCompatActivity {
    @Override
    protected void onStop() {
        Utilities.setupOnStop();

        super.onStop();
    }

    @Override
    protected void onRestart() {
        if(!Bruker.get().isConnected()) {
            super.onRestart();
            return;
        }

        Utilities.setupOnRestart(this);

        super.onRestart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.never_have_i_ever);

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.NHIE_content, new nhie_help_fragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}