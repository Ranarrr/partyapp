package com.partyspottr.appdir.classes;

/*
 * Created by Ranarrr on 26-Jan-18.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Telephony;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.classes.networking.getUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Bruker {
    private String brukernavn;
    private String passord;
    private String fornavn;
    private String etternavn;
    private String email;
    private boolean harakseptert;
    private boolean premium;
    private boolean loggetpa;
    private boolean isConnected;
    private int day_of_month;
    private int month;
    private int year;
    private String country;
    private String mobilnummer;
    private String town;
    private List<Friend> friendList;
    private List<Friend> requests;
    private List<Event> listOfMyEvents;
    private List<Event> listOfEvents;
    private String oneliner;
    private Calendar created;

    private static final String brukernavnElem = "brukernavnElem";
    private static final String fornavnElem = "fornavnElem";
    private static final String emailElem = "emailElem";
    private static final String passordElem = "passordElem";
    private static final String harakseptertElem = "harakseptertElem";
    private static final String etternavnElem = "etternavnElem";
    private static final String premiumElem = "premiumElem";
    private static final String loggedonElem = "loggedonElem";
    private static final String countryElem = "countryElem";
    private static final String listMyEventsElem = "listMyEventsElem";
    private static final String day_of_monthElem = "day_of_monthElem";
    private static final String friendlistElem = "friendlistElem";
    private static final String requestlistElem = "requestlistElem";
    private static final String monthElem = "monthElem";
    private static final String townElem = "townElem";
    private static final String yearElem = "yearElem";
    private static final String createdElem = "createdElem";
    private static final String mobilElem = "mobilElem";
    private static final String socketElem = "socketElem";

    private static final Type listEventsType = new TypeToken<List<Event>>(){}.getType();
    private static final Type listFriendsType = new TypeToken<List<Friend>>(){}.getType();

    private static Bruker lokalBruker;

    private SharedPreferences sharedPreferences;

    public static Bruker get() {
        if(lokalBruker == null) {
            lokalBruker = new Bruker();
        }

        return lokalBruker;
    }

    public void Init(Context c) {
        lokalBruker.sharedPreferences = c.getSharedPreferences("Bruker", Context.MODE_PRIVATE);
        HentBruker();
    }

    public void LagreBruker() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(brukernavnElem, brukernavn);
        editor.putString(passordElem, passord);
        editor.putBoolean(harakseptertElem, harakseptert);
        editor.putString(listMyEventsElem, new Gson().toJson(listOfMyEvents));
        editor.putString(fornavnElem, fornavn);
        editor.putString(etternavnElem, etternavn);
        editor.putBoolean(premiumElem, premium);
        editor.putString(countryElem, country);
        editor.putInt(day_of_monthElem, day_of_month);
        editor.putInt(monthElem, month);
        editor.putInt(yearElem, year);
        editor.putString(mobilElem, mobilnummer);
        editor.putString(emailElem, email);
        editor.apply();
    }

    private void HentBruker() {
        lokalBruker.brukernavn = sharedPreferences.getString(brukernavnElem, "");
        lokalBruker.passord = sharedPreferences.getString(passordElem, "");
        lokalBruker.harakseptert = sharedPreferences.getBoolean(harakseptertElem, false);
        lokalBruker.fornavn = sharedPreferences.getString(fornavnElem, "");
        lokalBruker.etternavn = sharedPreferences.getString(etternavnElem, "");
        lokalBruker.premium = sharedPreferences.getBoolean(premiumElem, false);
        lokalBruker.country = sharedPreferences.getString(countryElem, "");
        lokalBruker.day_of_month = sharedPreferences.getInt(day_of_monthElem, 0);
        lokalBruker.month = sharedPreferences.getInt(monthElem, 0);
        lokalBruker.year = sharedPreferences.getInt(yearElem, 0);
        lokalBruker.mobilnummer = sharedPreferences.getString(mobilElem, "");
        lokalBruker.email = sharedPreferences.getString(emailElem, "");
        lokalBruker.listOfEvents = new ArrayList<>();
        lokalBruker.friendList = new ArrayList<>();
        lokalBruker.requests = new ArrayList<>();
    }

    public void ParseEvents(JSONArray events) {
        if(events.toString().equals("[]")) {
            listOfEvents = new ArrayList<>();
            return;
        }

        listOfEvents = new ArrayList<>();

        try {
            for(int i = 0; i < events.length(); i++) {
                String str = events.getString(i);

                Event eventToAdd = new Gson().fromJson(events.getString(i), Event.class);

                if(eventToAdd.getEventId() == 0)
                    continue;
                if(eventToAdd.getHostStr().isEmpty())
                    continue;
                if(!eventToAdd.getNameofevent().isEmpty())
                    listOfEvents.add(eventToAdd);

            }
        } catch(JSONException | IllegalStateException e){
            e.printStackTrace();
        }
    }

    public Bruker retBrukerFromJSON(JSONObject json) {
        Bruker newBruker = new Bruker();
        try {
            newBruker.brukernavn = json.getString(brukernavnElem);
            newBruker.passord = json.getString(passordElem);
            newBruker.harakseptert = json.getBoolean(harakseptertElem);
            newBruker.fornavn = json.getString(fornavnElem);
            newBruker.etternavn = json.getString(etternavnElem);
            newBruker.premium = json.getBoolean(premiumElem);
            newBruker.mobilnummer = json.getString(mobilElem);
            newBruker.email = json.getString(emailElem);
            newBruker.day_of_month = json.getInt(day_of_monthElem);
            newBruker.month = json.getInt(monthElem);
            newBruker.year = json.getInt(yearElem);
            newBruker.friendList = new Gson().fromJson(json.getString(friendlistElem), listFriendsType);
            newBruker.requests = new Gson().fromJson(json.getString(requestlistElem), listFriendsType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newBruker;
    }

    public void JSONToBruker(JSONObject json) {
        try {
            brukernavn = json.getString(brukernavnElem);
            passord = json.getString(passordElem);
            harakseptert = json.getBoolean(harakseptertElem);
            fornavn = json.getString(fornavnElem);
            etternavn = json.getString(etternavnElem);
            premium = json.getBoolean(premiumElem);
            mobilnummer = json.getString(mobilElem);
            email = json.getString(emailElem);
            day_of_month = json.getInt(day_of_monthElem);
            month = json.getInt(monthElem);
            year = json.getInt(yearElem);
            friendList = new Gson().fromJson(json.getString(friendlistElem), listFriendsType);
            requests = new Gson().fromJson(json.getString(requestlistElem), listFriendsType);
            LagreBruker();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String BrukerToJSON() {
        try {
            JSONObject fullJson = new JSONObject();
            fullJson.put(brukernavnElem, brukernavn);
            fullJson.put(passordElem, passord);
            fullJson.put(harakseptertElem, harakseptert);
            fullJson.put(fornavnElem, fornavn);
            fullJson.put(etternavnElem, etternavn);
            fullJson.put(premiumElem, premium);
            fullJson.put(mobilElem, mobilnummer);
            fullJson.put(emailElem, email);
            fullJson.put(day_of_monthElem, day_of_month);
            fullJson.put(monthElem, month);
            fullJson.put(premiumElem, premium);
            fullJson.put(loggedonElem, loggetpa);
            fullJson.put(yearElem, year);
            fullJson.put(countryElem, country);
            fullJson.put(townElem, town);
            fullJson.put(friendlistElem, new Gson().toJson(friendList));
            fullJson.put(requestlistElem, new Gson().toJson(requests));
            fullJson.put(socketElem, BuildConfig.JSONParser_Socket);
            return fullJson.toString();
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Event getEventFromID(long eventid) {
        for(Event eventToParse : listOfEvents) {
            if(eventToParse.getEventId() == eventid) {
                return eventToParse;
            }
        }

        return null;
    }

    public boolean isParticipantInFriendlist(Participant participant) {
        for(Friend friend : friendList) {
            if(friend.getBrukernavn().equals(participant.getBrukernavn())) {
                return true;
            }
        }

        return false;
    }

    public void addToMyEvents(Event event) {
        listOfMyEvents.add(event);
        LagreBruker();
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isLoggetpa() {
        return loggetpa;
    }

    public void setLoggetpa(boolean loggetpa) {
        this.loggetpa = loggetpa;
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

    public boolean isHarakseptert() {
        return harakseptert;
    }

    public void setHarakseptert(boolean harakseptert) {
        this.harakseptert = harakseptert;
    }

    public int getDay_of_month() {
        return day_of_month;
    }

    public void setDOB(int day, int mnth, int yr) {
        day_of_month = day;
        month = mnth;
        year = yr;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public String getEmail() {
        return email;
    }

    public void setMobilnummer(String value) {
        this.mobilnummer = value;
    }

    public String getMobilnummer() {
        return mobilnummer;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public List<Event> getListOfEvents() {
        return listOfEvents;
    }

    public void setListOfEvents(List<Event> listOfEvents) {
        this.listOfEvents = listOfEvents;
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

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }

    public List<Friend> getRequests() {
        return requests;
    }

    public void setRequests(List<Friend> requests) {
        this.requests = requests;
    }

    public String getOneliner() {
        return oneliner;
    }

    public void setOneliner(String oneliner) {
        this.oneliner = oneliner;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public List<Event> getListOfMyEvents() {
        return listOfMyEvents;
    }

    public void setListOfMyEvents(List<Event> listOfMyEvents) {
        this.listOfMyEvents = listOfMyEvents;
    }
}
