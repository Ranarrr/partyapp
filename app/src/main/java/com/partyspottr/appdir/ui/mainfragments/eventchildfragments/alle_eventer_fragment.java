package com.partyspottr.appdir.ui.mainfragments.eventchildfragments;

import android.os.Bundle;
import android.os.Handler;
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

        swipeRefreshLayout.setRefreshing(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Bruker.get().StopParsingEvents();
                Bruker.get().GetAndParseEvents(getActivity());

                if(listView != null)
                    listView.setAdapter(new EventAdapter(getActivity(), Bruker.get().getListOfEvents()));

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(listView != null)
                    listView.setAdapter(new EventAdapter(getActivity(), Bruker.get().getListOfEvents()));

                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}