package com.partyspottr.appdir.classes;

/*
 * Created by Ranarrr on 24-Feb-18.
 */

import com.partyspottr.appdir.enums.EventStilling;

import java.util.ArrayList;
import java.util.List;

public class Participant {

    private String brukernavn;
    private String country;
    private String town;
    private EventStilling stilling;

    private Participant() {}

    public Participant(String bruker, EventStilling eventStilling) {
        stilling = eventStilling;
        brukernavn = bruker;
    }

    public static Participant convertBrukerParticipant(Bruker bruker, EventStilling eventStilling) {
        Participant result = new Participant();

        result.brukernavn = bruker.getBrukernavn();
        result.stilling = eventStilling;
        result.country = bruker.getCountry();
        if(bruker.getTown() != null) {
            result.town = bruker.getTown();
        }

        return result;
    }

    public static List<Participant> SortParticipants(List<Participant> participants) {
        List<Participant> list = new ArrayList<>();

        for(Participant temp : participants) {

            /*if(Bruker.get().isParticipantInFriendlist(temp)) {
                list.add(temp);
                continue;
            }*/

            switch(temp.stilling) {
                case VERT:
                    list.add(temp);
                    break;

                case MODERATOR:
                    list.add(temp);
                    break;

                case VEKTER:
                    list.add(temp);
                    break;

                case PREMIUM:
                    list.add(temp);
                    break;

                case GJEST:
                    list.add(temp);
                    break;
            }
        }

        return list;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public EventStilling getStilling() {
        return stilling;
    }

    public void setStilling(EventStilling stilling) {
        this.stilling = stilling;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
