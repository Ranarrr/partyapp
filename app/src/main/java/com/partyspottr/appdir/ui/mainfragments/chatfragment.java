package com.partyspottr.appdir.ui.mainfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.ChatPreview;
import com.partyspottr.appdir.classes.adapters.ChatPreviewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 20-Mar-18.
 *
 * @author Ranarrr
 */

public class chatfragment extends Fragment {
    public static ValueEventListener valueEventListener;
    public static DatabaseReference ref;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chatfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView lv_chat = getActivity().findViewById(R.id.lv_chat);

        if(Bruker.get().getChatMessageList().isEmpty())
            lv_chat.setAdapter(new ChatPreviewAdapter(getActivity(), new ArrayList<ChatPreview>()));
        else
            lv_chat.setAdapter(new ChatPreviewAdapter(getActivity(), Bruker.get().getChatMessageList()));

        ref = FirebaseDatabase.getInstance().getReference().child("messagepreviews");

        valueEventListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ChatPreview> list = new ArrayList<>();

                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.getKey().contains(Bruker.get().getBrukernavn())) {
                        list.add(child.getValue(ChatPreview.class));
                    }
                }

                lv_chat.setAdapter(new ChatPreviewAdapter(getActivity(), list));
                Bruker.get().setChatMessageList(list);
                Bruker.get().LagreBruker();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(getContext() != null && databaseError.getCode() == DatabaseError.NETWORK_ERROR)
                    Toast.makeText(getContext(), "Could not find chats.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
