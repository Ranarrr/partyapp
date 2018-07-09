package com.partyspottr.appdir.ui.other_ui;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.ImageAdapter;
import com.partyspottr.appdir.enums.ReturnWhere;
import com.partyspottr.appdir.ui.ProfilActivity;
import com.takusemba.cropme.CropView;
import com.takusemba.cropme.OnCropListener;

import java.io.File;

public class CropImage extends AppCompatActivity {
    private String selectedUri = null;
    private int returnwhere;

    @Override
    protected void onStop() {
        Utilities.setupOnStop(this);

        super.onStop();
    }

    @Override
    protected void onRestart() {
        if(!Bruker.get().isConnected()) {
            super.onRestart();
            return;
        }

        Utilities.setupOnRestart(this);

        super.onRestart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image);

        Bundle extras = getIntent().getExtras();
        if(extras == null)
            return;
        else {
            returnwhere = extras.getInt("returnwhere");

            if(returnwhere == -1)
                return;
        }

        GridView gridView = findViewById(R.id.gridview);
        final CropView cropView = findViewById(R.id.cropView);
        ImageButton delete = findViewById(R.id.crop_img_delete);
        ImageButton accept = findViewById(R.id.crop_img_accept);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedUri == null) {
                    onBackPressed();
                    return;
                }

                cropView.setBitmap(null);
                selectedUri = null;
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedUri != null) {
                    cropView.crop(new OnCropListener() {
                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            if(bitmap.getByteCount() > (2048 * 2048))
                                Toast.makeText(CropImage.this, "This image is too big! Max 4Mb.", Toast.LENGTH_SHORT).show();
                            else {
                                if(returnwhere == ReturnWhere.LEGG_TIL_EVENT.ordinal()) {
                                    ProfilActivity.imageChange.setBmp(bitmap);
                                    ProfilActivity.imageChange.setUri(Uri.parse(selectedUri));
                                } else {
                                    EventDetails.edit_event_imagechange.setBmp(bitmap);
                                    EventDetails.edit_event_imagechange.setUri(Uri.parse(selectedUri));
                                }

                                onBackPressed();
                            }
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(CropImage.this, "Failed to crop this image!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    if(returnwhere == ReturnWhere.LEGG_TIL_EVENT.ordinal()) {
                        ProfilActivity.imageChange.setBmp(null);
                        ProfilActivity.imageChange.setUri(null);
                    } else {
                        EventDetails.edit_event_imagechange.setBmp(null);
                        EventDetails.edit_event_imagechange.setUri(null);
                    }

                    onBackPressed();
                }
            }
        });

        final Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        if(cursor != null) {
            final Uri[] urls = new Uri[cursor.getCount()];
            cursor.moveToFirst();
            int j = 0;
            for (int i = cursor.getCount() - 1; i > -1; i--) {
                cursor.moveToPosition(i);
                urls[j] = Uri.parse(cursor.getString(1));
                j++;
            }

            cursor.close();

            if(urls.length == 0) {
                TextView empty_list = findViewById(R.id.crop_img_empty_list);
                empty_list.setVisibility(View.VISIBLE);
            } else {
                gridView.setAdapter(new ImageAdapter(this, urls));

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bitmap scaledbmp = Utilities.decodeSampledBitmapFromResource(urls[position].toString(), 640, 256);
                        scaledbmp = Utilities.correctOrientation(urls[position].toString(), scaledbmp);
                        cropView.setBitmap(scaledbmp);
                        selectedUri = urls[position].toString();
                    }
                });
            }
        }
    }
}
