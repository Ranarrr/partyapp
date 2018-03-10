package com.partyspottr.appdir.ui.mainfragments.eventchildfragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class mine_eventer_fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mine_eventer_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView lvmine_eventer = view.findViewById(R.id.lvmine_eventer);
        ImageButton search_events = getActivity().findViewById(R.id.search_events);
        final EditText søk_mine_eventer = view.findViewById(R.id.søk_mine_eventer);

        search_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bruker.get().getListOfMyEvents() != null && !Bruker.get().getListOfMyEvents().isEmpty()) {
                    søk_mine_eventer.setVisibility(søk_mine_eventer.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                    ViewGroup.LayoutParams params = søk_mine_eventer.getLayoutParams();

                    if(søk_mine_eventer.getVisibility() == View.INVISIBLE)
                        params.height = 0;
                    else {
                        params.height = WRAP_CONTENT;

                        søk_mine_eventer.addTextChangedListener(new TextWatcher() {
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
                    Toast.makeText(getContext(), "The list is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(Bruker.get().getListOfMyEvents() != null) {
            if(!Bruker.get().getListOfMyEvents().isEmpty()) {
                lvmine_eventer.setAdapter(new EventAdapter(getActivity(), Bruker.get().getListOfMyEvents()));
            }
        }
    }
}
