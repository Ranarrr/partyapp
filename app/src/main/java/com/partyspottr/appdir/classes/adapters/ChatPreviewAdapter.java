package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.ChatPreview;
import com.partyspottr.appdir.classes.Chatter;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.other_ui.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
        if(chatters == null || chatters.size() == 0) {
            chatterList = new ArrayList<>();
            chatterList.add(new ChatPreview("", BuildConfig.JSONParser_Socket, true, new ArrayList<Chatter>()));
        } else {
            chatterList = chatters;
        }

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
        final ChatPreview preview = getItem(position);

        if(preview.getGroupname().equals(BuildConfig.JSONParser_Socket) && preview.isGroupchat() && preview.getChatters().isEmpty() && preview.getMessage().isEmpty()) {
            LayoutInflater layoutInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(layoutInflater != null) {
                convertView = layoutInflater.inflate(R.layout.empty_eventlist, parent, false);
                TextView tom_liste = convertView.findViewById(R.id.empty_list);
                tom_liste.setTypeface(MainActivity.typeface);
                tom_liste.setText(thisActivity.getResources().getString(R.string.no_recent_chats));
                return convertView;
            }
        }

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

            navn.setTypeface(MainActivity.typeface);
            message.setTypeface(MainActivity.typeface);
            time.setTypeface(MainActivity.typeface);

            if(preview.getChatters().size() > 0) {
                if(!preview.isGroupchat()) {
                    Chatter other_chatter = Chatter.getChatterNotEqualToBruker(preview.getChatters());

                    if(other_chatter != null)
                        navn.setText(String.format(Locale.ENGLISH, "%s %s", other_chatter.getFornavn(), other_chatter.getEtternavn()));
                } else
                    navn.setText(String.format(Locale.ENGLISH, "%s", preview.getGroupname()));
            }

            if(preview.getMessage().length() > 25) {
                message.setText(String.format(Locale.ENGLISH, "%s...", preview.getMessage().substring(0, 25)));
            } else {
                message.setText(preview.getMessage());
            }

            if(preview.getLastmsg() > 0) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(preview.getLastmsg());

                switch(Utilities.getDateStrChat(calendar)) {
                    case 1:
                        time.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(preview.getLastmsg()));
                        break;

                    case 2:
                        time.setText(thisActivity.getResources().getString(R.string.i_går));
                        break;

                    case 3:
                        time.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
                        break;

                    case 4:
                        time.setText(String.format(Locale.ENGLISH, "%d. %s", calendar.get(Calendar.DAY_OF_MONTH), calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())));
                        break;

                    case 5:
                        time.setText(String.format(Locale.ENGLISH, "%d. %s %d", calendar.get(Calendar.DAY_OF_MONTH), calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()),
                                calendar.get(Calendar.YEAR)));
                        break;

                        default:
                            time.setText("");
                            break;
                }

            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(thisActivity, Chat.class);
                    intent.putExtra("chatter1", preview.getChatters().get(0).getBrukernavn());
                    intent.putExtra("chatter2", preview.getChatters().get(1).getBrukernavn());
                    thisActivity.startActivity(intent);
                }
            });
        }

        return convertView;
    }
}