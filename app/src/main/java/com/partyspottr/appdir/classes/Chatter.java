package com.partyspottr.appdir.classes;

import java.util.List;

/**
 * Created by Ranarrr on 20-Mar-18.
 *
 * @author Ranarrr
 */

public class Chatter {
    private String brukernavn;
    private String fornavn;
    private String etternavn;
    private String token;

    public Chatter() {
        brukernavn = "";
        fornavn = "";
        etternavn = "";
        token = "";
    }

    public Chatter(String bruker, String fornvn, String ettrnavn, String tokn) {
        brukernavn = bruker;
        fornavn = fornvn;
        etternavn = ettrnavn;
        token = tokn;
    }

    public static Chatter getChatterNotEqualToBruker(List<Chatter> list) {
        for(Chatter chatter : list) {
            if(!chatter.getBrukernavn().equals(Bruker.get().getBrukernavn())) {
                return chatter;
            }
        }

        return null;
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

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
