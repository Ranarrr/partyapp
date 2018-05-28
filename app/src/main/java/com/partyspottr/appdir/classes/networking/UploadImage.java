package com.partyspottr.appdir.classes.networking;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.ui.ProfilActivity;

import java.io.File;

/**
 * Created by Ranarrr on 26-Feb-18.
 *
 * @author Ranarrr
 */

public class UploadImage extends AsyncTask<Void, Void, Void> {
    private File bitmap;
    private Event eventPriv;
    private ProgressDialog progressDialog;

    UploadImage(ProgressDialog pD, Event event, File bmp) {
        progressDialog = pD;
        eventPriv = event;
        bitmap = bmp;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();

        UploadTask uploadTask = ProfilActivity.storage.getReference().child(eventPriv.getHostStr() + "_" + eventPriv.getNameofevent()).putFile(Uri.fromFile(bitmap), metadata);

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

        return null;
    }
}
