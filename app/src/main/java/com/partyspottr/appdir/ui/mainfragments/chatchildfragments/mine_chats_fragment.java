package com.partyspottr.appdir.ui.mainfragments.chatchildfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.ChatPreview;
import com.partyspottr.appdir.classes.adapters.ChatPreviewAdapter;

import java.util.ArrayList;

public class mine_chats_fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mine_chats_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView lv_chat = view.findViewById(R.id.lv_chat);

        if(Bruker.get().getChatMessageList() != null && Bruker.get().getChatMessageList().isEmpty())
            lv_chat.setAdapter(new ChatPreviewAdapter(getActivity(), new ArrayList<ChatPreview>()));
        else
            lv_chat.setAdapter(new ChatPreviewAdapter(getActivity(), Bruker.get().getChatMessageList()));
    }
}
