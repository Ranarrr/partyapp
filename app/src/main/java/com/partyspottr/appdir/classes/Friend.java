package com.partyspottr.appdir.classes;

import android.support.annotation.Nullable;

import java.util.Calendar;

/**
 * Created by Ranarrr on 25-Feb-18.
 *
 * @author Ranarrr
 */

public class Friend {

    private String brukernavn;
    private Calendar FriendSince;

    public Friend() {}

    public Friend BrukerToFriend(Bruker bruker, @Nullable Calendar friendSince) {
        Friend result = new Friend();
        result.brukernavn = bruker.getBrukernavn();
        result.FriendSince = friendSince;
        return result;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public Calendar getFriendSince() {
        return FriendSince;
    }

    public void setFriendSince(Calendar friendSince) {
        FriendSince = friendSince;
    }
}
