package com.partyspottr.appdir.ui.mainfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.adapters.ChatAdapter;

/**
 * Created by Ranarrr on 20-Mar-18.
 *
 * @author Ranarrr
 */

public class chatfragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chatfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView lv_chat = getActivity().findViewById(R.id.lv_chat);
        lv_chat.setAdapter(new ChatAdapter(getActivity(), Bruker.get().getChatMessageList()));
    }
}
