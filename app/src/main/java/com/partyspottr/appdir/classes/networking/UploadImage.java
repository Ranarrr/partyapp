package com.partyspottr.appdir.classes.networking;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.ImageChange;
import com.partyspottr.appdir.ui.ProfilActivity;
import com.partyspottr.appdir.ui.other_ui.EventDetails;

import java.io.ByteArrayOutputStream;

/**
 * Created by Ranarrr on 26-Feb-18.
 *
 * @author Ranarrr
 */

public class UploadImage extends AsyncTask<Void, Void, Void> {
    private Bitmap bitmap;
    private Event eventPriv;
    private ProgressDialog progressDialog;

    UploadImage(ProgressDialog pD, Event event, @Nullable Bitmap bmp) {
        progressDialog = pD;
        eventPriv = event;
        bitmap = bmp;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();

        UploadTask uploadTask;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        uploadTask = ProfilActivity.storage.getReference().child(eventPriv.getHostStr() + "_" + eventPriv.getNameofevent()).putBytes(stream.toByteArray(), metadata);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(progressDialog.getContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(progressDialog.getContext(), "Uploaded image!", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });

        ProfilActivity.imageChange = new ImageChange();
        EventDetails.edit_event_imagechange = new ImageChange();

        return null;
    }
}
