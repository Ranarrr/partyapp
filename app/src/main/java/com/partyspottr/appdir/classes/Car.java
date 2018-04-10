package com.partyspottr.appdir.classes;

import java.util.Date;

public class Car {
    private String merke;
    private String modell;
    private String farge;
    private boolean hasImg;
    private long timeCreated;

    public Car(String m_merke, String m_modell, String m_farge, boolean hasimg) {
        this(m_merke, m_modell, m_farge, hasimg, new Date().getTime());
    }

    public Car(String m_merke, String m_modell, String m_farge, boolean hasimg, long time) {
        merke = m_merke;
        modell = m_modell;
        farge = m_farge;
        hasImg = hasimg;
        timeCreated = time;
    }

    public String getMerke() {
        return merke;
    }

    public void setMerke(String merke) {
        this.merke = merke;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
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
