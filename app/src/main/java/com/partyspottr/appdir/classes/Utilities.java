package com.partyspottr.appdir.classes;

/*
 * Created by Ranarrr on 02-Feb-18.
 */

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.adapters.EventAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class Utilities {
    public static boolean hasNetwork(Context c) {
        if(Settings.Global.getInt(c.getContentResolver(), "airplane_mode_on", 0) != 0)
            return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
            return false;

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public static GregorianCalendar getDateFromString(String str, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ENGLISH);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        try {
            gregorianCalendar.setTime(simpleDateFormat.parse(str));
            return gregorianCalendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void onSearchEventsClickAlle(final Activity activity) {
        final ListView listView = activity.findViewById(R.id.lvalle_eventer);
        ImageButton searchevents = activity.findViewById(R.id.search_events);
        final EditText søk_alle_eventer = activity.findViewById(R.id.søk_alle_eventer);

        if(listView == null || searchevents == null || søk_alle_eventer == null)
            return;

        searchevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bruker.get().getListOfEvents() != null) {
                    if(!Bruker.get().getListOfEvents().isEmpty()) {
                        søk_alle_eventer.setVisibility(søk_alle_eventer.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                        ViewGroup.LayoutParams params = søk_alle_eventer.getLayoutParams();

                        if(søk_alle_eventer.getVisibility() == View.INVISIBLE)
                            params.height = 0;
                        else {
                            params.height = WRAP_CONTENT;

                            søk_alle_eventer.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    if(s.toString().isEmpty()) {
                                        listView.setAdapter(new EventAdapter(activity, Bruker.get().getListOfEvents()));
                                        return;
                                    }

                                    List<Event> list = new ArrayList<>();
                                    for(Event event : Bruker.get().getListOfEvents()) {
                                        if(event.getHostStr().contains(s))
                                            list.add(event);

                                        if(event.getNameofevent().contains(s))
                                            list.add(event);
                                    }

                                    listView.setAdapter(new EventAdapter(activity, list));
                                }

                                @Override
                                public void afterTextChanged(Editable s) {}
                            });
                        }

                        søk_alle_eventer.setLayoutParams(params);
                    } else {
                        Toast.makeText(activity, "The list is empty!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static int calcAge(Calendar DOB) {
        int result;

        Calendar now = Calendar.getInstance();

        int year1 = now.get(Calendar.YEAR);
        int year2 = DOB.get(Calendar.YEAR);
        result = year1 - year2;
        int month1 = now.get(Calendar.MONTH) + 1;
        int month2 = DOB.get(Calendar.MONTH);
        if (month2 > month1) {
            result--;
        } else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = DOB.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                result--;
            }
        }

        return result;
    }

    /*private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(byte[] bmp, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bmp, 0, bmp.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bmp, 0, bmp.length, options);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }*/
}