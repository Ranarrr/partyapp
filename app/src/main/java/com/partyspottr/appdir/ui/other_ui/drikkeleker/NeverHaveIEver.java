package com.partyspottr.appdir.ui.other_ui.drikkeleker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.ui.other_ui.drikkeleker.NHIE.nhie_game_fragment;
import com.partyspottr.appdir.ui.other_ui.drikkeleker.NHIE.nhie_help_fragment;

public class NeverHaveIEver extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.never_have_i_ever);

        Button nhie_help_continue = findViewById(R.id.nhie_help_continue);

        nhie_help_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(1);
            }
        });

        replaceFragment(0);
    }

    private void replaceFragment(int fragmentNum) {
        Fragment fragment;
        switch (fragmentNum) {
            case 0:
                fragment = new nhie_help_fragment();
                break;
            case 1:
                fragment = new nhie_game_fragment();
                break;
            default:
                fragment = new nhie_help_fragment();
                break;
        }

        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null){ // fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.NHIE_content, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}