package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.application.GlideApp;
import com.partyspottr.appdir.classes.application.MyGlideApp;

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