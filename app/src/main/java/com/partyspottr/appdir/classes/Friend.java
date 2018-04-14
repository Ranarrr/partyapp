package com.partyspottr.appdir.classes;

import java.util.Date;

/**
 * Created by Ranarrr on 25-Feb-18.
 *
 * @author Ranarrr
 */

public class Friend {
    private String brukernavn;
    private String fornavn;
    private String etternavn;
    private long FriendSince;

    public Friend() {}

    public static Friend BrukerToFriend(Bruker bruker) {
        Friend result = new Friend();
        result.brukernavn = bruker.getBrukernavn();
        result.FriendSince = new Date().getTime();
        result.fornavn = bruker.getFornavn();
        result.etternavn = bruker.getEtternavn();
        return result;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public long getFriendSince() {
        return FriendSince;
    }

    public void setFriendSince(long friendSince) {
        FriendSince = friendSince;
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
