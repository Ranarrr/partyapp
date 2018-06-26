package com.partyspottr.appdir.classes;

import com.google.gson.reflect.TypeToken;
import com.partyspottr.appdir.enums.EventStilling;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 24-Feb-18.
 *
 * @author Ranarrr
 */

public class Participant {

    private String brukernavn;
    private String country;
    private String town;
    private EventStilling stilling;

    public static final Type type = new TypeToken<Participant>(){}.getType();

    private Participant() {}

    public Participant(String bruker, EventStilling eventStilling) {
        stilling = eventStilling;
        brukernavn = bruker;
    }

    public Participant(String bruker, String cntry, String twn, EventStilling eventstling) {
        brukernavn = bruker;
        country = cntry;
        town = twn;
        stilling = eventstling;
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

    public static Participant getParticipantByUsername(List<Participant> list, String username) {
        for (Participant temp : list) {
            if (temp.getBrukernavn().equals(username)) {
                return temp;
            }
        }

        return null;
    }

    public static int getParticipantPos(List<Participant> list, Participant participant) {
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(participant)) {
                return i;
            }
        }

        return -1;
    }

    public static Participant getBrukerInList(List<Participant> list) {
        for(Participant temp : list) {
            if(temp.getBrukernavn().equalsIgnoreCase(Bruker.get().getBrukernavn())) {
                return temp;
            }
        }

        return null;
    }

    public static List<Participant> SortParticipants(List<Participant> participants) {
        List<Participant> list = new ArrayList<>();

        for(Participant temp : participants) {

            if(temp == null)
                continue;

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
