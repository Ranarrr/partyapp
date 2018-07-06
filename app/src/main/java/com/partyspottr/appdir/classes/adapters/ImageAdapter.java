package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.application.GlideApp;

public class ImageAdapter extends BaseAdapter {
    private Activity thisActivity;
    private Uri[] urls;
    private Bitmap[] bmps;

    public ImageAdapter(Activity a, Uri[] uris) {
        thisActivity = a;
        urls = uris;
        bmps = new Bitmap[uris.length];
    }

    public int getCount() {
        return urls.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;

        if(convertView == null) {
            imageView = new ImageView(thisActivity);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) thisActivity.getResources().getDimension(R.dimen._60sdp)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            thisActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GlideApp.with(thisActivity).load(urls[position].toString()).into(imageView);
                }
            });

            new Thread() {
                @Override
                public void run() {
                    Bitmap bmp = Utilities.decodeSampledBitmapFromResource(urls[position].toString(), 256, 64);
                    bmps[position] = bmp;
                }
            }.start();
        } else {
            imageView = (ImageView) convertView;
            if(bmps[position] == null) {
                thisActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GlideApp.with(thisActivity).load(urls[position].toString()).into(imageView);
                    }
                });

                new Thread() {
                    @Override
                    public void run() {
                        Bitmap bmp = Utilities.decodeSampledBitmapFromResource(urls[position].toString(), 256, 64);
                        bmps[position] = bmp;
                    }
                }.start();
            } else
                imageView.setImageBitmap(bmps[position]);
        }

        return imageView;
    }
}