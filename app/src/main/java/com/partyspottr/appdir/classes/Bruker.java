package com.partyspottr.appdir.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ranarrr on 26-Jan-18.
 *
 * @author Ranarrr
 */

public class Bruker {
    private long userid;
    private String brukernavn;
    private String passord;
    private String fornavn;
    private String etternavn;
    private String email;
    private boolean premium;
    private boolean loggetpa;
    private boolean isConnected;
    private int day_of_month;
    private int month;
    private int year;
    private String country;
    private String town;
    private List<Friend> friendList;
    private List<Friend> requests;
    private List<Event> listOfMyEvents;
    private List<Event> listOfEvents;
    private String oneliner;
    private Calendar created;
    private List<ChatPreview> chatMessageList;
    private boolean hascar;
    private Car current_car;
    private List<Chauffeur> listchauffeurs;

    private static final String idElem = "id";
    private static final String brukernavnElem = "brukernavnElem";
    private static final String fornavnElem = "fornavnElem";
    private static final String emailElem = "emailElem";
    private static final String passordElem = "passordElem";
    private static final String etternavnElem = "etternavnElem";
    private static final String premiumElem = "premiumElem";
    private static final String loggedonElem = "loggedonElem";
    private static final String countryElem = "countryElem";
    private static final String day_of_monthElem = "day_of_monthElem";
    private static final String friendlistElem = "friendlist";
    private static final String requestlistElem = "requests";
    private static final String monthElem = "monthElem";
    private static final String townElem = "townElem";
    private static final String yearElem = "yearElem";
    private static final String socketElem = "socketElem";
    private static final String chatElem = "chatElem";
    private static final String onelinerElem = "oneliner";
    private static final String currentCarElem = "currentCarElem";
    private static final String hascarElem = "hascarElem";

    private static final Type listFriendsType = new TypeToken<List<Friend>>(){}.getType();
    private static final Type listParticipantsType = new TypeToken<List<Participant>>(){}.getType();
    private static final Type listRequestsType = new TypeToken<List<Requester>>(){}.getType();
    private static final Type listPreviewMsgsType = new TypeToken<List<ChatPreview>>(){}.getType();
    private static final Type carType = new TypeToken<Car>(){}.getType();

    private static Bruker lokalBruker;
    private Chauffeur chauffeur;

    public DatabaseReference ref;
    public ChildEventListener eventListener;

    private SharedPreferences sharedPreferences;

    public static Bruker get() {
        if(lokalBruker == null) {
            lokalBruker = new Bruker();
        }

        return lokalBruker;
    }

    public void Init(Context c) {
        sharedPreferences = c.getSharedPreferences("Bruker", Context.MODE_PRIVATE);
        chauffeur = new Chauffeur(sharedPreferences);
        HentBruker();
    }

    public void populateChauffeur(Activity activity) {
        Chauffeur.getChauffeur(activity, brukernavn, chauffeur);
    }

    public void LagreBruker() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(brukernavnElem, brukernavn);
        editor.putString(passordElem, passord);

        editor.putString(fornavnElem, fornavn);
        editor.putString(etternavnElem, etternavn);
        editor.putBoolean(premiumElem, premium);
        editor.putString(countryElem, country);
        editor.putInt(day_of_monthElem, day_of_month);
        editor.putInt(monthElem, month);
        editor.putInt(yearElem, year);
        editor.putString(emailElem, email);
        editor.putString(onelinerElem, oneliner);
        editor.putBoolean(hascarElem, hascar);
        editor.putString(currentCarElem, new Gson().toJson(current_car));
        editor.putString(chatElem, new Gson().toJson(chatMessageList));
        editor.putString(friendlistElem, new Gson().toJson(friendList));
        editor.putString(requestlistElem, new Gson().toJson(requests));
        editor.apply();
    }

    private void HentBruker() {
        brukernavn = sharedPreferences.getString(brukernavnElem, "");
        passord = sharedPreferences.getString(passordElem, "");
        fornavn = sharedPreferences.getString(fornavnElem, "");
        etternavn = sharedPreferences.getString(etternavnElem, "");
        premium = sharedPreferences.getBoolean(premiumElem, false);
        country = sharedPreferences.getString(countryElem, "");
        day_of_month = sharedPreferences.getInt(day_of_monthElem, 0);
        month = sharedPreferences.getInt(monthElem, 0);
        year = sharedPreferences.getInt(yearElem, 0);
        email = sharedPreferences.getString(emailElem, "");
        oneliner = sharedPreferences.getString(onelinerElem, "");
        hascar = sharedPreferences.getBoolean(hascarElem, false);

        if(sharedPreferences.getString(currentCarElem, "").equals(""))
            current_car = new Car();
        else
            current_car = new Gson().fromJson(sharedPreferences.getString(currentCarElem, ""), carType);

        listOfEvents = new ArrayList<>();
        listOfMyEvents = new ArrayList<>();

        if(sharedPreferences.getString(chatElem, "").equals(""))
            chatMessageList = new ArrayList<>();
        else
            chatMessageList = new Gson().fromJson(sharedPreferences.getString(chatElem, ""), listPreviewMsgsType);

        if(sharedPreferences.getString(friendlistElem, "").equals(""))
            friendList = new ArrayList<>();
        else
            friendList = new Gson().fromJson(sharedPreferences.getString(friendlistElem, ""), listFriendsType);

        if(sharedPreferences.getString(requestlistElem, "").equals(""))
            requests = new ArrayList<>();
        else
            requests = new Gson().fromJson(sharedPreferences.getString(requestlistElem, ""), listRequestsType);
    }

    public void ParseChauffeurs(JSONArray chauffeurs) {
        listchauffeurs = new ArrayList<>();

        if(chauffeurs.length() == 0)
            return;

        try {
            for(int i = 0; i < chauffeurs.length(); i++) {
                JSONObject chauffeur = new JSONObject(chauffeurs.getString(i));

                List<Car> cars = new Gson().fromJson(chauffeur.getString("carlistElem"), Chauffeur.listOfCarsType);

                Chauffeur ret = new Chauffeur(chauffeur.getDouble("rating"), chauffeur.getString("brukernavnElem"), chauffeur.getString("fornavn"), chauffeur.getString("etternavn"),
                        chauffeur.getInt("age"), chauffeur.getInt("capacity"), chauffeur.getLong("timeDrivingFrom"), chauffeur.getLong("timeDrivingTo"), cars, chauffeur.getDouble("longitude"),
                        chauffeur.getDouble("latitude"));

                if(ret.getChauffeur_time_to() < System.currentTimeMillis())
                    continue;

                if(!ret.getM_brukernavn().isEmpty())
                    listchauffeurs.add(ret);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetAndParseEvents() {
        if(!Bruker.get().isConnected())
            return;

        listOfEvents = new ArrayList<>();
        listOfMyEvents = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("events");

        eventListener = ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event event = new Event();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getKey() != null) {
                        switch(snapshot.getKey()) {
                            case "nameofevent":
                                event.setNameofevent(snapshot.getValue(String.class));
                                break;

                            case "address":
                                event.setAddress(snapshot.getValue(String.class));
                                break;

                            case "agerestriction":
                                if(snapshot.getValue(Integer.class) != null) {
                                    event.setAgerestriction(snapshot.getValue(Integer.class));
                                }
                                break;

                            case "country":
                                event.setCountry(snapshot.getValue(String.class));
                                break;

                            case "datefrom":
                                if(snapshot.getValue(Long.class) != null) {
                                    event.setDatefrom(snapshot.getValue(Long.class));
                                }
                                break;

                            case "dateto":
                                if(snapshot.getValue(Long.class) != null) {
                                    event.setDateto(snapshot.getValue(Long.class));
                                }
                                break;

                            case "desc":
                                event.setDescription(snapshot.getValue(String.class));
                                break;

                            case "hasimage":
                                if(snapshot.getValue(Boolean.class) != null) {
                                    event.setHasimage(snapshot.getValue(Boolean.class));
                                }
                                break;

                            case "hostStr":
                                event.setHostStr(snapshot.getValue(String.class));
                                break;

                            case "isprivate":
                                if(snapshot.getValue(Boolean.class) != null) {
                                    event.setPrivateEvent(snapshot.getValue(Boolean.class));
                                }
                                break;

                            case "latitude":
                                event.setLatitude(snapshot.getValue(Double.class));
                                break;

                            case "longitude":
                                event.setLongitude(snapshot.getValue(Double.class));
                                break;

                            case "maxparticipants":
                                if(snapshot.getValue(Integer.class) != null) {
                                    event.setMaxparticipants(snapshot.getValue(Integer.class));
                                }
                                break;

                            case "participants":
                                List<Participant> participants = new Gson().fromJson(snapshot.getValue(String.class), listParticipantsType);
                                event.setParticipants(participants);
                                break;

                            case "postalcode":
                                event.setPostalcode(snapshot.getValue(String.class));
                                break;

                            case "requests":
                                List<Requester> requests = new Gson().fromJson(snapshot.getValue(String.class), listRequestsType);
                                event.setRequests(requests);
                                break;

                            case "showaddress":
                                if(snapshot.getValue(Boolean.class) != null) {
                                    event.setShowaddress(snapshot.getValue(Boolean.class));
                                }
                                break;

                            case "showguestlist":
                                if(snapshot.getValue(Boolean.class) != null) {
                                    event.setShowguestlist(snapshot.getValue(Boolean.class));
                                }
                                break;

                            case "town":
                                event.setTown(snapshot.getValue(String.class));
                                break;

                            case "eventId":
                                if(snapshot.getValue(Long.class) != null) {
                                    event.setEventId(snapshot.getValue(Long.class));
                                }
                                break;
                        }
                    }
                }

                if(event.getEventId() == 0)
                    return;

                if(event.getHostStr().isEmpty())
                    return;

                if(event.getHostStr().equals(Bruker.get().getBrukernavn()))
                    listOfMyEvents.add(event);

                listOfEvents.add(event);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event event = dataSnapshot.getValue(Event.class);

                if(event == null)
                    return;

                if(event.getEventId() == 0)
                    return;

                if(event.getHostStr().isEmpty())
                    return;

                setEventByID(event.getEventId(), event);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);

                if(event == null)
                    return;

                if(event.getEventId() == 0)
                    return;

                if(event.getHostStr().isEmpty())
                    return;

                removeEventByID(event.getEventId());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static Bruker retBrukerFromJSON(String str) {
        try {
            JSONObject json = new JSONObject(str);
            Bruker newBruker = new Bruker();

            newBruker.brukernavn = json.getString(brukernavnElem);
            newBruker.fornavn = json.getString(fornavnElem);
            newBruker.etternavn = json.getString(etternavnElem);
            newBruker.premium = Integer.valueOf(json.getString(premiumElem)) > 0;
            newBruker.email = json.getString(emailElem);
            newBruker.town = json.getString(townElem);
            newBruker.userid = Integer.valueOf(json.getString("id"));
            newBruker.country = json.getString(countryElem);
            newBruker.day_of_month = Integer.valueOf(json.getString(day_of_monthElem));
            newBruker.month = Integer.valueOf(json.getString(monthElem));
            newBruker.year = Integer.valueOf(json.getString(yearElem));
            newBruker.oneliner = json.getString(onelinerElem);

            return newBruker;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void JSONToBruker(JSONObject json) {
        try {
            userid = json.getLong(idElem);
            brukernavn = json.getString(brukernavnElem);
            email = json.getString(emailElem);

            LagreBruker();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getAllEventIDUserGoingTo(String user) {
        List<Integer> ret = new ArrayList<>();

        for(int i = 0; i < listOfEvents.size(); i++) {
            if(listOfEvents.get(i).getHostStr().equals(user)) {
                ret.add(i);
                continue;
            }

            for(Participant child : listOfEvents.get(i).getParticipants()) {
                if(child.getBrukernavn().equals(user))
                    ret.add(i);
            }
        }

        return ret;
    }

    public Event getEventFromID(long eventid) {
        for(Event eventToParse : listOfEvents) {
            if(eventToParse.getEventId() == eventid) {
                return eventToParse;
            }
        }

        return null;
    }

    public void setEventByID(long eventid, Event eventToSet) {
        for(int i = 0; i < listOfEvents.size(); i++) {
            if(listOfEvents.get(i).getEventId() == eventid) {
                listOfEvents.set(i, eventToSet);
                break;
            }
        }

        for(int i = 0; i < listOfMyEvents.size(); i++) {
            if(listOfMyEvents.get(i).getEventId() == eventid) {
                listOfMyEvents.set(i, eventToSet);
                return;
            }
        }
    }

    public void removeEventByID(long eventid) {
        for(int i = 0; i < listOfEvents.size(); i++) {
            if(listOfEvents.get(i).getEventId() == eventid) {
                listOfEvents.remove(i);
                break;
            }
        }

        for(int i = 0; i < listOfMyEvents.size(); i++) {
            if(listOfMyEvents.get(i).getEventId() == eventid) {
                listOfMyEvents.remove(i);
                return;
            }
        }
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

    public boolean startChat(ChatPreview chatPreview, String otherUser, String message) {
        if(isConnected()) {
            if(chatMessageList == null)
                chatMessageList = new ArrayList<>();

            chatMessageList.add(chatPreview);

            List<ChatMessage> list = new ArrayList<>();
            list.add(new ChatMessage(message, getBrukernavn()));

            FirebaseDatabase.getInstance().getReference("messagepreviews").child(getBrukernavn() + "_" + otherUser).setValue(chatPreview);
            FirebaseDatabase.getInstance().getReference("messages").child(getBrukernavn() + "_" + otherUser).setValue(list);
            LagreBruker();
            return true;
        }

        return false;
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

    public List<ChatPreview> getChatMessageList() {
        return chatMessageList;
    }

    public void setChatMessageList(List<ChatPreview> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public boolean isHascar() {
        return hascar;
    }

    public void setHascar(boolean hascar) {
        this.hascar = hascar;
    }

    public Car getCurrent_car() {
        return current_car;
    }

    public void setCurrent_car(Car current_car) {
        this.current_car = current_car;
    }

    public Chauffeur getChauffeur() {
        return chauffeur;
    }

    public void setChauffeur(Chauffeur chauffeur) {
        this.chauffeur = chauffeur;
    }

    public List<Chauffeur> getListchauffeurs() {
        return listchauffeurs;
    }

    public void setListchauffeurs(List<Chauffeur> listchauffeurs) {
        this.listchauffeurs = listchauffeurs;
    }
}