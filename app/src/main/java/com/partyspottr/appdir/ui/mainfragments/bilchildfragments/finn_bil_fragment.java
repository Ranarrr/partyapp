package com.partyspottr.appdir.ui.mainfragments.bilchildfragments;

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
import com.partyspottr.appdir.classes.adapters.ChauffeurAdapter;
import com.partyspottr.appdir.classes.networking.GetAllChauffeurs;

/**
 * Created by Ranarrr on 30-Jan-18.
 *
 * @author Ranarrr
 */

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
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_chauffeurs);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetAllChauffeurs getAllChauffeurs = new GetAllChauffeurs(getActivity());
                getAllChauffeurs.execute();
            }
        });

        if(Bruker.get().getListchauffeurs() != null && Bruker.get().getListchauffeurs().size() > 0)
            lv_chauffeurs.setAdapter(new ChauffeurAdapter(getActivity(), Bruker.get().getListchauffeurs()));
        else {
            GetAllChauffeurs getAllChauffeurs = new GetAllChauffeurs(getActivity());
            getAllChauffeurs.execute();
        }
    }
}
