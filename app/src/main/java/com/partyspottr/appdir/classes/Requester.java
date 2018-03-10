package com.partyspottr.appdir.classes;

/*
 * Created by Ranarrr on 24-Feb-18.
 */

public class Requester {

    private String brukernavn;
    private String fornavn;
    private String etternavn;
    private String country;
    private boolean premium;
    private int day_of_month;
    private int month;
    private int year;

    Requester() {}

    public Requester(String bruker, String forn, String ettern, String countr, boolean prem, int day, int mnth, int yr) {
        brukernavn = bruker;
        fornavn = forn;
        etternavn = ettern;
        country = countr;
        premium = prem;
        day_of_month = day;
        month = mnth;
        year = yr;
    }


    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public int getDay_of_month() {
        return day_of_month;
    }

    public void setDay_of_month(int day_of_month) {
        this.day_of_month = day_of_month;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
