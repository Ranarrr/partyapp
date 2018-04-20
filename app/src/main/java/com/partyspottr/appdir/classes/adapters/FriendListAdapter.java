package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Friend;
import com.partyspottr.appdir.ui.other_ui.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FriendListAdapter extends BaseAdapter {
    private Activity thisActivity;
    private List<Friend> friendList;

    public FriendListAdapter(Activity activity, List<Friend> list) {
        thisActivity = activity;

        if(list.size() == 0) {
            friendList = new ArrayList<>();
            friendList.add(new Friend("££££"));
        } else {
            friendList = list;
        }
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Friend friend = friendList.get(position);

        if(friendList.get(position).getBrukernavn().equals("££££")) {
            LayoutInflater inflater = thisActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.empty_eventlist, parent, false);
            ((TextView) convertView.findViewById(R.id.empty_list)).setText("You do not have any friends :(");
            return convertView;
        }

        if(convertView == null) {
            LayoutInflater inflater = thisActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.friend, parent, false);
        }

        if(convertView != null) {
            TextView bruker = convertView.findViewById(R.id.venn_brukernavn);
            TextView fullt_navn = convertView.findViewById(R.id.venn_fullt_navn);

            bruker.setText(friend.getBrukernavn());
            fullt_navn.setText(String.format(Locale.ENGLISH, "%s %s", friend.getFornavn(), friend.getEtternavn()));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(thisActivity, Profile.class);
                    intent.putExtra("user", friend.getBrukernavn());
                    thisActivity.startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
