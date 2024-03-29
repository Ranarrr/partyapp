package com.partyspottr.appdir.classes;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.adapters.ChatPreviewAdapter;
import com.partyspottr.appdir.classes.adapters.EventAdapter;
import com.partyspottr.appdir.classes.adapters.FriendListAdapter;
import com.partyspottr.appdir.classes.adapters.RequestAdapter;
import com.partyspottr.appdir.classes.application.ApplicationLifecycleMgr;
import com.partyspottr.appdir.classes.networking.AddEvent;
import com.partyspottr.appdir.classes.networking.UpdateEvent;
import com.partyspottr.appdir.classes.networking.UpdateUser;
import com.partyspottr.appdir.classes.services.NotificationService;
import com.partyspottr.appdir.enums.Categories;
import com.partyspottr.appdir.enums.EventStilling;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.ProfilActivity;
import com.partyspottr.appdir.ui.SplashActivity;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.alle_eventer_fragment;
import com.partyspottr.appdir.ui.other_ui.EventDetails;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.partyspottr.appdir.ui.MainActivity.typeface;

/**
 * Created by Ranarrr on 02-Feb-18.
 *
 * @author Ranarrr
 */

public class Utilities {
    public static final int PLACE_PICKER_CODE = 1002;
    public static final int LOCATION_REQUEST_CODE = 1000;
    public static final int READ_EXTERNAL_STORAGE_CODE = 1001;
    public static final int READ_EXTERNAL_STORAGE_PROFILE_CODE = 1003;

    public static final Type listFriendsType = new TypeToken<List<Friend>>(){}.getType();
    public static final Type listParticipantsType = new TypeToken<List<Participant>>(){}.getType();
    public static final Type listRequestsType = new TypeToken<List<Requester>>(){}.getType();
    public static final Type listPreviewMsgsType = new TypeToken<List<ChatPreview>>(){}.getType();
    public static final Type listCarsType = new TypeToken<List<Car>>(){}.getType();
    public static final Type carType = new TypeToken<Car>(){}.getType();

    public static boolean hasNetwork(Context c) {
        if(Settings.Global.getInt(c.getContentResolver(), "airplane_mode_on", 0) != 0)
            return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
            return false;

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static GregorianCalendar getDateFromString(String str, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());

        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        try {
            gregorianCalendar.setTime(simpleDateFormat.parse(str.toUpperCase()));
            return gregorianCalendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getDateStringFromMillis(long millis, boolean isdato) {
        GregorianCalendar time = new GregorianCalendar();
        time.setTimeInMillis(millis);

        return isdato ? String.format(Locale.ENGLISH, "%02d %s %d", time.get(Calendar.DAY_OF_MONTH), time.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()), time.get(Calendar.YEAR)) :
                String.format(Locale.ENGLISH, "%02d:%02d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
    }

    public static void startChatListener(final Activity activity) {
        ProfilActivity.ref = FirebaseDatabase.getInstance().getReference().child("messagepreviews");

        ProfilActivity.valueEventListener = ProfilActivity.ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChatPreview> list = new ArrayList<>();

                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.getKey() == null)
                        continue;

                    for(String splitted : child.getKey().split("_")) {
                        if(splitted.equalsIgnoreCase(Bruker.get().getBrukernavn())) {
                            list.add(child.getValue(ChatPreview.class));
                            break;
                        }
                    }
                }

                if(activity != null) {
                    ListView lv_chat = activity.findViewById(R.id.lv_chat);

                    if(lv_chat != null)
                        lv_chat.setAdapter(new ChatPreviewAdapter(activity, list));
                }

                Bruker.get().setChatMessageList(list);
                Bruker.get().LagreBruker();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(databaseError.getCode() == DatabaseError.NETWORK_ERROR  && activity != null)
                    Toast.makeText(activity, activity.getResources().getString(R.string.could_not_find_chats), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Location getLatLng(Activity activity) {
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, activity.getResources().getString(R.string.please_allow), Toast.LENGTH_LONG).show();
            System.exit(0);
        }

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            Criteria criteria = new Criteria();
            String bestprovider = locationManager.getBestProvider(criteria, false);

            Location location = locationManager.getLastKnownLocation(bestprovider);

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if(location == null) {
                Toast.makeText(activity, activity.getResources().getString(R.string.try_again_later), Toast.LENGTH_LONG).show();
                return null;
            } else
                return location;
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.try_again_later), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    /**
     * This method is used for gettting the latest position of the user currently logged in.
     *
     * @param activity Activity to be used for checking permissions and getting location service see Context.LOCATION_SERVICE.
     */
    public static void getPosition(Activity activity) {
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, activity.getResources().getString(R.string.please_allow), Toast.LENGTH_LONG).show();
            System.exit(0);
        }

        Geocoder geocoder = new Geocoder(activity, Locale.ENGLISH);
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            Criteria criteria = new Criteria();
            String bestprovider = locationManager.getBestProvider(criteria, false);

            Location location = locationManager.getLastKnownLocation(bestprovider);

            if(location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if(location != null) {
                try {
                    for(Address address : geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1)) {
                        if(address != null) {
                            Bruker.get().setCountry(address.getCountryName());
                            Bruker.get().setTown(address.getLocality());
                            Bruker.get().LagreBruker();

                            if(Bruker.get().isLoggetpa()) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(Bruker.get().getBrukernavn());

                                ref.child("country").setValue(address.getCountryName());
                                ref.child("town").setValue(address.getLocality());
                            }

                            UpdateUser updateUser = new UpdateUser();
                            updateUser.execute();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Narch", "Vors", "Party", "Concert", "Nightclub", "Birthday", "Festival"
    public static Categories getCategoryFromString(String category) {
        switch (category.toUpperCase()) {
            case "NARCH":
                return Categories.NARCHSPIEL;

            case "VORS":
                return Categories.VORSPIEL;

            case "PARTY":
                return Categories.PARTY;

            case "CONCERT":
                return Categories.CONCERT;

            case "NIGHTCLUB":
                return Categories.NIGHTCLUB;

            case "BIRTHDAY":
                return Categories.BIRTHDAY;

            case "FESTIVAL":
                return Categories.FESTIVAL;

            default:
                return Categories.PARTY;
        }
    }

    public static void onSearchEventsClickAlle(final Activity activity) {
        final ListView listView = activity.findViewById(R.id.lvalle_eventer);
        ImageButton searchevents = activity.findViewById(R.id.search_events);
        final EditText search_alle_eventer = activity.findViewById(R.id.search_alle_eventer);

        if(listView == null || searchevents == null || search_alle_eventer == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSearchEventsClickAlle(activity);
                }
            }, 300);
            return;
        }

        searchevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bruker.get().getListOfEvents() != null && !Bruker.get().getListOfEvents().isEmpty()) {
                    search_alle_eventer.setVisibility(search_alle_eventer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                    if(search_alle_eventer.getVisibility() == View.VISIBLE) {
                        search_alle_eventer.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(s.toString().isEmpty()) {
                                    listView.setAdapter(new EventAdapter(activity, Bruker.get().getListOfEvents()));
                                    return;
                                }

                                if(s.toString().length() <= 2) {
                                    listView.setAdapter(new EventAdapter(activity, Bruker.get().getListOfEvents()));
                                    return;
                                }

                                List<Event> list = new ArrayList<>();
                                for(Event event : Bruker.get().getListOfEvents()) {
                                    if(event.getHostStr().toLowerCase().contains(s.toString().toLowerCase())) {
                                        list.add(event);
                                        continue;
                                    }

                                    if(event.getNameofevent().toLowerCase().contains(s.toString().toLowerCase()))
                                        list.add(event);
                                }

                                listView.setAdapter(new EventAdapter(activity, list));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });
                    }
                } else {
                    Toast.makeText(activity, "The list is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void getBrukerChauffeur(DataSnapshot dataSnapshot) {
        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            if(snapshot.getKey() != null) {
                switch(snapshot.getKey()) {
                    case "rating":
                        if(snapshot.getValue() != null)
                            Bruker.get().getChauffeur().setM_rating(Double.valueOf(snapshot.getValue(String.class)));
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
                        Bruker.get().getChauffeur().setM_age(Integer.valueOf(snapshot.getValue(String.class)));
                        break;

                    case "capacity":
                        if(snapshot.getValue(Integer.class) != null)
                            Bruker.get().getChauffeur().setM_capacity(snapshot.getValue(Integer.class));
                        break;

                    case "current_car":
                        Bruker.get().getChauffeur().setCurrent_car((Car) new Gson().fromJson(snapshot.getValue(String.class), carType));
                        break;

                    case "timefrom":
                        if(snapshot.getValue(Long.class) != null)
                            Bruker.get().getChauffeur().setChauffeur_time_from(snapshot.getValue(Long.class));
                        break;

                    case "timeto":
                        if(snapshot.getValue(Long.class) != null)
                            Bruker.get().getChauffeur().setChauffeur_time_to(snapshot.getValue(Long.class));
                        break;

                    case "carlist":
                        List<Car> list = new Gson().fromJson(snapshot.getValue(String.class), Utilities.listCarsType);
                        Bruker.get().getChauffeur().setListOfCars(list);
                        break;

                    case "longitude":
                        Bruker.get().getChauffeur().setLongitude(Double.valueOf(snapshot.getValue(String.class)));
                        break;

                    case "latitude":
                        Bruker.get().getChauffeur().setLatitude(Double.valueOf(snapshot.getValue(String.class)));
                        break;
                }
            }
        }
    }

    public static Chauffeur getChauffeurFromDataSnapshot(DataSnapshot dataSnapshot) {
        Chauffeur ret = new Chauffeur();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            if(snapshot.getKey() != null) {
                switch (snapshot.getKey()) {
                    case "rating":
                        ret.setM_rating(Double.valueOf(snapshot.getValue(String.class)));
                        break;

                    case "brukernavn":
                        ret.setM_brukernavn(snapshot.getValue(String.class));
                        break;

                    case "fornavn":
                        ret.setFornavn(snapshot.getValue(String.class));
                        break;

                    case "etternavn":
                        ret.setEtternavn(snapshot.getValue(String.class));
                        break;

                    case "age":
                        ret.setM_age(Integer.valueOf(snapshot.getValue(String.class)));
                        break;

                    case "current_car":
                        ret.setCurrent_car((Car) new Gson().fromJson(snapshot.getValue(String.class), carType));
                        break;

                    case "capacity":
                        if (snapshot.getValue(Integer.class) != null)
                            ret.setM_capacity(snapshot.getValue(Integer.class));
                        break;

                    case "timefrom":
                        if (snapshot.getValue(Long.class) != null)
                            ret.setChauffeur_time_from(snapshot.getValue(Long.class));
                        break;

                    case "timeto":
                        if (snapshot.getValue(Long.class) != null)
                            ret.setChauffeur_time_to(snapshot.getValue(Long.class));
                        break;

                    case "carlist":
                        List<Car> list = new Gson().fromJson(snapshot.getValue(String.class), Utilities.listCarsType);
                        ret.setListOfCars(list);
                        break;

                    case "longitude":
                        ret.setLongitude(Double.valueOf(snapshot.getValue(String.class)));
                        break;

                    case "latitude":
                        ret.setLatitude(Double.valueOf(snapshot.getValue(String.class)));
                        break;
                }
            }
        }

        if(ret.getChauffeur_time_to() < System.currentTimeMillis())
            return null;

        return ret;
    }

    public static Event getEventFromDataSnapshot(DataSnapshot eventData) {
        Event event = new Event();

        for(DataSnapshot eventinfo : eventData.getChildren()) {
            if(eventinfo.getKey() != null) {
                switch(eventinfo.getKey()) {
                    case "nameofevent":
                        event.setNameofevent(eventinfo.getValue(String.class));
                        break;

                    case "address":
                        event.setAddress(eventinfo.getValue(String.class));
                        break;

                    case "agerestriction":
                        if(eventinfo.getValue(Integer.class) != null) {
                            event.setAgerestriction(eventinfo.getValue(Integer.class));
                        }
                        break;

                    case "country":
                        event.setCountry(eventinfo.getValue(String.class));
                        break;

                    case "datefrom":
                        if(eventinfo.getValue(Long.class) != null) {
                            event.setDatefrom(eventinfo.getValue(Long.class));
                        }
                        break;

                    case "dateto":
                        if(eventinfo.getValue(Long.class) != null) {
                            event.setDateto(eventinfo.getValue(Long.class));
                        }
                        break;

                    case "desc":
                        event.setDescription(eventinfo.getValue(String.class));
                        break;

                    case "hasimage":
                        if(eventinfo.getValue(Boolean.class) != null) {
                            event.setHasimage(eventinfo.getValue(Boolean.class));
                        }
                        break;

                    case "hostStr":
                        event.setHostStr(eventinfo.getValue(String.class));
                        break;

                    case "isprivate":
                        if(eventinfo.getValue(Boolean.class) != null) {
                            event.setPrivateEvent(eventinfo.getValue(Boolean.class));
                        }
                        break;

                    case "latitude":
                        event.setLatitude(eventinfo.getValue(Double.class));
                        break;

                    case "longitude":
                        event.setLongitude(eventinfo.getValue(Double.class));
                        break;

                    case "maxparticipants":
                        if(eventinfo.getValue(Integer.class) != null) {
                            event.setMaxparticipants(eventinfo.getValue(Integer.class));
                        }
                        break;

                    case "participants":
                        List<Participant> participants = new Gson().fromJson(eventinfo.getValue(String.class), listParticipantsType);
                        event.setParticipants(participants);
                        break;

                    case "requests":
                        List<Requester> requests = new Gson().fromJson(eventinfo.getValue(String.class), listRequestsType);
                        event.setRequests(requests);
                        break;

                    case "showaddress":
                        if(eventinfo.getValue(Boolean.class) != null) {
                            event.setShowaddress(eventinfo.getValue(Boolean.class));
                        }
                        break;

                    case "showguestlist":
                        if(eventinfo.getValue(Boolean.class) != null) {
                            event.setShowguestlist(eventinfo.getValue(Boolean.class));
                        }
                        break;

                    case "town":
                        event.setTown(eventinfo.getValue(String.class));
                        break;

                    case "category":
                        event.setCategory(getCategoryFromString(eventinfo.getValue(String.class)));
                        break;
                }
            }
        }

        if(eventData.getKey() != null && !eventData.getKey().equals("eventidCounter"))
            event.setEventId(Long.valueOf(eventData.getKey()));

        return event;
    }

    public static void onSearchMineEventer(final Activity activity) {
        final ListView lvmine_eventer = activity.findViewById(R.id.lvmine_eventer);
        final EditText search_mine_eventer = activity.findViewById(R.id.search_mine_eventer);
        final ImageButton search_events = activity.findViewById(R.id.search_events);

        if(lvmine_eventer == null || search_events == null || search_mine_eventer == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSearchMineEventer(activity);
                }
            }, 300);
            return;
        }

        search_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bruker.get().getListOfMyEvents() != null && !Bruker.get().getListOfMyEvents().isEmpty()) {
                    search_mine_eventer.setVisibility(search_mine_eventer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                    if(search_mine_eventer.getVisibility() == View.VISIBLE) {
                        search_mine_eventer.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(s.toString().isEmpty()) {
                                    lvmine_eventer.setAdapter(new EventAdapter(activity, Bruker.get().getListOfMyEvents()));
                                    return;
                                }

                                if(s.toString().length() <= 2) {
                                    lvmine_eventer.setAdapter(new EventAdapter(activity, Bruker.get().getListOfMyEvents()));
                                    return;
                                }

                                List<Event> list = new ArrayList<>();
                                for(Event event : Bruker.get().getListOfMyEvents()) {
                                    if(event.getHostStr().toLowerCase().contains(s.toString().toLowerCase())) {
                                        list.add(event);
                                        continue;
                                    }

                                    if(event.getNameofevent().toLowerCase().contains(s.toString().toLowerCase()))
                                        list.add(event);
                                }

                                lvmine_eventer.setAdapter(new EventAdapter(activity, list));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });
                    }
                } else
                    Toast.makeText(activity, activity.getResources().getString(R.string.you_dont_have_events), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void parseBrukerInfo(DataSnapshot snapshot) {
        if (snapshot.getKey() != null) {
            switch (snapshot.getKey()) {
                case "fornavn":
                    Bruker.get().setFornavn(snapshot.getValue(String.class));
                    break;

                case "etternavn":
                    Bruker.get().setEtternavn(snapshot.getValue(String.class));
                    break;

                case "premium":
                    if (snapshot.getValue(Boolean.class) != null)
                        Bruker.get().setPremium(snapshot.getValue(Boolean.class));
                    break;

                case "country":
                    Bruker.get().setCountry(snapshot.getValue(String.class));
                    break;

                case "day_of_month":
                    if (snapshot.getValue(Integer.class) != null)
                        Bruker.get().setDay_of_month(snapshot.getValue(Integer.class));
                    break;

                case "friendlist":
                    List<Friend> list = new Gson().fromJson(snapshot.getValue(String.class), Utilities.listFriendsType);
                    Bruker.get().setFriendList(list);
                    break;

                case "requestlist":
                    List<Friend> requests = new Gson().fromJson(snapshot.getValue(String.class), Utilities.listFriendsType);
                    Bruker.get().setRequests(requests);
                    break;

                case "month":
                    if (snapshot.getValue(Integer.class) != null)
                        Bruker.get().setMonth(snapshot.getValue(Integer.class));
                    break;

                case "town":
                    Bruker.get().setTown(snapshot.getValue(String.class));
                    break;

                case "year":
                    if (snapshot.getValue(Integer.class) != null)
                        Bruker.get().setYear(snapshot.getValue(Integer.class));
                    break;

                case "oneliner":
                    Bruker.get().setOneliner(snapshot.getValue(String.class));
                    break;
            }
        }
    }

    public static void setupOnRestart(final Activity activity) {
        if(SplashActivity.mAuth.getCurrentUser() == null && Bruker.get().getEmail() != null && !Bruker.get().getEmail().isEmpty()
                && Bruker.get().getPassord() != null && !Bruker.get().getPassord().isEmpty()) {
            SplashActivity.mAuth.signInWithEmailAndPassword(Bruker.get().getEmail(), Bruker.get().getPassord())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Bruker.get().GetAndParseEvents(activity);
                                Bruker.get().GetAndParseBrukerInfo();
                                Bruker.get().GetAndParseChauffeurs(activity);
                                Bruker.get().GetAndParseBrukerChauffeur();

                                Utilities.startChatListener(activity);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(Bruker.get().getBrukernavn());
                                ref.child("loggedon").setValue(true);
                                Bruker.get().setLoggetpa(true);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, "Failed to log you in, please try again.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intent);
                }
            });
        }
    }

    public static void setupOnStop(Activity activity) {
        if(ApplicationLifecycleMgr.isAppVisible())
            return;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(Bruker.get().getBrukernavn());
        ref.child("loggedon").setValue(false);
        Bruker.get().setLoggetpa(false);

        Intent intent = new Intent(activity, NotificationService.class);
        intent.putExtra("user", Bruker.get().getBrukernavn());
        activity.startService(intent);

        Bruker.get().StopParsingBrukerInfo();
        Bruker.get().StopParsingEvents();
        Bruker.get().StopParsingChauffeurs();
        Bruker.get().StopParsingBrukerChauffeur();

        if(ProfilActivity.valueEventListener != null && ProfilActivity.ref != null)
            ProfilActivity.ref.removeEventListener(ProfilActivity.valueEventListener);

        if(SplashActivity.mAuth.getCurrentUser() != null)
            SplashActivity.mAuth.signOut();
    }

    public static void onSearchMineChats(final Activity activity) {
        final ListView lvmine_chats = activity.findViewById(R.id.lv_chat);
        final EditText search_chats = activity.findViewById(R.id.search_chat);
        final ImageButton search_events = activity.findViewById(R.id.search_events);

        if(lvmine_chats == null || search_events == null || search_chats == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSearchMineChats(activity);
                }
            }, 300);
            return;
        }

        search_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bruker.get().getChatMessageList() != null && !Bruker.get().getChatMessageList().isEmpty()) {
                    search_chats.setVisibility(search_chats.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                    if(search_chats.getVisibility() == View.VISIBLE) {
                        search_chats.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(s.toString().isEmpty()) {
                                    lvmine_chats.setAdapter(new ChatPreviewAdapter(activity, Bruker.get().getChatMessageList()));
                                    return;
                                }

                                if(s.toString().length() <= 2) {
                                    lvmine_chats.setAdapter(new ChatPreviewAdapter(activity, Bruker.get().getChatMessageList()));
                                    return;
                                }

                                List<ChatPreview> list = new ArrayList<>();
                                for(ChatPreview preview : Bruker.get().getChatMessageList()) {
                                    if(preview.isGroupchat() && !preview.getGroupname().isEmpty() && preview.getGroupname().toLowerCase().contains(s.toString().toLowerCase())) {
                                        list.add(preview);
                                        continue;
                                    }

                                    for(Chatter chatter : preview.getChatters()) {
                                        if(chatter.getBrukernavn().equals(Bruker.get().getBrukernavn()))
                                            continue;

                                        if(chatter.getBrukernavn().toLowerCase().contains(s.toString().toLowerCase())) {
                                            list.add(preview);
                                            continue;
                                        }

                                        if(chatter.getEtternavn().toLowerCase().contains(s.toString().toLowerCase())) {
                                            list.add(preview);
                                            continue;
                                        }

                                        if(chatter.getFornavn().toLowerCase().contains(s.toString().toLowerCase()))
                                            list.add(preview);
                                    }
                                }

                                lvmine_chats.setAdapter(new ChatPreviewAdapter(activity, list));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });
                    }
                } else {
                    Toast.makeText(activity, "The list is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void onSearchVenner(final Activity activity) {
        final ListView lv_venner = activity.findViewById(R.id.lv_venner);
        final EditText search_venner = activity.findViewById(R.id.search_venner);
        final ImageButton search_events = activity.findViewById(R.id.search_events);

        if(lv_venner == null || search_events == null || search_venner == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSearchVenner(activity);
                }
            }, 300);
            return;
        }

        search_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bruker.get().getFriendList() != null && !Bruker.get().getFriendList().isEmpty()) {
                    search_venner.setVisibility(search_venner.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                    if(search_venner.getVisibility() == View.VISIBLE) {
                        search_venner.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(s.toString().isEmpty()) {
                                    lv_venner.setAdapter(new FriendListAdapter(activity, Bruker.get().getFriendList()));
                                    return;
                                }

                                if(s.toString().length() <= 2)
                                    return;

                                List<Friend> list = new ArrayList<>();
                                for(Friend friend : Bruker.get().getFriendList()) {
                                    if(friend.getBrukernavn().toLowerCase().contains(s.toString().toLowerCase())) {
                                        list.add(friend);
                                        continue;
                                    }

                                    if(friend.getFornavn().toLowerCase().contains(s.toString().toLowerCase())) {
                                        list.add(friend);
                                        continue;
                                    }

                                    if(friend.getEtternavn().toLowerCase().contains(s.toString().toLowerCase()))
                                        list.add(friend);
                                }

                                lv_venner.setAdapter(new FriendListAdapter(activity, list));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });
                    }
                } else {
                    Toast.makeText(activity, "The list is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void CheckAddEvent(EditText dato, EditText time, EditText datotil, EditText timetil, EditText titletext, Place attainedPlace, EditText aldersgrense, EditText maks_deltakere,
                                     EditText beskrivelse, Dialog dialog, CheckBox vis_gjesteliste, CheckBox alle_deltakere, CheckBox vis_adresse, AppCompatSpinner kategorier, boolean shouldupdate,
                                     @Nullable Long eventid) {
        String[] location = attainedPlace.getAddress().toString().split(",");

        if(!datotil.getText().toString().isEmpty() && !timetil.getText().toString().isEmpty()) {
            GregorianCalendar datefrom, dateto;

            if(dato.getText().toString().contains("."))
                datefrom = getDateFromString(String.format(Locale.ENGLISH, "%s %s", dato.getText().toString(), time.getText().toString()), "dd MMMM yyyy HH:mm");
            else
                datefrom = getDateFromString(String.format(Locale.ENGLISH, "%s %s", dato.getText().toString(), time.getText().toString()), "dd MMM yyyy HH:mm");

            if(datotil.getText().toString().contains("."))
                dateto = getDateFromString(String.format(Locale.ENGLISH, "%s %s", datotil.getText().toString(), timetil.getText().toString()), "dd MMMM yyyy HH:mm");
            else
                dateto = getDateFromString(String.format(Locale.ENGLISH, "%s %s", datotil.getText().toString(), timetil.getText().toString()), "dd MMM yyyy HH:mm");

            if(dateto != null && datefrom != null) {
                if(dateto.before(datefrom)) {
                    Toast.makeText(dato.getContext(), "Please choose a ending date and time that is after the starting date and time.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(datefrom.before(new GregorianCalendar().getTime())) {
                    Toast.makeText(dato.getContext(), "You have to choose a time in the future.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(dato.getContext(), "Failed to get date and time.", Toast.LENGTH_SHORT).show();
                return;
            }

            Event creating_event = new Event(shouldupdate ? eventid : 0, titletext.getText().toString(), location[0], location[2].trim(), Bruker.get().getBrukernavn(),
                    alle_deltakere.isChecked(), attainedPlace.getLatLng().longitude, attainedPlace.getLatLng().latitude, datefrom.getTimeInMillis(), dateto.getTimeInMillis(), Integer.valueOf(aldersgrense.getText().toString()),
                    new ArrayList<>(Collections.singletonList(Participant.convertBrukerParticipant(Bruker.get(), EventStilling.VERT))), Integer.valueOf(maks_deltakere.getText().toString()),
                    location[1].trim(), beskrivelse.getText().toString(), vis_gjesteliste.isChecked(), vis_adresse.isChecked(), new ArrayList<Requester>(),
                    ProfilActivity.imageChange.getBmp() != null, getCategoryFromString((String) kategorier.getSelectedItem()));

            if(shouldupdate) {
                if(EventDetails.edit_event_imagechange.getBmp() != null)
                    creating_event.setHasimage(true);
                else {
                    Event event = Bruker.get().getEventFromID(eventid);
                    if(event.isHasimage()) {
                        StorageReference ref = ProfilActivity.storage.getReference().child(event.getHostStr() + "_" + event.getNameofevent());
                        ref.delete();
                    }

                    creating_event.setHasimage(false);
                }

                UpdateEvent updateEvent = new UpdateEvent(dialog.getOwnerActivity(), creating_event, EventDetails.edit_event_imagechange.getBmp());
                updateEvent.execute();
            } else {
                AddEvent addEvent = new AddEvent(dialog, creating_event, ProfilActivity.imageChange.getBmp());
                addEvent.execute();
            }
        } else if(datotil.getText().toString().isEmpty() && timetil.getText().toString().isEmpty()) {
            GregorianCalendar datefrom;

            if(dato.getText().toString().contains("."))
                datefrom = getDateFromString(String.format(Locale.ENGLISH, "%s %s", dato.getText().toString(), time.getText().toString()), "dd MMMM yyyy HH:mm");
            else
                datefrom = getDateFromString(String.format(Locale.ENGLISH, "%s %s", dato.getText().toString(), time.getText().toString()), "dd MMM yyyy HH:mm");

            if(datefrom != null) {
                if(datefrom.before(new GregorianCalendar().getTime())) {
                    Toast.makeText(dato.getContext(), "You have to choose a time in the future.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(dato.getContext(), "Failed to get date and time.", Toast.LENGTH_SHORT).show();
                return;
            }

            Event creating_event = new Event(shouldupdate ? eventid : 0, titletext.getText().toString(), location[0], location[2].trim(), Bruker.get().getBrukernavn(),
                    alle_deltakere.isChecked(),attainedPlace.getLatLng().longitude, attainedPlace.getLatLng().latitude, datefrom.getTimeInMillis(), 0, Integer.valueOf(aldersgrense.getText().toString()),
                    new ArrayList<>(Collections.singletonList(Participant.convertBrukerParticipant(Bruker.get(), EventStilling.VERT))), Integer.valueOf(maks_deltakere.getText().toString()),
                    location[1].trim(), beskrivelse.getText().toString(), vis_gjesteliste.isChecked(), vis_adresse.isChecked(), new ArrayList<Requester>(),
                    ProfilActivity.imageChange.getBmp() != null, getCategoryFromString((String) kategorier.getSelectedItem()));

            if(shouldupdate) {
                if(EventDetails.edit_event_imagechange.getBmp() != null)
                    creating_event.setHasimage(true);
                else {
                    Event event = Bruker.get().getEventFromID(eventid);
                    if(event.isHasimage()) {
                        StorageReference ref = ProfilActivity.storage.getReference().child(event.getHostStr() + "_" + event.getNameofevent());
                        ref.delete();
                    }

                    creating_event.setHasimage(false);
                }

                UpdateEvent updateEvent = new UpdateEvent(dialog.getOwnerActivity(), creating_event, EventDetails.edit_event_imagechange.getBmp());
                updateEvent.execute();
            } else {
                AddEvent addEvent = new AddEvent(dialog, creating_event, ProfilActivity.imageChange.getBmp());
                addEvent.execute();
            }
        }
    }

    public static Bitmap correctOrientation(String path, Bitmap bmp) {
        try {
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotate(bmp, 90);

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotate(bmp, 180);

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotate(bmp, 270);

                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    return flip(bmp, true, false);

                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    return flip(bmp, false, true);

                default:
                    return bmp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * This is used to calculate the age of a user.
     *
     * @param DOB Date-Of-Birth calendar to be used in calculation.
     * @return Returns age.
     */
    public static int calcAge(Calendar DOB) {
        int result;

        Calendar now = Calendar.getInstance();

        int year1 = now.get(Calendar.YEAR);
        int year2 = DOB.get(Calendar.YEAR);
        result = year1 - year2;
        int month1 = now.get(Calendar.MONTH) + 1;
        int month2 = DOB.get(Calendar.MONTH);
        if (month2 > month1) {
            result--;
        } else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = DOB.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                result--;
            }
        }

        return result;
    }

    public static int getDateStrChat(GregorianCalendar calendar) {
        GregorianCalendar today = new GregorianCalendar();
        today.setTime(new Date(System.currentTimeMillis()));

        if(calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH) && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            return 1;
            //return "Today";
        }

        if((today.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH)) == 1 && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            return 2;
            //return "Yesterday";
        }

        if((today.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH)) < 7 && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            return 3;
            //return String.format(Locale.ENGLISH, "%s", calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
        }

        if(calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            return 4;
            //return String.format(Locale.ENGLISH, "%d %s", calendar.get(Calendar.DAY_OF_MONTH), calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }

        if(calendar.get(Calendar.MONTH) != today.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            return 4;
            //return String.format(Locale.ENGLISH, "%d %s", calendar.get(Calendar.DAY_OF_MONTH), calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }

        if(calendar.get(Calendar.YEAR) != today.get(Calendar.YEAR)) {
            return 5;
            //return String.format(Locale.ENGLISH, "%d %s %d", calendar.get(Calendar.DAY_OF_MONTH), calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()), calendar.get(Calendar.YEAR));
        }

        return 0;
    }

    /**
     * This is used to prevent viewing an unloaded layout
     *
     * @param views the views passed to make visible
     */
    public static void makeVisible(View ... views) {
        if(views == null || views.length == 0) {
            return;
        }

        for(View view : views) {
            if(view == null)
                continue;

            view.setVisibility(View.VISIBLE);
        }
    }

    public static void showEventDetailRequests(final Activity activity, final Event event) {
        final Dialog requestdialog = new Dialog(activity);
        requestdialog.requestWindowFeature(1);
        requestdialog.setContentView(R.layout.foresporsler);
        requestdialog.setCancelable(true);
        requestdialog.setCanceledOnTouchOutside(true);

        Toolbar toolbarrequests = requestdialog.findViewById(R.id.toolbar_foresporsler);
        TextView toolbar_title = requestdialog.findViewById(R.id.foresporsel_TB_text);
        ImageButton search = requestdialog.findViewById(R.id.foresporsel_search);
        final EditText foresporsel_search = requestdialog.findViewById(R.id.foresporsel_search_field);
        final ListView foresporsel_list = requestdialog.findViewById(R.id.lv_foresporsler);

        foresporsel_list.setAdapter(new RequestAdapter(activity, event.getRequests(), event.getEventId()));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foresporsel_search.setVisibility(foresporsel_search.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                foresporsel_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.toString().isEmpty()) {
                            foresporsel_list.setAdapter(new RequestAdapter(activity, event.getRequests(), event.getEventId()));
                            return;
                        }

                        List<Requester> list = new ArrayList<>();
                        for(Requester requester : event.getRequests()) {
                            if(requester.getBrukernavn().contains(s)) {
                                list.add(requester);
                            }
                        }

                        foresporsel_list.setAdapter(new RequestAdapter(activity, list, event.getEventId()));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
            }
        });

        toolbar_title.setTypeface(typeface);

        toolbarrequests.setNavigationIcon(activity.getResources().getDrawable(R.drawable.left_arrow));

        toolbarrequests.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestdialog.onBackPressed();
            }
        });

        requestdialog.show();
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap decodeSampledBitmapFromByteArray(byte[] arr, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(arr, 0, arr.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(arr, 0, arr.length, options);
    }
}