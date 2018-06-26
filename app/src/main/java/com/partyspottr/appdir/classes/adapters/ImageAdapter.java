package com.partyspottr.appdir.classes.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.partyspottr.appdir.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Uri[] urls;

    public ImageAdapter(Context c, Uri[] uris) {
        mContext = c;
        urls = uris;
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

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext.getResources().getDimension(R.dimen._60sdp)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else
            imageView = (ImageView) convertView;

        //String str = Utilities.getPathFromUri(mContext, urls[position]);

        //if(str != null)
        imageView.setImageBitmap(BitmapFactory.decodeFile(urls[position].toString()));

        return imageView;
    }
}