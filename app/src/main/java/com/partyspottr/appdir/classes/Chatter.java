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

    public Chatter() {
        brukernavn = "";
        fornavn = "";
        etternavn = "";
    }

    public Chatter(String bruker, String fornvn, String ettrnavn) {
        brukernavn = bruker;
        fornavn = fornvn;
        etternavn = ettrnavn;
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
}
