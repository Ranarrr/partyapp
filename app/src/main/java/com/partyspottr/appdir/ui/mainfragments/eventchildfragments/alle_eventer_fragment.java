package com.partyspottr.appdir.ui.mainfragments.eventchildfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.partyspottr.appdir.classes.adapters.EventAdapter;
import com.partyspottr.appdir.classes.networking.GetAllEvents;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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
                GetAllEvents getAllEvents = new GetAllEvents(getContext(), getActivity());
                getAllEvents.execute();
            }
        });

        ImageButton searchevents = getActivity().findViewById(R.id.search_events);
        final EditText søk_alle_eventer = view.findViewById(R.id.søk_alle_eventer);

        searchevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bruker.get().getListOfEvents() != null && !Bruker.get().getListOfEvents().isEmpty()) {
                    søk_alle_eventer.setVisibility(søk_alle_eventer.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                    ViewGroup.LayoutParams params = søk_alle_eventer.getLayoutParams();

                    if(søk_alle_eventer.getVisibility() == View.INVISIBLE)
                        params.height = 0;
                    else {
                        params.height = WRAP_CONTENT;

                        søk_alle_eventer.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(s.toString().isEmpty()) {
                                    listView.setAdapter(new EventAdapter(getActivity(), Bruker.get().getListOfEvents()));
                                    return;
                                }

                                List<Event> list = new ArrayList<>();
                                for(Event event : Bruker.get().getListOfEvents()) {
                                    if(event.getHostStr().contains(s))
                                        list.add(event);

                                    if(event.getNameofevent().contains(s))
                                        list.add(event);
                                }

                                listView.setAdapter(new EventAdapter(getActivity(), list));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });
                    }

                    søk_alle_eventer.setLayoutParams(params);
                } else {
                    Toast.makeText(getContext(), "The list is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(Bruker.get().getListOfEvents() != null) {
            if(!Bruker.get().getListOfEvents().isEmpty()) {
                listView.setAdapter(new EventAdapter(getActivity(), Bruker.get().getListOfEvents()));
            } else {
                GetAllEvents getAllEvents = new GetAllEvents(getContext(), getActivity());
                getAllEvents.execute();
            }
        }
    }
}
