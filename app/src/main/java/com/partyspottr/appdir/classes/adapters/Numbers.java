package com.partyspottr.appdir.classes.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.ui.MainActivity;

import java.util.Locale;

public class Numbers extends BaseAdapter {
    private Context m_Context;

    public Numbers( Context cxt )
    {
        super();

        m_Context = cxt;
    }

    public static final int[] m_Numbers = {
            0,
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8
    };


    @Override
    public int getCount() {
        return m_Numbers.length;
    }

    @Override
    public Object getItem(int position) {
        return m_Numbers[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if( convertView == null )
        {
            view = new TextView(m_Context);
            view.setPadding((int) m_Context.getResources().getDimension(R.dimen._10sdp), (int) m_Context.getResources().getDimension(R.dimen._10sdp), (int) m_Context.getResources().getDimension(R.dimen._10sdp),
                    (int) m_Context.getResources().getDimension(R.dimen._10sdp));
        }
        else
        {
            view = (TextView)convertView;
        }

        view.setTypeface(MainActivity.typeface);

        view.setText(String.format(Locale.ENGLISH, "%d", m_Numbers[position]));

        return view;
    }
}
