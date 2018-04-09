package com.partyspottr.appdir.ui.mainfragments.bilchildfragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Chauffeur;
import com.partyspottr.appdir.classes.adapters.ChauffeurAdapter;

import java.util.ArrayList;

public class finn_bil_fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.passasjer_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView lv_chauffeurs = getActivity().findViewById(R.id.lv_chauffeurs);

        lv_chauffeurs.setAdapter(new ChauffeurAdapter(getActivity(), new ArrayList<Chauffeur>(), lv_chauffeurs));
    }
}
