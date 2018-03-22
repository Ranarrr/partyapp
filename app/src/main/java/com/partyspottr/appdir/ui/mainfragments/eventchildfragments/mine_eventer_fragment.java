package com.partyspottr.appdir.ui.mainfragments.eventchildfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.adapters.EventAdapter;

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

        ListView lvmine_eventer = getActivity().findViewById(R.id.lvmine_eventer);

        if(Bruker.get().getListOfMyEvents() != null) {
            if(!Bruker.get().getListOfMyEvents().isEmpty()) {
                lvmine_eventer.setAdapter(new EventAdapter(getActivity(), Bruker.get().getListOfMyEvents()));
            }
        }
    }
}