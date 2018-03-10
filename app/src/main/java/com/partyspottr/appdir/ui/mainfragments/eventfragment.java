package com.partyspottr.appdir.ui.mainfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.ui.ProfilActivity;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.alle_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.mine_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.mitt_arkiv_fragment;

import static com.partyspottr.appdir.ui.ProfilActivity.typeface;

/**
 * Created by Ranarrr on 30-Jan-18.
 *
 * @author Ranarrr
 */

public class eventfragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.eventfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        AppCompatButton alle_eventer_btn = getActivity().findViewById(R.id.alle_eventer_btn);
        ViewCompat.setBackgroundTintList(alle_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightgrey));

        AppCompatButton mine_eventer_btn = getActivity().findViewById(R.id.mine_eventer_btn);
        AppCompatButton mitt_arkiv_btn = getActivity().findViewById(R.id.arkiv_btn);

        mine_eventer_btn.setTypeface(typeface);
        mitt_arkiv_btn.setTypeface(typeface);
        alle_eventer_btn.setTypeface(typeface);

        Fragment eventFragment = getActivity().getSupportFragmentManager().findFragmentByTag(eventfragment.class.getName());

        if(eventFragment != null) {
            PagerAdapter pagerAdapter = new ScreenSliderPagerAdapter(eventFragment.getChildFragmentManager());
            ViewPager viewPager = getActivity().findViewById(R.id.pagerview_event);
            if(viewPager != null) {
                viewPager.setAdapter(pagerAdapter);
                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        AppCompatButton alle_eventer_btn = getActivity().findViewById(R.id.alle_eventer_btn);
                        AppCompatButton mine_eventer_btn = getActivity().findViewById(R.id.mine_eventer_btn);
                        AppCompatButton mitt_arkiv_btn = getActivity().findViewById(R.id.arkiv_btn);

                        switch(position) {
                            case 0:
                                ViewCompat.setBackgroundTintList(alle_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightgrey));
                                ViewCompat.setBackgroundTintList(mine_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));
                                ViewCompat.setBackgroundTintList(mitt_arkiv_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));
                                break;

                            case 1:
                                ViewCompat.setBackgroundTintList(alle_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));
                                ViewCompat.setBackgroundTintList(mine_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightgrey));
                                ViewCompat.setBackgroundTintList(mitt_arkiv_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));
                                break;

                            case 2:
                                ViewCompat.setBackgroundTintList(alle_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));
                                ViewCompat.setBackgroundTintList(mine_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));
                                ViewCompat.setBackgroundTintList(mitt_arkiv_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightgrey));
                                break;
                        }
                    }
                });
            }
        }
    }

    private class ScreenSliderPagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSliderPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new alle_eventer_fragment();
                case 1:
                    return new mine_eventer_fragment();
                case 2:
                    return new mitt_arkiv_fragment();
            }
            return new alle_eventer_fragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
