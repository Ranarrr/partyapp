package com.partyspottr.appdir.ui.mainfragments.bilchildfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.adapters.ChauffeurAdapter;

/**
 * Created by Ranarrr on 30-Jan-18.
 *
 * @author Ranarrr
 */

public class finn_bil_fragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.passasjer_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView lv_chauffeurs = view.findViewById(R.id.lv_chauffeurs);
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_chauffeurs);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Bruker.get().StopParsingChauffeurs();
                Bruker.get().GetAndParseChauffeurs(getActivity());

                if(lv_chauffeurs != null)
                    lv_chauffeurs.setAdapter(new ChauffeurAdapter(getActivity(), Bruker.get().getListchauffeurs()));

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if(lv_chauffeurs != null)
            lv_chauffeurs.setAdapter(new ChauffeurAdapter(getActivity(), Bruker.get().getListchauffeurs()));
    }
}
