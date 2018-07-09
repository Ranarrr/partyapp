package com.partyspottr.appdir.classes;

import android.graphics.Bitmap;
import android.net.Uri;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 26-Feb-18.
 *
 * @author Ranarrr
 */

public class ImageChange {
    private Uri uri;
    private Bitmap bmp;
    private List<PropertyChangeListener> listeners = new ArrayList<>();

    public ImageChange() {}

    private void notifyListeners(Object object, Bitmap oldvalue, Bitmap newvalue) {
        for(PropertyChangeListener image : listeners) {
            image.propertyChange(new PropertyChangeEvent(object, "bmp", oldvalue, newvalue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listeners.add(newListener);
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        notifyListeners(this, this.bmp, this.bmp = bmp);
    }
}
