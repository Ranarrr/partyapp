package com.partyspottr.appdir.ui.other_ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.adapters.ImageAdapter;
import com.takusemba.cropme.CropView;

public class CropImage extends AppCompatActivity {
    private static String[] strUrls = null;
    private String[] mNames = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image);

        GridView gridView = findViewById(R.id.gridview);
        final CropView cropView = findViewById(R.id.cropView);

        final Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);;

        if(cursor != null) {
            final Uri[] urls = new Uri[cursor.getCount()];
            strUrls = new String[cursor.getCount()];
            mNames = new String[cursor.getCount()];
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                urls[i] = Uri.parse(cursor.getString(1));
                strUrls[i] = cursor.getString(1);
                mNames[i] = cursor.getString(3);
            }

            gridView.setAdapter(new ImageAdapter(this, urls));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cropView.setUri(urls[position]);
                }
            });
        }
    }
}
