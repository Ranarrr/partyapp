package com.partyspottr.appdir.ui.mainfragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.ui.ProfilActivity;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.alle_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.mine_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.mitt_arkiv_fragment;

import static com.partyspottr.appdir.ui.MainActivity.typeface;

/**
 * Created by Ranarrr on 30-Jan-18.
 *
 * @author Ranarrr
 */

public class eventfragment extends Fragment {
    private boolean firsttimepagechanged;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.eventfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        final AppCompatButton alle_eventer_btn = view.findViewById(R.id.alle_eventer_btn);
        final AppCompatButton mine_eventer_btn = view.findViewById(R.id.mine_eventer_btn);
        AppCompatButton mitt_arkiv_btn = view.findViewById(R.id.arkiv_btn);

        if(getActivity() == null)
            return;

        ((TextView) getActivity().findViewById(R.id.title_toolbar)).setText(getResources().getString(R.string.arrangementer));
        ImageButton search_events = getActivity().findViewById(R.id.search_events);
        search_events.setVisibility(View.VISIBLE);
        search_events.setImageDrawable(getResources().getDrawable(R.drawable.search));
        getActivity().findViewById(R.id.add_event).setVisibility(View.VISIBLE);

        mine_eventer_btn.setTypeface(typeface);
        mitt_arkiv_btn.setTypeface(typeface);
        alle_eventer_btn.setTypeface(typeface);

        firsttimepagechanged = false;

        Fragment eventFragment = getActivity().getSupportFragmentManager().findFragmentByTag(eventfragment.class.getName());

        if(eventFragment != null) {
            final ViewPager viewPager = getActivity().findViewById(R.id.pagerview_event);
            final ImageView arrow_eventfragment = view.findViewById(R.id.arrow_eventfragment);

            if(viewPager != null) {
                PagerAdapter pagerAdapter = new ScreenSliderPagerAdapter(eventFragment.getChildFragmentManager());
                viewPager.setAdapter(pagerAdapter);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch(viewPager.getCurrentItem()) {
                            case 0:
                                ObjectAnimator.ofFloat(arrow_eventfragment, "translationX", 0.0f).start();
                                break;

                            case 1:
                                ObjectAnimator.ofFloat(arrow_eventfragment, "translationX",
                                        ((mine_eventer_btn.getX() + (mine_eventer_btn.getX() + mine_eventer_btn.getWidth())) / 2) - ((alle_eventer_btn.getX() + (alle_eventer_btn.getX() + alle_eventer_btn.getWidth())) / 2))
                                        .start();
                                break;

                            case 2:
                                ObjectAnimator.ofFloat(arrow_eventfragment, "translationX",
                                        (((mine_eventer_btn.getX() + (mine_eventer_btn.getX() + mine_eventer_btn.getWidth())) / 2) - ((alle_eventer_btn.getX() + (alle_eventer_btn.getX() + alle_eventer_btn.getWidth())) / 2)) * 2.f).start();
                                break;
                        }
                    }
                }, 250);

                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        AppCompatButton alle_eventer_btn = getActivity().findViewById(R.id.alle_eventer_btn);
                        AppCompatButton mine_eventer_btn = getActivity().findViewById(R.id.mine_eventer_btn);
                        ImageButton search_events = getActivity().findViewById(R.id.search_events);

                        if(!firsttimepagechanged)
                            firsttimepagechanged = true;
                        else {
                            switch(position) {
                                case 0:
                                    if(!ProfilActivity.childfragmentsinstack.contains(alle_eventer_fragment.class.getName()))
                                        ProfilActivity.childfragmentsinstack.add(alle_eventer_fragment.class.getName());
                                    else {
                                        ProfilActivity.childfragmentsinstack.remove(alle_eventer_fragment.class.getName());
                                        ProfilActivity.childfragmentsinstack.add(alle_eventer_fragment.class.getName());
                                    }

                                    break;
                                case 1:
                                    if(!ProfilActivity.childfragmentsinstack.contains(mine_eventer_fragment.class.getName()))
                                        ProfilActivity.childfragmentsinstack.add(mine_eventer_fragment.class.getName());
                                    else {
                                        ProfilActivity.childfragmentsinstack.remove(mine_eventer_fragment.class.getName());
                                        ProfilActivity.childfragmentsinstack.add(mine_eventer_fragment.class.getName());
                                    }

                                    break;
                                case 2:
                                    if(!ProfilActivity.childfragmentsinstack.contains(mitt_arkiv_fragment.class.getName()))
                                        ProfilActivity.childfragmentsinstack.add(mitt_arkiv_fragment.class.getName());
                                    else {
                                        ProfilActivity.childfragmentsinstack.remove(mitt_arkiv_fragment.class.getName());
                                        ProfilActivity.childfragmentsinstack.add(mitt_arkiv_fragment.class.getName());
                                    }

                                    break;
                            }
                        }

                        search_events.setVisibility(View.VISIBLE);

                        switch(position) {
                            case 0:
                                ObjectAnimator.ofFloat(arrow_eventfragment, "translationX", 0.0f).start();

                                Utilities.onSearchEventsClickAlle(getActivity());
                                break;

                            case 1:
                                ObjectAnimator.ofFloat(arrow_eventfragment, "translationX",
                                        ((mine_eventer_btn.getX() + (mine_eventer_btn.getX() + mine_eventer_btn.getWidth())) / 2) - ((alle_eventer_btn.getX() + (alle_eventer_btn.getX() + alle_eventer_btn.getWidth())) / 2)).start();

                                Utilities.onSearchMineEventer(getActivity());
                                break;

                            case 2:
                                ObjectAnimator.ofFloat(arrow_eventfragment, "translationX", (((mine_eventer_btn.getX() + (mine_eventer_btn.getX() + mine_eventer_btn.getWidth())) / 2) - ((alle_eventer_btn.getX() + (alle_eventer_btn.getX() + alle_eventer_btn.getWidth())) / 2)) * 2.f).start();

                                search_events.setVisibility(View.INVISIBLE);
                                break;
                        }
                    }
                });
            }
        }
    }

    private class ScreenSliderPagerAdapter extends FragmentStatePagerAdapter {
        ScreenSliderPagerAdapter(FragmentManager fragmentManager) {
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

                default:
                    return new alle_eventer_fragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
