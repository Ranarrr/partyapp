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
import com.partyspottr.appdir.ui.mainfragments.bilchildfragments.finn_bil_fragment;
import com.partyspottr.appdir.ui.mainfragments.bilchildfragments.min_bil_fragment;

import static com.partyspottr.appdir.ui.MainActivity.typeface;

/**
 * Created by Ranarrr on 30-Jan-18.
 *
 * @author Ranarrr
 */

public class bilfragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bilfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        AppCompatButton finn_bil_btn = getActivity().findViewById(R.id.finn_bil_btn);
        ViewCompat.setBackgroundTintList(finn_bil_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightgrey));

        AppCompatButton min_bil_btn = getActivity().findViewById(R.id.min_bil_btn);

        finn_bil_btn.setTypeface(typeface);
        min_bil_btn.setTypeface(typeface);

        Fragment bilFragment = getActivity().getSupportFragmentManager().findFragmentByTag(bilfragment.class.getName());

        if(bilFragment != null) {
            PagerAdapter pagerAdapter = new ScreenSliderPagerAdapter(bilFragment.getChildFragmentManager());
            ViewPager viewPager = getActivity().findViewById(R.id.pagerview_bil);
            if(viewPager != null) {
                viewPager.setAdapter(pagerAdapter);
                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        AppCompatButton finn_bil_btn = getActivity().findViewById(R.id.finn_bil_btn);
                        AppCompatButton min_bil_btn = getActivity().findViewById(R.id.min_bil_btn);

                        switch(position) {
                            case 0:
                                ViewCompat.setBackgroundTintList(finn_bil_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightgrey));
                                ViewCompat.setBackgroundTintList(min_bil_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));
                                break;

                            case 1:
                                ViewCompat.setBackgroundTintList(finn_bil_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));
                                ViewCompat.setBackgroundTintList(min_bil_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightgrey));
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
                    return new finn_bil_fragment();
                case 1:
                    return new min_bil_fragment();
            }
            return new finn_bil_fragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
