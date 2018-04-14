package com.partyspottr.appdir.ui.mainfragments.eventchildfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.EventAdapter;

public class mine_eventer_fragment extends Fragment {
    public static boolean once = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mine_eventer_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView lvmine_eventer = getActivity().findViewById(R.id.lvmine_eventer);
        ViewPager pager = getActivity().findViewById(R.id.pagerview_event);

        if(!once && pager != null) {
            if(pager.getCurrentItem() == 0) {
                Utilities.onSearchEventsClickAlle(getActivity());
            } else if(pager.getCurrentItem() == 1) {
                Utilities.onSearchMineEventer(getActivity());
            }

            once = true;
        }

        if(Bruker.get().getListOfMyEvents() != null) {
            if(!Bruker.get().getListOfMyEvents().isEmpty()) {
                lvmine_eventer.setAdapter(new EventAdapter(getActivity(), Bruker.get().getListOfMyEvents()));
            }
        }
    }
}