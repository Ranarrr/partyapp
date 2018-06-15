package com.partyspottr.appdir.ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.ImageChange;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.networking.GetLocationInfo;
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

    public static ValueEventListener valueEventListener;
    public static DatabaseReference ref;
    public static FirebaseStorage storage;

    public static ImageChange imageChange = new ImageChange();

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

        Bruker.get().GetAndParseEvents(this);
        Bruker.get().GetAndParseBrukerInfo();
        Bruker.get().GetAndParseChauffeurs();
        if(Bruker.get().isHascar())
            Bruker.get().GetAndParseBrukerChauffeur();

        ctd.start();

        storage = FirebaseStorage.getInstance();

        Utilities.startChatListener(this);

        if(!Utilities.hasNetwork(getApplicationContext())) {
            Bruker.get().setConnected(false);
            Toast.makeText(this, "You are not connected.", Toast.LENGTH_SHORT).show();
        } else if(Utilities.hasNetwork(getApplicationContext())) {
            if(Bruker.get().isLoggetpa()) {
                Utilities.getPosition(this);
            }

            Bruker.get().setConnected(true);
        }

        TextView tittel = findViewById(R.id.title_toolbar);

        ConstraintLayout main_content = findViewById(R.id.main_content);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        main_content.setBackgroundColor(getResources().getColor(R.color.colorAlpha)); // new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.forsidebilde), size.x, size.y, true))

        tittel.setTypeface(typeface);

        replaceFragment(0);

        setTitle("");
    }

    @Override
    protected void onPause() {
        ctd.cancel();
        super.onPause();
    }

    @Override
    protected void onStop() {
        ctd.cancel();

        Bruker.get().StopParsingBrukerInfo();
        Bruker.get().StopParsingEvents();
        Bruker.get().StopParsingChauffeurs();
        if(Bruker.get().isHascar())
            Bruker.get().StopParsingBrukerChauffeur();

        if(ProfilActivity.valueEventListener != null && ProfilActivity.ref != null)
            ProfilActivity.ref.removeEventListener(ProfilActivity.valueEventListener);

        if(SplashActivity.mAuth.getCurrentUser() != null)
            SplashActivity.mAuth.signOut();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(Bruker.get().getBrukernavn());
        ref.child("loggedon").setValue(false);
        Bruker.get().setLoggetpa(false);

        super.onStop();
    }

    @Override
    protected void onRestart() {
        if(!Bruker.get().isConnected()) {
            super.onStart();
            return;
        }

        if(SplashActivity.mAuth.getCurrentUser() == null && Bruker.get().getEmail() != null && !Bruker.get().getEmail().isEmpty()
                && Bruker.get().getPassord() != null && !Bruker.get().getPassord().isEmpty())
            SplashActivity.mAuth.signInWithEmailAndPassword(Bruker.get().getEmail(), Bruker.get().getPassord())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Bruker.get().GetAndParseEvents(ProfilActivity.this);
                                Bruker.get().GetAndParseBrukerInfo();
                                Bruker.get().GetAndParseChauffeurs();
                                if(Bruker.get().isHascar())
                                    Bruker.get().GetAndParseBrukerChauffeur();

                                Utilities.startChatListener(ProfilActivity.this);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(Bruker.get().getBrukernavn());
                                ref.child("loggedon").setValue(true);
                                Bruker.get().setLoggetpa(true);

                                replaceFragment(0);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfilActivity.this, "Failed to log you in, please try again.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

        super.onRestart();
    }

    @Override
    protected void onResume() {
        if(!Utilities.hasNetwork(this)) {
            Toast.makeText(this, getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
            Bruker.get().setConnected(false);
        } else {
            if(Bruker.get().isLoggetpa())
                Utilities.getPosition(this);

            Bruker.get().setConnected(true);
        }

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

        findViewById(R.id.search_events).setVisibility(View.INVISIBLE);
        findViewById(R.id.add_event).setVisibility(View.INVISIBLE);
    }

    public void onAlleEventerClick(View v) {
        AppCompatButton mine_eventer_btn = findViewById(R.id.mine_eventer_btn);
        AppCompatButton mitt_arkiv_btn = findViewById(R.id.arkiv_btn);
        ViewPager viewPager = findViewById(R.id.pagerview_event);
        ImageView arrow_eventfragment = findViewById(R.id.arrow_eventfragment);

        if(mine_eventer_btn == null || mitt_arkiv_btn == null || viewPager == null || arrow_eventfragment == null)
            return;

        ObjectAnimator.ofFloat(arrow_eventfragment, "translationX", 0.0f).start();

        viewPager.setCurrentItem(0, true);
        }

    public void onMineEventerClick(View v) {
        AppCompatButton alle_eventer_btn = findViewById(R.id.alle_eventer_btn);
        AppCompatButton mitt_arkiv_btn = findViewById(R.id.arkiv_btn);
        ViewPager viewPager = findViewById(R.id.pagerview_event);
        ImageView arrow_eventfragment = findViewById(R.id.arrow_eventfragment);

        if(alle_eventer_btn == null || mitt_arkiv_btn == null || viewPager == null || arrow_eventfragment == null)
            return;

        ObjectAnimator.ofFloat(arrow_eventfragment, "translationX", ((v.getX() + (v.getX() + v.getWidth())) / 2) - ((alle_eventer_btn.getX() + (alle_eventer_btn.getX() + alle_eventer_btn.getWidth())) / 2)).start();

        viewPager.setCurrentItem(1, true);
    }

    public void onMittArkivClick(View v) {
        AppCompatButton alle_eventer_btn = findViewById(R.id.alle_eventer_btn);
        AppCompatButton mine_eventer_btn = findViewById(R.id.mine_eventer_btn);
        ViewPager viewPager = findViewById(R.id.pagerview_event);
        ImageView arrow_eventfragment = findViewById(R.id.arrow_eventfragment);

        if(alle_eventer_btn == null || mine_eventer_btn == null || viewPager == null || arrow_eventfragment == null)
            return;

        ObjectAnimator.ofFloat(arrow_eventfragment, "translationX", (((mine_eventer_btn.getX() + (mine_eventer_btn.getX() + mine_eventer_btn.getWidth())) / 2) - ((alle_eventer_btn.getX() + (alle_eventer_btn.getX() + alle_eventer_btn.getWidth())) / 2)) * 2.f).start();

        viewPager.setCurrentItem(2, true);
    }

    public void onFinnBilClick(View v) {
        AppCompatButton min_bil_btn = findViewById(R.id.min_bil_btn);
        ViewPager viewPager = findViewById(R.id.pagerview_bil);
        ImageView arrow_bilfragment = findViewById(R.id.arrow_bilfragment);

        if(min_bil_btn == null || viewPager == null || arrow_bilfragment == null)
            return;

        ObjectAnimator.ofFloat(arrow_bilfragment, "translationX", 0.0f).start();

        viewPager.setCurrentItem(0, true);
    }

    public void onMinBilClick(View v) {
        AppCompatButton finn_bil_btn = findViewById(R.id.finn_bil_btn);
        ViewPager viewPager = findViewById(R.id.pagerview_bil);
        ImageView arrow_bilfragment = findViewById(R.id.arrow_bilfragment);

        if(finn_bil_btn == null || viewPager == null || arrow_bilfragment == null)
            return;

        ObjectAnimator.ofFloat(arrow_bilfragment, "translationX", finn_bil_btn.getWidth()).start();

        viewPager.setCurrentItem(1, true);
    }

    public void onMineChatsClick(View v) {
        AppCompatButton venner_btn = findViewById(R.id.venner);
        ViewPager viewPager = findViewById(R.id.viewpager_chat);
        ImageView arrow_chatfragment = findViewById(R.id.arrow_chatfragment);

        if(venner_btn == null || viewPager == null || arrow_chatfragment == null)
            return;

        ObjectAnimator.ofFloat(arrow_chatfragment, "translationX", 0.0f);

        viewPager.setCurrentItem(0, true);
    }

    public void onVennerClick(View v) {
        AppCompatButton mine_chats = findViewById(R.id.mine_chats);
        ViewPager viewPager = findViewById(R.id.viewpager_chat);
        ImageView arrow_chatfragment = findViewById(R.id.arrow_chatfragment);

        if(mine_chats == null || viewPager == null || arrow_chatfragment == null)
            return;

        ObjectAnimator.ofFloat(arrow_chatfragment, "translationX", mine_chats.getWidth());

        viewPager.setCurrentItem(1, true);
    }

    public void onLeggTilEventClick(View v) {
        final Dialog dialog = new Dialog(this);

        dialog.setOwnerActivity(this);

        dialog.requestWindowFeature(1);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(R.layout.legg_til_event);

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
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ProfilActivity.this, R.style.mydatepickerdialog, new TimePickerDialog.OnTimeSetListener() {
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
                    DatePickerDialog dialog = new DatePickerDialog(ProfilActivity.this, R.style.mydatepickerdialog, new DatePickerDialog.OnDateSetListener() {
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
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ProfilActivity.this, R.style.mydatepickerdialog, new TimePickerDialog.OnTimeSetListener() {
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
                    DatePickerDialog dialog = new DatePickerDialog(ProfilActivity.this, R.style.mydatepickerdialog, new DatePickerDialog.OnDateSetListener() {
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
                datotil.setText("");
                timetil.setText("");
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
                    return;
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
                                    Utilities.CheckAddEvent(dato, time, datotil, timetil, titletext, gate, aldersgrense, maks_deltakere, postnr, by, beskrivelse, dialog, vis_gjesteliste, alle_deltakere, vis_adresse,
                                            false);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}})
                            .show();
                } else
                    Utilities.CheckAddEvent(dato, time, datotil, timetil, titletext, gate, aldersgrense, maks_deltakere, postnr, by, beskrivelse, dialog, vis_gjesteliste, alle_deltakere, vis_adresse, false);
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
