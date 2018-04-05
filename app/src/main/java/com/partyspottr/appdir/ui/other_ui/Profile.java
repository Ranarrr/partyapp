package com.partyspottr.appdir.ui.other_ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.ChatPreview;
import com.partyspottr.appdir.classes.Chatter;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.CountryCodes;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.alle_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.mine_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.mitt_arkiv_fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.partyspottr.appdir.ui.MainActivity.typeface;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.profil);

        String brukernavn;

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            return;
        } else {
            brukernavn = extras.getString("user");

            if(brukernavn == null || brukernavn.isEmpty())
                return;
        }

        StringRequest stringRequest = new StringRequest(BuildConfig.DBMS_URL + "?get_user={\"socketElem\":\"" + Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(),
                Base64.DEFAULT) + "\",\"username\":\"" + brukernavn + "\"}", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response == null || response.isEmpty()) {
                    Toast.makeText(Profile.this, "Failed to load profile!", Toast.LENGTH_SHORT).show();
                    return;
                }

                TextView fornavn_etternavn = findViewById(R.id.fornavn_etternavn);
                TextView by = findViewById(R.id.profil_by);
                ImageView countryflag = findViewById(R.id.countryflag_profil);
                TextView oneliner = findViewById(R.id.profil_oneliner);
                TextView title = findViewById(R.id.brukernavn_profil);
                Button send_message = findViewById(R.id.send_message);
                Button add_friend = findViewById(R.id.add_friend);
                ConstraintLayout content = findViewById(R.id.profile_content);
                CustomViewPager pager = findViewById(R.id.profile_eventpager);

                Point size = new Point();
                getWindowManager().getDefaultDisplay().getSize(size);
                content.setBackground(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.forsidebilde), size.x, size.y, true)));

                send_message.setTypeface(MainActivity.typeface);
                add_friend.setTypeface(MainActivity.typeface);
                fornavn_etternavn.setTypeface(MainActivity.typeface);
                by.setTypeface(MainActivity.typeface);
                oneliner.setTypeface(MainActivity.typeface);
                title.setTypeface(MainActivity.typeface);

                final Bruker fullparticipant = Bruker.retBrukerFromJSON(response);

                if(fullparticipant != null) {
                    List<Integer> eventids = Bruker.get().getAllEventIDUserGoingTo(fullparticipant.getBrukernavn());

                    EventSlidePagerAdapter adapter = new EventSlidePagerAdapter(Profile.this, eventids);

                    pager.setAdapter(adapter);

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
                                    list.add(new Chatter(fullparticipant.getBrukernavn(), fullparticipant.getFornavn(), fullparticipant.getEtternavn()));
                                    if(Bruker.get().startChat(new ChatPreview(message.getText().toString(), "", false, list), fullparticipant.getBrukernavn(), message.getText().toString())) {
                                        Toast.makeText(Profile.this, "Started a chat with " + fullparticipant.getBrukernavn(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            send_msg.show();
                        }
                    });

                    title.setText(fullparticipant.getBrukernavn());

                    if(fullparticipant.getTown() == null || fullparticipant.getTown().isEmpty()) {
                        by.setText(fullparticipant.getCountry());
                    } else {
                        by.setText(String.format(Locale.ENGLISH, "%s, %s", fullparticipant.getCountry(), fullparticipant.getTown()));
                    }

                    fornavn_etternavn.setText(String.format(Locale.ENGLISH, "%s %s, %d", fullparticipant.getFornavn(), fullparticipant.getEtternavn(),
                            Utilities.calcAge(new GregorianCalendar(fullparticipant.getYear(),
                                    fullparticipant.getMonth(), fullparticipant.getDay_of_month()))));

                    if(fullparticipant.getOneliner().isEmpty()) {
                        oneliner.setVisibility(View.GONE);
                    } else {
                        oneliner.setText(fullparticipant.getOneliner());
                    }

                    if(!Bruker.get().getCountry().equals("Dominican Republic")) { // because android studio reserves the resource name "do"
                        String identifier = CountryCodes.getCountrySign(fullparticipant.getCountry()).toLowerCase();

                        int resource = Profile.this.getResources().getIdentifier(identifier, "drawable", Profile.this.getPackageName());

                        if(resource > 0) {
                            Drawable drawable = Profile.this.getResources().getDrawable(resource);

                            countryflag.setImageDrawable(drawable);
                        }
                    } else {
                        countryflag.setImageResource(R.drawable.dominican_republic);
                    }
                } else {
                    Toast.makeText(Profile.this, "Failed to load profile!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getCause().printStackTrace();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public class CustomViewPager extends ViewPager {

        public CustomViewPager(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int height = 0;
            for(int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if(h > height) height = h;
            }

            if (height != 0) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
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
                TextView hostText = v.findViewById(R.id.hostText);
                TextView datoText = v.findViewById(R.id.datoText);
                //ImageView bildeIListe = v.findViewById(R.id.imageView2);

                arrangementNavn.setTypeface(typeface);
                stedText.setTypeface(typeface);
                hostText.setTypeface(typeface);
                datoText.setTypeface(typeface);

                arrangementNavn.setText(event.getNameofevent());
                stedText.setText(event.getAddress());
                hostText.setText(event.getHostStr());

                datoText.setText(String.format(Locale.ENGLISH, "%d %s %d", event.getDatefrom().get(Calendar.DAY_OF_MONTH), event.getDatefrom().getDisplayName(Calendar.MONTH, Calendar.SHORT,
                        inflater.getContext().getResources().getConfiguration().locale).toLowerCase(), event.getDatefrom().get(Calendar.YEAR)));
            }

            container.addView(v);

            return v;
        }

        @Override
        public int getCount() {
            return eventIds.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == (View) object);
        }
    }
}
