package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.partyspottr.appdir.R;

/**
 * Created by Ranarrr on 26-Mar-18.
 *
 * @author Ranarrr
 */

public class ChatMessageAdapter extends BaseAdapter {
    Activity thisActivity;

    public ChatMessageAdapter(Activity activity) {
        thisActivity = activity;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(inflater != null) {
                convertView = inflater.inflate(R.layout.chatmessageitem, parent, false);
            }
        }

        if(convertView != null) {

        }

        return convertView;
    }
}
