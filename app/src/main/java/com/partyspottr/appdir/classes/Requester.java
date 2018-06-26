package com.partyspottr.appdir.classes;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Ranarrr on 24-Feb-18.
 *
 * @author Ranarrr
 */

public class Requester {
    private String brukernavn;
    private String fornavn;
    private String etternavn;
    private String country;
    private String town;
    private boolean premium;
    private int day_of_month;
    private int month;
    private int year;

    public static final Type type = new TypeToken<Requester>(){}.getType();

    public Requester(String bruker, String forn, String ettern, String countr, String twn, boolean prem, int day, int mnth, int yr) {
        brukernavn = bruker;
        fornavn = forn;
        etternavn = ettern;
        town = twn;
        country = countr;
        premium = prem;
        day_of_month = day;
        month = mnth;
        year = yr;
    }

    public static Requester convertBrukerRequester(Bruker bruker) {
        return new Requester(bruker.getBrukernavn(), bruker.getFornavn(), bruker.getEtternavn(), bruker.getCountry(), bruker.getTown() == null ? "" : bruker.getTown(), bruker.isPremium(),
                bruker.getDay_of_month(), bruker.getMonth(), bruker.getYear());
    }

    public static void removeRequest(List<Requester> requests, String brukernavn) {
        for(int i = 0; i < requests.size(); i++) {
            if(requests.get(i).getBrukernavn().equals(brukernavn)) {
                requests.remove(i);
                return;
            }
        }
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

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
