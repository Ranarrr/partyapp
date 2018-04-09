package com.partyspottr.appdir.classes;

import java.util.Date;

/**
 * Created by Ranarrr on 25-Feb-18.
 *
 * @author Ranarrr
 */

public class Friend {
    private String brukernavn;
    private long FriendSince;

    public Friend() {}

    public Friend BrukerToFriend(Bruker bruker) {
        Friend result = new Friend();
        result.brukernavn = bruker.getBrukernavn();
        result.FriendSince = new Date().getTime();
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
}
