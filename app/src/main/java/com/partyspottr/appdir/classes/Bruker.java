package com.partyspottr.appdir.classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.adapters.EventAdapter;
import com.partyspottr.appdir.ui.other_ui.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ranarrr on 26-Jan-18.
 *
 * Thomas Høyland Rekvik f. 22. Juli 1998
 * Han har 2 tanter
 * Begge er i live.
 *
 * thpokasd telbiok
 *
 * @author Ranarrr
 */

public class Bruker {
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

    private static Bruker lokalBruker;
    private Chauffeur chauffeur;

    private static Map<String, Bitmap> eventImages;

    public DatabaseReference eventsref;
    public ChildEventListener eventschildlistener;

    public DatabaseReference brukerinforef;
    public ChildEventListener brukerinfolistener;

    public DatabaseReference brukerchauffeurref;
    public ChildEventListener brukerchauffeurlistener;

    public DatabaseReference chauffeursref;
    public ChildEventListener chauffeurslistener;

    private long eventIdCounter;

    private SharedPreferences sharedPreferences;

    public static Bruker get() {
        if(lokalBruker == null) {
            lokalBruker = new Bruker();
        }

        return lokalBruker;
    }

    public static void AddEventImage(String key, Bitmap value) {
        eventImages.put(key, value);
    }

    public static Map<String, Bitmap> getEventImages() {
        return eventImages;
    }

    public static void setEventImages(Map<String, Bitmap> eventImages) {
        Bruker.eventImages = eventImages;
    }

    public void Init(Context c) {
        sharedPreferences = c.getSharedPreferences("Bruker", Context.MODE_PRIVATE);
        chauffeur = new Chauffeur(sharedPreferences);
        eventImages = new HashMap<>();
        HentBruker();
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

    public void DeleteBruker() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
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
        eventIdCounter = 0;

        if(sharedPreferences.getString(currentCarElem, "").equals(""))
            current_car = new Car();
        else
            current_car = new Gson().fromJson(sharedPreferences.getString(currentCarElem, ""), Utilities.carType);

        listOfEvents = new ArrayList<>();
        listOfMyEvents = new ArrayList<>();

        if(sharedPreferences.getString(chatElem, "").equals(""))
            chatMessageList = new ArrayList<>();
        else
            chatMessageList = new Gson().fromJson(sharedPreferences.getString(chatElem, ""), Utilities.listPreviewMsgsType);

        if(sharedPreferences.getString(friendlistElem, "").equals(""))
            friendList = new ArrayList<>();
        else
            friendList = new Gson().fromJson(sharedPreferences.getString(friendlistElem, ""), Utilities.listFriendsType);

        if(sharedPreferences.getString(requestlistElem, "").equals(""))
            requests = new ArrayList<>();
        else
            requests = new Gson().fromJson(sharedPreferences.getString(requestlistElem, ""), Utilities.listRequestsType);
    }

    public void StopParsingChauffeurs() {
        if(chauffeursref != null)
            chauffeursref.removeEventListener(chauffeurslistener);
    }

    public void GetAndParseChauffeurs() {
        chauffeursref = FirebaseDatabase.getInstance().getReference("chauffeurs");

        chauffeurslistener = chauffeursref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(DataSnapshot chauffeurs : dataSnapshot.getChildren()) {
                    Chauffeur chauffeur = Utilities.getChauffeurFromDataSnapshot(chauffeurs);

                    if(chauffeur != null && listchauffeurs != null)
                        listchauffeurs.add(chauffeur);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(DataSnapshot chauffeurs : dataSnapshot.getChildren()) {
                    Chauffeur chauffeur = Utilities.getChauffeurFromDataSnapshot(chauffeurs);

                    if(chauffeur == null)
                        continue;

                    Chauffeur.setChauffeur(chauffeur, listchauffeurs);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot chauffeurs : dataSnapshot.getChildren()) {
                    Chauffeur chauffeur = Utilities.getChauffeurFromDataSnapshot(chauffeurs);

                    if(chauffeur == null)
                        continue;

                    Chauffeur.removeChauffeur(chauffeur, listchauffeurs);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void StopParsingBrukerChauffeur() {
        if(brukerchauffeurref != null)
            brukerchauffeurref.removeEventListener(brukerchauffeurlistener);
    }

    public void GetAndParseBrukerChauffeur() {
        brukerchauffeurref = FirebaseDatabase.getInstance().getReference("chauffeurs").child(lokalBruker.getBrukernavn());

        brukerchauffeurlistener = brukerchauffeurref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(!dataSnapshot.exists()) {
                    setHascar(false);
                    LagreBruker();
                    return;
                }

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getKey() != null) {
                        switch(snapshot.getKey()) {
                            case "rating":
                                if(snapshot.getValue() != null)
                                    getChauffeur().setM_rating(snapshot.getValue(Double.class));
                                break;

                            case "brukernavn":
                                getChauffeur().setM_brukernavn(snapshot.getValue(String.class));
                                break;

                            case "fornavn":
                                getChauffeur().setFornavn(snapshot.getValue(String.class));
                                break;

                            case "etternavn":
                                getChauffeur().setEtternavn(snapshot.getValue(String.class));
                                break;

                            case "age":
                                if(snapshot.getValue(Integer.class) != null)
                                    getChauffeur().setM_age(snapshot.getValue(Integer.class));
                                break;

                            case "capacity":
                                if(snapshot.getValue(Integer.class) != null)
                                    getChauffeur().setM_capacity(snapshot.getValue(Integer.class));
                                break;

                            case "timefrom":
                                if(snapshot.getValue(Long.class) != null)
                                    getChauffeur().setChauffeur_time_from(snapshot.getValue(Long.class));
                                break;

                            case "timeto":
                                if(snapshot.getValue(Long.class) != null)
                                    getChauffeur().setChauffeur_time_to(snapshot.getValue(Long.class));
                                break;

                            case "listcars":
                                List<Car> list = new Gson().fromJson(snapshot.getValue(String.class), Utilities.listCarsType);
                                getChauffeur().setListOfCars(list);
                                break;

                            case "longitude":
                                if(snapshot.getValue(Double.class) != null)
                                    getChauffeur().setLongitude(snapshot.getValue(Double.class));
                                break;

                            case "latitude":
                                if(snapshot.getValue(Double.class) != null)
                                    getChauffeur().setLatitude(snapshot.getValue(Double.class));
                                break;
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(!dataSnapshot.exists()) {
                    setHascar(false);
                    LagreBruker();
                    return;
                }

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getKey() != null) {
                        switch(snapshot.getKey()) {
                            case "rating":
                                if(snapshot.getValue() != null)
                                    Bruker.get().getChauffeur().setM_rating(snapshot.getValue(Double.class));
                                break;

                            case "brukernavn":
                                Bruker.get().getChauffeur().setM_brukernavn(snapshot.getValue(String.class));
                                break;

                            case "fornavn":
                                Bruker.get().getChauffeur().setFornavn(snapshot.getValue(String.class));
                                break;

                            case "etternavn":
                                Bruker.get().getChauffeur().setEtternavn(snapshot.getValue(String.class));
                                break;

                            case "age":
                                if(snapshot.getValue(Integer.class) != null)
                                    Bruker.get().getChauffeur().setM_age(snapshot.getValue(Integer.class));
                                break;

                            case "capacity":
                                if(snapshot.getValue(Integer.class) != null)
                                    Bruker.get().getChauffeur().setM_capacity(snapshot.getValue(Integer.class));
                                break;

                            case "timefrom":
                                if(snapshot.getValue(Long.class) != null)
                                    Bruker.get().getChauffeur().setChauffeur_time_from(snapshot.getValue(Long.class));
                                break;

                            case "timeto":
                                if(snapshot.getValue(Long.class) != null)
                                    Bruker.get().getChauffeur().setChauffeur_time_to(snapshot.getValue(Long.class));
                                break;

                            case "listcars":
                                List<Car> list = new Gson().fromJson(snapshot.getValue(String.class), Utilities.listCarsType);
                                Bruker.get().getChauffeur().setListOfCars(list);
                                break;

                            case "longitude":
                                if(snapshot.getValue(Double.class) != null)
                                    Bruker.get().getChauffeur().setLongitude(snapshot.getValue(Double.class));
                                break;

                            case "latitude":
                                if(snapshot.getValue(Double.class) != null)
                                    Bruker.get().getChauffeur().setLatitude(snapshot.getValue(Double.class));
                                break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void StopParsingBrukerInfo() {
        if(brukerinforef != null)
            brukerinforef.removeEventListener(brukerinfolistener);
    }

    public void GetAndParseBrukerInfo() {
        brukerinforef = FirebaseDatabase.getInstance().getReference("users").child(getBrukernavn());

        brukerinfolistener = brukerinforef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Utilities.parseBrukerInfo(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Utilities.parseBrukerInfo(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void StopParsingEvents() {
        if(eventsref != null)
            eventsref.removeEventListener(eventschildlistener);
    }

    public void GetAndParseEvents(final Activity activity) {
        listOfEvents = new ArrayList<>();
        listOfMyEvents = new ArrayList<>();

        eventsref = FirebaseDatabase.getInstance().getReference("events");

        eventschildlistener = eventsref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey() != null)
                    if(dataSnapshot.getKey().equals("eventidCounter") && dataSnapshot.getValue() != null)
                        Bruker.get().setEventIdCounter(dataSnapshot.getValue(Long.class));

                Event event = Utilities.getEventFromDataSnapshot(dataSnapshot);

                if(event.getEventId() == 0)
                    return;

                if(event.getHostStr().isEmpty())
                    return;

                if(event.getHostStr().toLowerCase().equals(Bruker.get().getBrukernavn()))
                    listOfMyEvents.add(event);

                listOfEvents.add(event);

                ListView lv_alle_eventer = activity.findViewById(R.id.lvalle_eventer);
                ListView lv_mine_eventer = activity.findViewById(R.id.lvmine_eventer);

                if(lv_mine_eventer != null)
                    lv_mine_eventer.setAdapter(new EventAdapter(activity, listOfMyEvents));

                if(lv_alle_eventer != null)
                    lv_alle_eventer.setAdapter(new EventAdapter(activity, listOfEvents));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey() != null)
                    if(dataSnapshot.getKey().equals("eventidCounter") && dataSnapshot.getValue() != null)
                        Bruker.get().setEventIdCounter(dataSnapshot.getValue(Long.class));

                Event event = Utilities.getEventFromDataSnapshot(dataSnapshot);

                if(event.getEventId() == 0)
                    return;

                if(event.getHostStr().isEmpty())
                    return;

                setEventByID(event.getEventId(), event);

                ListView lv_alle_eventer = activity.findViewById(R.id.lvalle_eventer);
                ListView lv_mine_eventer = activity.findViewById(R.id.lvmine_eventer);

                if(lv_mine_eventer != null)
                    lv_mine_eventer.setAdapter(new EventAdapter(activity, listOfMyEvents));

                if(lv_alle_eventer != null)
                    lv_alle_eventer.setAdapter(new EventAdapter(activity, listOfEvents));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey() != null)
                    removeEventByID(Long.valueOf(dataSnapshot.getKey()));

                ListView lv_alle_eventer = activity.findViewById(R.id.lvalle_eventer);
                ListView lv_mine_eventer = activity.findViewById(R.id.lvmine_eventer);

                if(lv_mine_eventer != null)
                    lv_mine_eventer.setAdapter(new EventAdapter(activity, listOfMyEvents));

                if(lv_alle_eventer != null)
                    lv_alle_eventer.setAdapter(new EventAdapter(activity, listOfEvents));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void JSONToBruker(JSONObject json) {
        try {
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

    private boolean doesChatExist(String brukernavn) {
        if(chatMessageList == null)
            return false;

        for(ChatPreview preview : chatMessageList) {
            for(Chatter chatter : preview.getChatters()) {
                if(chatter.getBrukernavn().equalsIgnoreCase(brukernavn))
                    return true;
            }
        }

        return false;
    }

    public void startChat(Activity activity, ChatPreview chatPreview, String otherUser, String message) {
        if(doesChatExist(otherUser)) {
            Toast.makeText(activity, "You have already started a conversation with this user!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(isConnected()) {
            if(chatMessageList == null)
                chatMessageList = new ArrayList<>();

            chatMessageList.add(chatPreview);

            List<ChatMessage> list = new ArrayList<>();
            list.add(new ChatMessage(message, getBrukernavn()));

            FirebaseDatabase.getInstance().getReference("messagepreviews").child(getBrukernavn() + "_" + otherUser.toLowerCase()).setValue(chatPreview);
            FirebaseDatabase.getInstance().getReference("messages").child(getBrukernavn() + "_" + otherUser.toLowerCase()).setValue(list);
            LagreBruker();

            Toast.makeText(activity, "Started a chat with " + chatPreview.getChatters().get(1).getBrukernavn(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getBrukernavn() {
        if(brukernavn == null)
            brukernavn = sharedPreferences.getString(brukernavnElem, "");

        return brukernavn.toLowerCase();
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

    public void setEmail(String value) {
        this.email = value;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getDay_of_month() {
        return day_of_month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDay_of_month(int day_of_month) {
        this.day_of_month = day_of_month;
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

    public long getEventIdCounter() {
        return eventIdCounter;
    }

    public void setEventIdCounter(long eventIdCounter) {
        this.eventIdCounter = eventIdCounter;
    }
}