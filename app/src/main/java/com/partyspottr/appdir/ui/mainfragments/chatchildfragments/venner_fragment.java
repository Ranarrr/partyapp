package com.partyspottr.appdir.ui.mainfragments.chatchildfragments;

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
import com.partyspottr.appdir.classes.adapters.FriendListAdapter;

/**
 * Created by Ranarrr on 15-Apr-18.
 *
 * @author Ranarrr
 */

public class venner_fragment extends Fragment {
    public static boolean once = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.venner_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager pager = view.findViewById(R.id.viewpager_chat);
        ListView lv_venner = view.findViewById(R.id.lv_venner);

        if(!once && pager != null) {
            if(pager.getCurrentItem() == 0) {
                Utilities.onSearchMineChats(getActivity());
            } else if(pager.getCurrentItem() == 1) {
                Utilities.onSearchVenner(getActivity());
            }

            once = true;
        }

        if(Bruker.get().getFriendList() != null)
            lv_venner.setAdapter(new FriendListAdapter(getActivity(), Bruker.get().getFriendList()));
    }
}
