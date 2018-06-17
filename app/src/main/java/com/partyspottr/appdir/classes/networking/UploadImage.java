package com.partyspottr.appdir.classes.networking;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.ImageChange;
import com.partyspottr.appdir.ui.ProfilActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Ranarrr on 26-Feb-18.
 *
 * @author Ranarrr
 */

public class UploadImage extends AsyncTask<Void, Void, Void> {
    private File bitmapfile;
    private Bitmap bitmap;
    private boolean isfile;
    private Event eventPriv;
    private ProgressDialog progressDialog;

    public UploadImage(ProgressDialog pD, Event event, @Nullable File file, @Nullable Bitmap bmp) {
        progressDialog = pD;
        eventPriv = event;

        isfile = true;

        if(file == null) {
            bitmap = bmp;
            isfile = false;
        } else
            bitmapfile = file;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();

        UploadTask uploadTask;

        if(isfile)
            uploadTask = ProfilActivity.storage.getReference().child(eventPriv.getHostStr() + "_" + eventPriv.getNameofevent()).putFile(Uri.fromFile(bitmapfile), metadata);
        else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            uploadTask = ProfilActivity.storage.getReference().child(eventPriv.getHostStr() + "_" + eventPriv.getNameofevent()).putBytes(stream.toByteArray(), metadata);
        }

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(progressDialog.getContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(progressDialog.getContext(), progressDialog.getContext().getResources().getString(R.string.event_lagt_til), Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });

        ProfilActivity.imageChange = new ImageChange();

        return null;
    }
}
