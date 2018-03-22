package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.ChatMessage;

import java.util.List;
import java.util.Locale;

/**
 * Created by Ranarrr on 20-Mar-18.
 *
 * @author Ranarrr
 */

public class ChatAdapter extends BaseAdapter {
    private Activity thisActivity;
    private List<ChatMessage> chatterList;

    public ChatAdapter(Activity activity, List<ChatMessage> chatters) {
        chatterList = chatters;
        thisActivity = activity;
    }

    @Override
    public int getCount() {
        return chatterList.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        return chatterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chat = getItem(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(inflater != null) {
                convertView = inflater.inflate(R.layout.chat, parent, false);
            }
        }

        if(convertView != null) {
            TextView navn = convertView.findViewById(R.id.chatnavn);
            TextView message = convertView.findViewById(R.id.chatmessage);
            TextView time = convertView.findViewById(R.id.chattime);

            if(chat.getChatters().size() > 0) {
                navn.setText(String.format(Locale.ENGLISH, "%s %s", chat.getChatters().get(0).getFornavn(), chat.getChatters().get(0).getEtternavn()));
            }

            if(chat.getMessage().length() > 20) {
                message.setText(String.format(Locale.ENGLISH, "%s...", chat.getMessage().substring(0, 20)));
            } else {
                message.setText(chat.getMessage());
            }
        }

        return convertView;
    }
}
