package com.partyspottr.appdir.ui.other_ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.ChatPreview;
import com.partyspottr.appdir.classes.Chatter;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.Friend;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.CountryCodes;
import com.partyspottr.appdir.classes.customviews.CustomViewPager;
import com.partyspottr.appdir.ui.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.partyspottr.appdir.ui.MainActivity.typeface;

/**
 * Created by Ranarrr on 05-Apr-18.
 *
 * @author Ranarrr
 */

public class Profile extends AppCompatActivity {
    @Override
    protected void onStop() {
        Utilities.setupOnStop();

        super.onStop();
    }

    @Override
    protected void onRestart() {
        if(!Bruker.get().isConnected()) {
            super.onRestart();
            return;
        }

        Utilities.setupOnRestart(this);

        super.onRestart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.profil);

        final String brukernavn;

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            return;
        } else {
            brukernavn = extras.getString("user");

            if(brukernavn == null || brukernavn.isEmpty())
                return;
        }

        final TextView fornavn_etternavn = findViewById(R.id.fornavn_etternavn);
        final TextView by = findViewById(R.id.profil_by);
        final ImageView countryflag = findViewById(R.id.countryflag_profil);
        final TextView oneliner = findViewById(R.id.profil_oneliner);
        final TextView title = findViewById(R.id.brukernavn_profil);
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        final Button send_message = findViewById(R.id.send_message);
        final Button add_friend = findViewById(R.id.add_friend);
        LinearLayout content = findViewById(R.id.profile_content);
        final CustomViewPager pager = findViewById(R.id.customviewpager);
        final TextView skal_paa = findViewById(R.id.profil_going_to);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        skal_paa.setTypeface(MainActivity.typeface);
        send_message.setTypeface(MainActivity.typeface);
        add_friend.setTypeface(MainActivity.typeface);
        fornavn_etternavn.setTypeface(MainActivity.typeface);
        by.setTypeface(MainActivity.typeface);
        oneliner.setTypeface(MainActivity.typeface);
        title.setTypeface(MainActivity.typeface);

        DatabaseReference brukerref = FirebaseDatabase.getInstance().getReference("users");

        brukerref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey() != null && dataSnapshot.getKey().equals(brukernavn)) {
                    final Bruker bruker = new Bruker();

                    bruker.setBrukernavn(brukernavn);

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getKey() != null) {
                            switch(snapshot.getKey()) {
                                case "country":
                                    bruker.setCountry(snapshot.getValue(String.class));
                                    break;

                                case "day_of_month":
                                    if(snapshot.getValue(Integer.class) != null)
                                        bruker.setDay_of_month(snapshot.getValue(Integer.class));
                                    break;

                                case "etternavn":
                                    bruker.setEtternavn(snapshot.getValue(String.class));
                                    break;

                                case "fornavn":
                                    bruker.setFornavn(snapshot.getValue(String.class));
                                    break;

                                case "friendlist":
                                    List<Friend> friendList = new Gson().fromJson(snapshot.getValue(String.class), Utilities.listFriendsType);
                                    bruker.setFriendList(friendList);
                                    break;

                                case "loggedon":
                                    if(snapshot.getValue(Boolean.class) != null)
                                        bruker.setLoggetpa(snapshot.getValue(Boolean.class));
                                    break;

                                case "month":
                                    if(snapshot.getValue(Integer.class) != null)
                                        bruker.setMonth(snapshot.getValue(Integer.class));
                                    break;

                                case "oneliner":
                                    bruker.setOneliner(snapshot.getValue(String.class));
                                    break;

                                case "premium":
                                    if(snapshot.getValue(Boolean.class) != null)
                                        bruker.setPremium(snapshot.getValue(Boolean.class));
                                    break;

                                case "town":
                                    bruker.setTown(snapshot.getValue(String.class));
                                    break;

                                case "year":
                                    if(snapshot.getValue(Integer.class) != null)
                                        bruker.setYear(snapshot.getValue(Integer.class));
                                    break;
                            }
                        }
                    }

                    if(bruker.getBrukernavn() != null && !bruker.getBrukernavn().isEmpty()) {
                        ConstraintLayout layout1 = findViewById(R.id.profile_info);
                        LinearLayout layout = findViewById(R.id.profile_events);
                        ConstraintLayout layout2 = findViewById(R.id.profile_buttons);

                        Utilities.makeVisible(layout, layout1, layout2);

                        List<Integer> eventids = Bruker.get().getAllEventIDUserGoingTo(bruker.getBrukernavn());

                        if(eventids.size() == 0) {
                            pager.setVisibility(View.GONE);
                            skal_paa.setText("Denne brukeren skal ikke på en event.");
                        } else {
                            EventSlidePagerAdapter adapter = new EventSlidePagerAdapter(Profile.this, eventids);
                            pager.setAdapter(adapter);
                        }

                        send_message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Dialog send_msg = new Dialog(Profile.this);
                                send_msg.setCancelable(true);
                                send_msg.setCanceledOnTouchOutside(true);
                                send_msg.requestWindowFeature(1);
                                send_msg.setContentView(R.layout.send_message_dialog);

                                if(send_msg.getWindow() != null) {
                                    WindowManager.LayoutParams layoutParams = send_msg.getWindow().getAttributes();

                                    DisplayMetrics dm = new DisplayMetrics();

                                    Profile.this.getWindowManager().getDefaultDisplay().getMetrics(dm);

                                    send_msg.getWindow().setBackgroundDrawable(null);

                                    layoutParams.width = dm.widthPixels;

                                    send_msg.getWindow().setAttributes(layoutParams);
                                    send_msg.getWindow().setGravity(Gravity.BOTTOM);

                                }

                                send_msg.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialog) {
                                        if (send_msg.getWindow() != null) {
                                            View view = send_msg.getWindow().getDecorView();

                                            ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0.0f).start();
                                        }
                                    }
                                });

                                Button send = send_msg.findViewById(R.id.send_new_msg_btn);

                                send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        EditText message = send_msg.findViewById(R.id.send_new_msg_text);

                                        List<Chatter> list = new ArrayList<>();
                                        list.add(new Chatter(Bruker.get().getBrukernavn(), Bruker.get().getFornavn(), Bruker.get().getEtternavn()));
                                        list.add(new Chatter(bruker.getBrukernavn(), bruker.getFornavn(), bruker.getEtternavn()));
                                        Bruker.get().startChat(Profile.this, new ChatPreview(message.getText().toString(), "", false, list), bruker.getBrukernavn(), message.getText().toString());
                                    }
                                });

                                send_msg.show();
                            }
                        });

                        title.setText(bruker.getBrukernavn());

                        if(bruker.getTown() == null || bruker.getTown().isEmpty()) {
                            by.setText(bruker.getCountry());
                        } else {
                            by.setText(String.format(Locale.ENGLISH, "%s, %s", bruker.getCountry(), bruker.getTown()));
                        }

                        fornavn_etternavn.setText(String.format(Locale.ENGLISH, "%s %s, %d", bruker.getFornavn(), bruker.getEtternavn(),
                                Utilities.calcAge(new GregorianCalendar(bruker.getYear(),
                                        bruker.getMonth(), bruker.getDay_of_month()))));

                        if(bruker.getOneliner().isEmpty()) {
                            oneliner.setVisibility(View.GONE);
                        } else {
                            oneliner.setText(bruker.getOneliner());
                        }

                        if(!Bruker.get().getCountry().equals("Dominican Republic")) { // because android studio reserves the resource name "do"
                            String identifier = CountryCodes.getCountrySign(bruker.getCountry()).toLowerCase();

                            int resource = Profile.this.getResources().getIdentifier(identifier, "drawable", Profile.this.getPackageName());

                            if(resource > 0) {
                                Drawable drawable = Profile.this.getResources().getDrawable(resource);

                                countryflag.setImageDrawable(drawable);
                            }
                        } else {
                            countryflag.setImageResource(R.drawable.dominican_republic);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(databaseError.getCode() == DatabaseError.OPERATION_FAILED) {
                    Toast.makeText(Profile.this, "Failed to load profile!", Toast.LENGTH_SHORT).show();
                    Profile.this.onBackPressed();
                }
            }
        });
    }

    private class EventSlidePagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        private List<Integer> eventIds;

        public EventSlidePagerAdapter(Activity activity, List<Integer> eventids) {
            inflater = activity.getLayoutInflater();
            eventIds = eventids;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = inflater.inflate(R.layout.event, container, false);

            Event event = Bruker.get().getListOfEvents().get(eventIds.get(position));

            if(event != null) {
                TextView arrangementNavn = v.findViewById(R.id.eventText);
                TextView stedText = v.findViewById(R.id.stedText);
                TextView hostText = v.findViewById(R.id.aldersgrenseText);
                TextView datoText = v.findViewById(R.id.datoText);
                //ImageView bildeIListe = v.findViewById(R.id.imageView2);

                arrangementNavn.setTypeface(typeface);
                stedText.setTypeface(typeface);
                hostText.setTypeface(typeface);
                datoText.setTypeface(typeface);

                arrangementNavn.setText(event.getNameofevent());
                stedText.setText(event.getAddress());
                hostText.setText(event.getHostStr());

                GregorianCalendar datefrom = new GregorianCalendar();
                datefrom.setTimeInMillis(event.getDatefrom());

                datoText.setText(String.format(Locale.ENGLISH, "%d %s %d", datefrom.get(Calendar.DAY_OF_MONTH), datefrom.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                        inflater.getContext().getResources().getConfiguration().locale).toLowerCase(), datefrom.get(Calendar.YEAR)));
            }

            container.addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return eventIds.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }
}
