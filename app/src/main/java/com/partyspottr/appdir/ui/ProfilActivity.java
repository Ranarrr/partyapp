package com.partyspottr.appdir.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.ChatPreview;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.ImageChange;
import com.partyspottr.appdir.classes.Participant;
import com.partyspottr.appdir.classes.Requester;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.ChatPreviewAdapter;
import com.partyspottr.appdir.classes.networking.GetLocationInfo;
import com.partyspottr.appdir.enums.EventStilling;
import com.partyspottr.appdir.ui.mainfragments.bilfragment;
import com.partyspottr.appdir.ui.mainfragments.chatchildfragments.mine_chats_fragment;
import com.partyspottr.appdir.ui.mainfragments.chatchildfragments.venner_fragment;
import com.partyspottr.appdir.ui.mainfragments.chatfragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.alle_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.mine_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventfragment;
import com.partyspottr.appdir.ui.mainfragments.profilfragment;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.partyspottr.appdir.ui.MainActivity.typeface;

/**
 * Created by Ranarrr on 26-Jan-18.
 *
 * @author Ranarrr
 */

public class ProfilActivity extends AppCompatActivity {
    public static List<String> childfragmentsinstack;
    public static List<String> childfragmentsinstackChat;

    boolean bool;

    public static ValueEventListener valueEventListener;
    public static DatabaseReference ref;
    public static FirebaseStorage storage;

    private ImageChange imageChange = new ImageChange();

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        ViewPager eventPager = findViewById(R.id.pagerview_event);

        if(eventPager != null) {
            if(childfragmentsinstack.size() > 0 && eventPager.getCurrentItem() != 0) {
                if(childfragmentsinstack.size() > 1) {
                    if(childfragmentsinstack.get(childfragmentsinstack.size() - 2).equals(alle_eventer_fragment.class.getName()))
                        eventPager.setCurrentItem(0, true);
                    else if(childfragmentsinstack.get(childfragmentsinstack.size() - 2).equals(mine_eventer_fragment.class.getName()))
                        eventPager.setCurrentItem(1, true);
                    else
                        eventPager.setCurrentItem(2, true);
                } else {
                    eventPager.setCurrentItem(0, true);
                }

                if(childfragmentsinstack.size() > 1)
                    childfragmentsinstack.remove(childfragmentsinstack.size() - 1);
                if(childfragmentsinstack.size() == 0)
                    childfragmentsinstack.add(alle_eventer_fragment.class.getName());
                return;
            }
        } else {
            ViewPager chatPager = findViewById(R.id.viewpager_chat);

            if(chatPager != null) {
                if(childfragmentsinstackChat.size() > 0 && chatPager.getCurrentItem() != 0) {
                    if(childfragmentsinstackChat.size() > 1) {
                        if(childfragmentsinstackChat.get(childfragmentsinstackChat.size() - 2).equals(mine_chats_fragment.class.getName()))
                            chatPager.setCurrentItem(0, true);
                        else if(childfragmentsinstackChat.get(childfragmentsinstackChat.size() - 2).equals(venner_fragment.class.getName()))
                            chatPager.setCurrentItem(1, true);
                        else
                            chatPager.setCurrentItem(2, true);
                    } else {
                        chatPager.setCurrentItem(0, true);
                    }

                    if(childfragmentsinstackChat.size() > 1)
                        childfragmentsinstackChat.remove(childfragmentsinstackChat.size() - 1);
                    if(childfragmentsinstackChat.size() == 0)
                        childfragmentsinstackChat.add(alle_eventer_fragment.class.getName());
                    return;
                }
            }
        }

        if (count <= 1) {
            finish();
            System.exit(0);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    CountDownTimer ctd = new CountDownTimer(Long.MAX_VALUE, 1000) {
        public void onTick(long millisUntilFinished) {
            if(!Utilities.hasNetwork(getApplicationContext())) {
                Bruker.get().setConnected(false);
            } else if(Utilities.hasNetwork(getApplicationContext())) {
                Bruker.get().setConnected(true);
            }
        }

        public void onFinish() {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_profil));

        childfragmentsinstack = new ArrayList<>();
        childfragmentsinstackChat = new ArrayList<>();
        childfragmentsinstack.add(alle_eventer_fragment.class.getName());

        ctd.start();

        storage = FirebaseStorage.getInstance();

        ref = FirebaseDatabase.getInstance().getReference().child("messagepreviews");

        valueEventListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChatPreview> list = new ArrayList<>();

                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.getKey() == null)
                        continue;

                    if(child.getKey().contains(Bruker.get().getBrukernavn())) {
                        list.add(child.getValue(ChatPreview.class));
                    }
                }

                ListView lv_chat = findViewById(R.id.lv_chat);

                if(lv_chat != null)
                    lv_chat.setAdapter(new ChatPreviewAdapter(ProfilActivity.this, list));

                Bruker.get().setChatMessageList(list);
                Bruker.get().LagreBruker();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(databaseError.getCode() == DatabaseError.NETWORK_ERROR)
                    Toast.makeText(ProfilActivity.this, "Could not find chats.", Toast.LENGTH_SHORT).show();
            }
        });

        if(!Utilities.hasNetwork(getApplicationContext())) {
            Bruker.get().setConnected(false);
            Toast.makeText(this, "You are not connected.", Toast.LENGTH_SHORT).show();
        } else if(Utilities.hasNetwork(getApplicationContext())) {
            if(Bruker.get().isLoggetpa()) {
                Utilities.getPosition(this);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utilities.getPosition(ProfilActivity.this);
                    }
                }, 3000);
            }

            Bruker.get().setConnected(true);
        }

        TextView tittel = findViewById(R.id.title_toolbar);

        ConstraintLayout main_content = findViewById(R.id.main_content);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        main_content.setBackgroundColor(getResources().getColor(R.color.colorAlpha)); // new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.forsidebilde), size.x, size.y, true))

        tittel.setTypeface(typeface);

        if(Bruker.get().isConnected())
            replaceFragment(0);

        setTitle("");
    }

    @Override
    protected void onPause() {
        ctd.cancel();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(!Utilities.hasNetwork(this)) {
            Toast.makeText(this, getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
            Bruker.get().setConnected(false);
        } else {
            Bruker.get().setConnected(true);
        }

        Utilities.getPosition(this);

        ctd.start();
        super.onResume();
    }

    private void replaceFragment(int fragmentNum) {
        if(!Bruker.get().isConnected()) {
            Toast.makeText(this, "You are not connected.", Toast.LENGTH_SHORT).show();
            return;
        }

        Fragment fragment;
        switch (fragmentNum) {
            case 0:
                fragment = new eventfragment();
                break;
            case 1:
                fragment = new bilfragment();
                break;
            case 2:
                fragment = new profilfragment();
                break;
            case 3:
                fragment = new chatfragment();
                break;
            default:
                fragment = new eventfragment();
                break;
        }

        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null){ // fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_content, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void onBilMenyClick(View v) {
        replaceFragment(1);
        ((TextView) findViewById(R.id.title_toolbar)).setText("Ride");
        ImageButton search_events = findViewById(R.id.search_events);
        search_events.setImageDrawable(getResources().getDrawable(R.drawable.search));

        search_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void onKalenderMenyClick(View v) {
        replaceFragment(0);
        ((TextView) findViewById(R.id.title_toolbar)).setText(getResources().getString(R.string.arrangementer));
        findViewById(R.id.search_events).setVisibility(View.VISIBLE);
        ImageButton search_events = findViewById(R.id.search_events);
        search_events.setImageDrawable(getResources().getDrawable(R.drawable.search));

        mine_eventer_fragment.once = false;

        findViewById(R.id.add_event).setVisibility(View.VISIBLE);
    }

    public void onChatMenyClick(View v) {
        replaceFragment(3);
        ImageButton search_chat = findViewById(R.id.search_events);

        if(search_chat == null)
            return;

        venner_fragment.once = false;

        search_chat.setImageDrawable(getResources().getDrawable(R.drawable.search));

        ((TextView) findViewById(R.id.title_toolbar)).setText("Messages"); // TODO : Translation
    }

    public void onProfilMenyClick(View v) {
        replaceFragment(2);
        ((TextView) findViewById(R.id.title_toolbar)).setText(Bruker.get().getBrukernavn());

        findViewById(R.id.add_event).setVisibility(View.INVISIBLE);
    }

    public void onAlleEventerClick(View v) {
        AppCompatButton mine_eventer_btn = findViewById(R.id.mine_eventer_btn);
        AppCompatButton mitt_arkiv_btn = findViewById(R.id.arkiv_btn);
        ViewPager viewPager = findViewById(R.id.pagerview_event);

        if(mine_eventer_btn == null || mitt_arkiv_btn == null || viewPager == null)
            return;

        ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgrey));
        ViewCompat.setBackgroundTintList(mine_eventer_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));
        ViewCompat.setBackgroundTintList(mitt_arkiv_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));

        viewPager.setCurrentItem(0, true);
        }

    public void onMineEventerClick(View v) {
        AppCompatButton alle_eventer_btn = findViewById(R.id.alle_eventer_btn);
        AppCompatButton mitt_arkiv_btn = findViewById(R.id.arkiv_btn);
        ViewPager viewPager = findViewById(R.id.pagerview_event);

        if(alle_eventer_btn == null || mitt_arkiv_btn == null || viewPager == null)
            return;

        ViewCompat.setBackgroundTintList(alle_eventer_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));
        ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgrey));
        ViewCompat.setBackgroundTintList(mitt_arkiv_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));

        viewPager.setCurrentItem(1, true);
    }

    public void onMittArkivClick(View v) {
        AppCompatButton alle_eventer_btn = findViewById(R.id.alle_eventer_btn);
        AppCompatButton mine_eventer_btn = findViewById(R.id.mine_eventer_btn);
        ViewPager viewPager = findViewById(R.id.pagerview_event);

        if(alle_eventer_btn == null || mine_eventer_btn == null || viewPager == null)
            return;

        ViewCompat.setBackgroundTintList(alle_eventer_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));
        ViewCompat.setBackgroundTintList(mine_eventer_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));
        ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgrey));

        viewPager.setCurrentItem(2, true);
    }

    public void onFinnBilClick(View v) {
        AppCompatButton min_bil_btn = findViewById(R.id.min_bil_btn);
        ViewPager viewPager = findViewById(R.id.pagerview_bil);

        if(min_bil_btn == null || viewPager == null)
            return;

        ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgrey));
        ViewCompat.setBackgroundTintList(min_bil_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));

        viewPager.setCurrentItem(0, true);
    }

    public void onMinBilClick(View v) {
        AppCompatButton finn_bil_btn = findViewById(R.id.finn_bil_btn);
        ViewPager viewPager = findViewById(R.id.pagerview_bil);

        if(finn_bil_btn == null || viewPager == null)
            return;

        ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgrey));
        ViewCompat.setBackgroundTintList(finn_bil_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));

        viewPager.setCurrentItem(1, true);
    }

    public void onMineChatsClick(View v) {
        AppCompatButton venner_btn = findViewById(R.id.venner);
        ViewPager viewPager = findViewById(R.id.viewpager_chat);

        if(venner_btn == null || viewPager == null)
            return;

        ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgrey));
        ViewCompat.setBackgroundTintList(venner_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));

        viewPager.setCurrentItem(0, true);
    }

    public void onVennerClick(View v) {
        AppCompatButton mine_chats = findViewById(R.id.mine_chats);
        ViewPager viewPager = findViewById(R.id.viewpager_chat);

        if(mine_chats == null || viewPager == null)
            return;

        ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgrey));
        ViewCompat.setBackgroundTintList(mine_chats, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));

        viewPager.setCurrentItem(1, true);
    }

    public void onLeggTilEventClick(View v) {
        final Dialog dialog = new Dialog(this);

        dialog.setOwnerActivity(this);

        bool = false;

        dialog.requestWindowFeature(1);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(R.layout.legg_til_event);

        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if(dialog.getWindow() != null) {
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        }

        final Button button = dialog.findViewById(R.id.create_event_btn1);
        final TextView til_label = dialog.findViewById(R.id.textView12);
        final EditText dato = dialog.findViewById(R.id.datofratext);
        final EditText time = dialog.findViewById(R.id.timefratext);
        final EditText datotil = dialog.findViewById(R.id.datotiltext);
        final EditText timetil = dialog.findViewById(R.id.timetiltext);
        final CheckBox alle_deltakere = dialog.findViewById(R.id.alle_deltakere_må);
        final TextView fjern_sluttidspunkt = dialog.findViewById(R.id.fjern_sluttidspunkt);
        fjern_sluttidspunkt.setPaintFlags(fjern_sluttidspunkt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final CheckBox vis_adresse = dialog.findViewById(R.id.vis_adresse);
        final ImageButton legg_til_bilde = dialog.findViewById(R.id.imageButton6);
        final EditText maks_deltakere = dialog.findViewById(R.id.maks_deltakere);
        final EditText titletext = dialog.findViewById(R.id.create_eventText);
        final EditText beskrivelse = dialog.findViewById(R.id.beskrivelse_create);
        final EditText aldersgrense = dialog.findViewById(R.id.aldersgrense);
        final TextView sluttidspunkt = dialog.findViewById(R.id.legg_til_sluttidspunkt);
        sluttidspunkt.setPaintFlags(sluttidspunkt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Toolbar create_event_toolbar = dialog.findViewById(R.id.toolbar2);
        final EditText postnr = dialog.findViewById(R.id.create_postnr);
        final EditText gate = dialog.findViewById(R.id.create_gate);

        final Handler textchangedHandler = new Handler();

        postnr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                textchangedHandler.removeCallbacksAndMessages(null);
                textchangedHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(postnr.getText().toString().length() > 3 && gate.getText().toString().length() > 4) {
                            GetLocationInfo getLocationInfo = new GetLocationInfo(dialog, gate.getText().toString(), Integer.valueOf(s.toString()), new Event(""),null, false);
                            getLocationInfo.execute();
                        }
                    }
                }, 200);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        gate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                textchangedHandler.removeCallbacksAndMessages(null);
                textchangedHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(postnr.getText().toString().length() > 3 && gate.getText().toString().length() > 4) {
                            GetLocationInfo getLocationInfo = new GetLocationInfo(dialog, s.toString(), Integer.valueOf(postnr.getText().toString()), new Event(""), null, false);
                            getLocationInfo.execute();
                        }
                    }
                }, 200);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        create_event_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.onBackPressed();
            }
        });

        create_event_toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));

        legg_til_bilde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(ProfilActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), Utilities.SELECT_IMAGE_CODE);
                } else {
                    ActivityCompat.requestPermissions(ProfilActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Utilities.READ_EXTERNAL_STORAGE_CODE);
                }
            }
        });

        imageChange.addChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    legg_til_bilde.setImageBitmap(Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), imageChange.getUri()), (int) getResources().getDimension(R.dimen._150sdp), (int) getResources().getDimension(R.dimen._75sdp), true));
                    legg_til_bilde.setScaleX(1.0f);
                    legg_til_bilde.setScaleY(1.0f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        alle_deltakere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vis_adresse.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        //noinspection AndroidLintClickableViewAccessibility
        time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar nowtime = Calendar.getInstance();
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ProfilActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            time.setText(String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minute));
                        }
                    }, nowtime.get(Calendar.HOUR_OF_DAY), nowtime.get(Calendar.MINUTE), true);
                    timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, ProfilActivity.this.getResources().getString(R.string.angi), timePickerDialog);
                    timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, ProfilActivity.this.getResources().getString(R.string.avbryt), timePickerDialog);
                    timePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        //noinspection AndroidLintClickableViewAccessibility
        dato.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar c = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(ProfilActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Calendar calendar;
                            calendar = Calendar.getInstance();
                            calendar.set(Calendar.MONTH, month);
                            dato.setText(String.format(Locale.ENGLISH, "%02d %s %d", dayOfMonth, calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                                    ProfilActivity.this.getResources().getConfiguration().locale), year));
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, ProfilActivity.this.getResources().getString(R.string.angi), dialog);
                    dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, ProfilActivity.this.getResources().getString(R.string.avbryt), dialog);
                    dialog.show();
                    return true;
                }

                return false;
            }
        });

        //noinspection AndroidLintClickableViewAccessibility
        timetil.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar nowtime = Calendar.getInstance();
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ProfilActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            timetil.setText(String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minute));
                        }
                    }, nowtime.get(Calendar.HOUR_OF_DAY), nowtime.get(Calendar.MINUTE), true);
                    timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, ProfilActivity.this.getResources().getString(R.string.angi), timePickerDialog);
                    timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, ProfilActivity.this.getResources().getString(R.string.avbryt), timePickerDialog);
                    timePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        //noinspection AndroidLintClickableViewAccessibility
        datotil.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    v.requestFocusFromTouch();

                    Calendar c = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(ProfilActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Calendar calendar;
                            calendar = Calendar.getInstance();
                            calendar.set(Calendar.MONTH, month);
                            datotil.setText(String.format(Locale.ENGLISH, "%02d %s %d", dayOfMonth, calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                                    ProfilActivity.this.getResources().getConfiguration().locale), year));
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, ProfilActivity.this.getResources().getString(R.string.angi), dialog);
                    dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, ProfilActivity.this.getResources().getString(R.string.avbryt), dialog);
                    dialog.show();
                    return true;
                }

                return false;
            }
        });

        sluttidspunkt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                til_label.setVisibility(View.VISIBLE);
                datotil.setVisibility(View.VISIBLE);
                fjern_sluttidspunkt.setVisibility(View.VISIBLE);
                sluttidspunkt.setVisibility(View.INVISIBLE);
                timetil.setVisibility(View.VISIBLE);
            }
        });

        fjern_sluttidspunkt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                til_label.setVisibility(View.INVISIBLE);
                datotil.setVisibility(View.INVISIBLE);
                fjern_sluttidspunkt.setVisibility(View.GONE);
                sluttidspunkt.setVisibility(View.VISIBLE);
                timetil.setVisibility(View.INVISIBLE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CheckBox vis_gjesteliste = dialog.findViewById(R.id.vis_gjesteliste);
                final TextView by = dialog.findViewById(R.id.by_textview);

                if(titletext.length() <= 3) {
                    Toast.makeText(ProfilActivity.this, "Please choose a title longer than 3 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(dato.getText().toString().isEmpty() || time.getText().toString().isEmpty()) {
                    Toast.makeText(ProfilActivity.this, "Please choose a starting date.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(maks_deltakere.getText().toString().isEmpty() || Integer.valueOf(maks_deltakere.getText().toString()) <= 1) {
                    Toast.makeText(ProfilActivity.this, "Max participants can not be lower than 2.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!maks_deltakere.getText().toString().isEmpty() && (Integer.valueOf(maks_deltakere.getText().toString()) > 40 && !Bruker.get().isPremium())) {
                    new AlertDialog.Builder(ProfilActivity.this)
                            .setTitle("Premium")
                            .setMessage("To use this feature you need to have premium.\nWould you like to purchase premium? (This will not prompt you with a purchase, rather with information.)")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // show info about premium and it's features.
                                }})
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}})
                            .show();
                }

                if(aldersgrense.getText().toString().isEmpty() || Integer.valueOf(aldersgrense.getText().toString()) <= 13) {
                    Toast.makeText(ProfilActivity.this, "The agerestriction can not be lower than 14.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(by.getText().toString().equals("By")) {
                    Toast.makeText(ProfilActivity.this, "Please make sure the address is filled in correctly.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(imageChange.getImage() == null) {
                    new AlertDialog.Builder(ProfilActivity.this)
                            .setTitle("Image")
                            .setMessage("Are you sure you do not want to add an image?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface somedialog, int which) {
                                    bool = true;
                                    button.callOnClick();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }})
                            .show();
                }

                if(!datotil.getText().toString().isEmpty() && !timetil.getText().toString().isEmpty() && bool) {
                    GregorianCalendar datefrom, dateto;

                    if(dato.getText().toString().contains(".")) {
                        datefrom = Utilities.getDateFromString(String.format(Locale.ENGLISH, "%s %s", dato.getText().toString(), time.getText().toString()), "dd MMM. yyyy HH:mm");
                    } else {
                        datefrom = Utilities.getDateFromString(String.format(Locale.ENGLISH, "%s %s", dato.getText().toString(), time.getText().toString()), "dd MMM yyyy HH:mm");
                    }

                    if(datotil.getText().toString().contains(".")) {
                        dateto = Utilities.getDateFromString(String.format(Locale.ENGLISH, "%s %s", datotil.getText().toString(), timetil.getText().toString()), "dd MMM. yyyy HH:mm");
                    } else {
                        dateto = Utilities.getDateFromString(String.format(Locale.ENGLISH, "%s %s", datotil.getText().toString(), timetil.getText().toString()), "dd MMM yyyy HH:mm");
                    }

                    if(dateto != null && datefrom != null && !dateto.before(datefrom)) {

                        Event creating_event = new Event(0, titletext.getText().toString(), gate.getText().toString(), "", Bruker.get().getBrukernavn(),
                                alle_deltakere.isChecked(),0.0, 0.0, datefrom.getTimeInMillis(), dateto.getTimeInMillis(), Integer.valueOf(aldersgrense.getText().toString()),
                                new ArrayList<>(Collections.singletonList(Participant.convertBrukerParticipant(Bruker.get(), EventStilling.VERT))), Integer.valueOf(maks_deltakere.getText().toString()),
                                postnr.getText().toString(), by.getText().toString(), beskrivelse.getText().toString(), vis_gjesteliste.isChecked(), vis_adresse.isChecked(), new ArrayList<Requester>(),
                                false);

                        GetLocationInfo getLocationInfo = new GetLocationInfo(dialog, gate.getText().toString(), Integer.valueOf(postnr.getText().toString()), creating_event,
                                imageChange.getImage(), true);
                        getLocationInfo.execute();
                        imageChange = new ImageChange();
                    }
                } else if(datotil.getText().toString().isEmpty() && timetil.getText().toString().isEmpty()) {
                    GregorianCalendar datefrom;

                    if(dato.getText().toString().contains(".")) {
                        datefrom = Utilities.getDateFromString(String.format(Locale.ENGLISH, "%s %s", dato.getText().toString(), time.getText().toString()), "dd MMM. yyyy HH:mm");
                    } else {
                        datefrom = Utilities.getDateFromString(String.format(Locale.ENGLISH, "%s %s", dato.getText().toString(), time.getText().toString()), "dd MMM yyyy HH:mm");
                    }

                    if(datefrom != null) {
                        Event creating_event = new Event(0, titletext.getText().toString(), gate.getText().toString(), "", Bruker.get().getBrukernavn(),
                                alle_deltakere.isChecked(),0.0, 0.0, datefrom.getTimeInMillis(), 0, Integer.valueOf(aldersgrense.getText().toString()),
                                new ArrayList<>(Collections.singletonList(Participant.convertBrukerParticipant(Bruker.get(), EventStilling.VERT))), Integer.valueOf(maks_deltakere.getText().toString()),
                                postnr.getText().toString(), by.getText().toString(), beskrivelse.getText().toString(), vis_gjesteliste.isChecked(), vis_adresse.isChecked(), new ArrayList<Requester>(),
                                false);

                        GetLocationInfo getLocationInfo = new GetLocationInfo(dialog, gate.getText().toString(), Integer.valueOf(postnr.getText().toString()), creating_event,
                                imageChange.getImage(), true);
                        getLocationInfo.execute();
                        imageChange = new ImageChange();
                    }
                }
            }
        });

        dialog.show();

        if(dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(lp);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Utilities.READ_EXTERNAL_STORAGE_CODE) {
            for(int i = 0; i < permissions.length; i++) {
                if(permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Utilities.SELECT_IMAGE_CODE);
                    }
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Utilities.SELECT_IMAGE_CODE && resultCode == RESULT_OK && data.getData() != null) {
            Uri image = data.getData();

            imageChange.setUri(image);
            String str = Utilities.getPathFromUri(this, image);
            if(str != null)
                imageChange.setImage(new File(str));
        }
    }
}
