package com.partyspottr.appdir.classes;

import java.util.Date;
import java.util.List;

public class Car {
    private String merke;
    private String farge;
    private boolean hasImg;
    private long timeCreated;

    public Car() {
        this("", "", false, 0);
    }

    public Car(String m_merke, String m_farge, boolean hasimg) {
        this(m_merke, m_farge, hasimg, new Date().getTime());
    }

    public Car(String m_merke, String m_farge, boolean hasimg, long time) {
        merke = m_merke;
        farge = m_farge;
        hasImg = hasimg;
        timeCreated = time;
    }

    public static Car getCarFromList(List<Car> list, String farge, String merke) {
        for(Car car : list) {
            if(car.getFarge().equalsIgnoreCase(farge) && car.getMerke().equalsIgnoreCase(merke))
                return car;
        }

        return null;
    }

    public String getMerke() {
        return merke;
    }

    public void setMerke(String merke) {
        this.merke = merke;
    }

    public String getFarge() {
        return farge;
    }

    public void setFarge(String farge) {
        this.farge = farge;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public boolean isHasImg() {
        return hasImg;
    }

    public void setHasImg(boolean hasImg) {
        this.hasImg = hasImg;
    }
}
