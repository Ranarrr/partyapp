package com.partyspottr.appdir.classes;

/**
 * Created by Ranarrr on 20-Mar-18.
 *
 * @author Ranarrr
 */

public class Chatter {
    private long userId;
    private String fornavn;
    private String etternavn;

    public Chatter() {

    }

    private Chatter(long userid, String fornvn, String ettrnavn) {
        userId = userid;
        fornavn = fornvn;
        etternavn = ettrnavn;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
}
