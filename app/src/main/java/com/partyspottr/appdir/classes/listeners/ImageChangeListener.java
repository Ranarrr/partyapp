package com.partyspottr.appdir.classes.listeners;

import com.partyspottr.appdir.classes.ImageChange;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by Ranarrr on 26-Feb-18.
 *
 * @author Ranarrr
 */

public class ImageChangeListener implements PropertyChangeListener {

    public ImageChangeListener(ImageChange imageChange) {
        imageChange.addChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Changed property: " + evt.getPropertyName() + " [old -> "
                + evt.getOldValue() + "] | [new -> " + evt.getNewValue() +"]");
    }
}
