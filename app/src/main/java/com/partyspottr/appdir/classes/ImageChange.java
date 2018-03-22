package com.partyspottr.appdir.classes;

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

    private File image;
    private Uri uri;
    private List<PropertyChangeListener> listeners = new ArrayList<>();

    public ImageChange() {}

    private void notifyListeners(Object object, File oldvalue, File newvalue) {
        for(PropertyChangeListener image : listeners) {
            image.propertyChange(new PropertyChangeEvent(object, "image", oldvalue, newvalue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listeners.add(newListener);
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        notifyListeners(this, this.image, this.image = image);
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
