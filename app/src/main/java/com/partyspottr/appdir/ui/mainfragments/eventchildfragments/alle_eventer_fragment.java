package com.partyspottr.appdir.ui.mainfragments.eventchildfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.adapters.EventAdapter;
import com.partyspottr.appdir.classes.networking.GetAllEvents;

public class alle_eventer_fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.alle_eventer_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView listView = view.findViewById(R.id.lvalle_eventer);
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_layout_events);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Bruker.get().getListOfEvents() != null)
                    if(!Bruker.get().getListOfEvents().isEmpty())
                        listView.setAdapter(new EventAdapter(getActivity(), Bruker.get().getListOfEvents()));

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if(Bruker.get().getListOfEvents() != null) {
            if(!Bruker.get().getListOfEvents().isEmpty())
                listView.setAdapter(new EventAdapter(getActivity(), Bruker.get().getListOfEvents()));
        }
    }
}