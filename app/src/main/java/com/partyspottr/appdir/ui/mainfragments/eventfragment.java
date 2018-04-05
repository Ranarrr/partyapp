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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.EventAdapter;
import com.partyspottr.appdir.ui.ProfilActivity;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.alle_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.mine_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.mitt_arkiv_fragment;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.partyspottr.appdir.ui.MainActivity.typeface;

/**
 * Created by Ranarrr on 30-Jan-18.
 *
 * @author Ranarrr
 */

public class eventfragment extends Fragment {
    private boolean firsttimepagechanged;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        firsttimepagechanged = false;

        Fragment eventFragment = getActivity().getSupportFragmentManager().findFragmentByTag(eventfragment.class.getName());

        if(eventFragment != null) {
            final ViewPager viewPager = getActivity().findViewById(R.id.pagerview_event);
            if(viewPager != null) {
                PagerAdapter pagerAdapter = new ScreenSliderPagerAdapter(eventFragment.getChildFragmentManager());
                viewPager.setAdapter(pagerAdapter);
                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        AppCompatButton alle_eventer_btn = getActivity().findViewById(R.id.alle_eventer_btn);
                        AppCompatButton mine_eventer_btn = getActivity().findViewById(R.id.mine_eventer_btn);
                        AppCompatButton mitt_arkiv_btn = getActivity().findViewById(R.id.arkiv_btn);
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
                                ViewCompat.setBackgroundTintList(alle_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightgrey));
                                ViewCompat.setBackgroundTintList(mine_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));
                                ViewCompat.setBackgroundTintList(mitt_arkiv_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));

                                Utilities.onSearchEventsClickAlle(getActivity());
                                break;

                            case 1:
                                ViewCompat.setBackgroundTintList(alle_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));
                                ViewCompat.setBackgroundTintList(mine_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightgrey));
                                ViewCompat.setBackgroundTintList(mitt_arkiv_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));

                                final ListView lvmine_eventer = getActivity().findViewById(R.id.lvmine_eventer);
                                final EditText search_mine_eventer = getActivity().findViewById(R.id.search_mine_eventer);

                                if(lvmine_eventer == null || search_mine_eventer == null)
                                    break;

                                search_events.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(Bruker.get().getListOfMyEvents() != null && !Bruker.get().getListOfMyEvents().isEmpty()) {
                                            search_mine_eventer.setVisibility(search_mine_eventer.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                                            ViewGroup.LayoutParams params = search_mine_eventer.getLayoutParams();

                                            if(search_mine_eventer.getVisibility() == View.INVISIBLE)
                                                params.height = 0;
                                            else {
                                                params.height = WRAP_CONTENT;

                                                search_mine_eventer.addTextChangedListener(new TextWatcher() {
                                                    @Override
                                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                                    @Override
                                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                        if(s.toString().isEmpty()) {
                                                            lvmine_eventer.setAdapter(new EventAdapter(getActivity(), Bruker.get().getListOfMyEvents()));
                                                            return;
                                                        }

                                                        List<Event> list = new ArrayList<>();
                                                        for(Event event : Bruker.get().getListOfMyEvents()) {
                                                            if(event.getHostStr().contains(s))
                                                                list.add(event);

                                                            if(event.getNameofevent().contains(s))
                                                                list.add(event);
                                                        }

                                                        lvmine_eventer.setAdapter(new EventAdapter(getActivity(), list));
                                                    }

                                                    @Override
                                                    public void afterTextChanged(Editable s) {}
                                                });
                                            }
                                        } else {
                                            Toast.makeText(getContext(), "You don't have any events!", Toast.LENGTH_SHORT).show(); // TODO: fix translation
                                        }
                                    }
                                });
                                break;

                            case 2:
                                ViewCompat.setBackgroundTintList(alle_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));
                                ViewCompat.setBackgroundTintList(mine_eventer_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightlightgrey));
                                ViewCompat.setBackgroundTintList(mitt_arkiv_btn, ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.lightgrey));

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
            }
            return new alle_eventer_fragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
