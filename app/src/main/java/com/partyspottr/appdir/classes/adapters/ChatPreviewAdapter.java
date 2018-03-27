package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.ChatPreview;

import java.util.List;
import java.util.Locale;

/**
 * Created by Ranarrr on 20-Mar-18.
 *
 * @author Ranarrr
 */

public class ChatPreviewAdapter extends BaseAdapter {
    private Activity thisActivity;
    private List<ChatPreview> chatterList;

    // chatters is a list of chats you have previously had with others.
    public ChatPreviewAdapter(Activity activity, List<ChatPreview> chatters) {
        chatterList = chatters;
        thisActivity = activity;
    }

    @Override
    public int getCount() {
        return chatterList.size();
    }

    @Override
    public ChatPreview getItem(int position) {
        return chatterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatPreview preview = getItem(position);

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

            if(preview.getChatters().size() > 0) {
                if(preview.isGroupchat())
                    navn.setText(String.format(Locale.ENGLISH, "%s %s", preview.getChatters().get(0).getFornavn(), preview.getChatters().get(0).getEtternavn()));
                else
                    navn.setText(String.format(Locale.ENGLISH, "%s", preview.getGroupname()));
            }

            if(preview.getMessage().length() > 20) {
                message.setText(String.format(Locale.ENGLISH, "%s...", preview.getMessage().substring(0, 20)));
            } else {
                message.setText(preview.getMessage());
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(thisActivity);

                    dialog.requestWindowFeature(1);

                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);

                    dialog.setContentView(R.layout.chatmessage);

                    ListView lv_chatmessages = dialog.findViewById(R.id.lv_chatmessages);

                    FirebaseListAdapter adapter = new FirebaseListAdapter<ChatMessage>() {
                        @Override
                        protected void populateView(View v, Object model) {

                        }
                    }

                    lv_chatmessages.setAdapter(new );
                }
            });
        }

        return convertView;
    }
}
