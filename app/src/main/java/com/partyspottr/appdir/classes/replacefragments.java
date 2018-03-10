package com.partyspottr.appdir.classes;

/*
 * Created by Ranarrr on 30-Jan-18.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.ui.mainfragments.bilchildfragments.finn_bil_fragment;
import com.partyspottr.appdir.ui.mainfragments.bilchildfragments.min_bil_fragment;
import com.partyspottr.appdir.ui.mainfragments.bilfragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.alle_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.mine_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.mitt_arkiv_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventfragment;

public class replacefragments {

    public void replaceBilChildFragment(int childnum, FragmentActivity parentActivity) {
        Fragment fragment, bilFragment = parentActivity.getSupportFragmentManager().findFragmentByTag(bilfragment.class.getName());
        if(bilFragment == null)
            return;

        switch(childnum) {
            case 0:
                fragment = new finn_bil_fragment();
                break;
            case 1:
                fragment = new min_bil_fragment();
                break;
            default:
                fragment = new finn_bil_fragment();
                break;
        }

        String backStateName = fragment.getClass().getName();

        FragmentManager manager = bilFragment.getChildFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if(!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_bil_content, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void replaceEventChildFragment(int childnum, FragmentActivity parentActivity) {
        Fragment fragment, eventFragment = parentActivity.getSupportFragmentManager().findFragmentByTag(eventfragment.class.getName());
        if(eventFragment == null)
            return;

        switch(childnum) {
            case 0:
                fragment = new alle_eventer_fragment();
                break;
            case 1:
                fragment = new mine_eventer_fragment();
                break;
            case 2:
                fragment = new mitt_arkiv_fragment();
                break;
            default:
                fragment = new alle_eventer_fragment();
                break;
        }

        String backStateName = fragment.getClass().getName();

        FragmentManager manager = eventFragment.getChildFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if(!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_event_content, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}
