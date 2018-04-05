package com.partyspottr.appdir.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.ImageChange;
import com.partyspottr.appdir.classes.Participant;
import com.partyspottr.appdir.classes.Requester;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.networking.GetLocationInfo;
import com.partyspottr.appdir.enums.EventStilling;
import com.partyspottr.appdir.ui.mainfragments.bilfragment;
import com.partyspottr.appdir.ui.mainfragments.chatfragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.alle_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventchildfragments.mine_eventer_fragment;
import com.partyspottr.appdir.ui.mainfragments.eventfragment;
import com.partyspottr.appdir.ui.mainfragments.profilfragment;
import com.partyspottr.appdir.ui.other_ui.SettingActivity;

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
 * Created by Ranarrr on Unknown date.
 *
 * @author Ranarrr
 */

public class ProfilActivity extends AppCompatActivity {

    private AppCompatButton alle_eventer_btn;
    private AppCompatButton mine_eventer_btn;
    private AppCompatButton mitt_arkiv_btn;

    public static List<String> childfragmentsinstack;

    private ImageChange imageChange = new ImageChange();

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        ViewPager viewPager = findViewById(R.id.pagerview_event);

        if(childfragmentsinstack.size() > 0 && (viewPager != null && viewPager.getCurrentItem() != 0)) {
            if(childfragmentsinstack.size() > 1) {
                if(childfragmentsinstack.get(childfragmentsinstack.size() - 2).equals(alle_eventer_fragment.class.getName()))
                    viewPager.setCurrentItem(0, true);
                else if(childfragmentsinstack.get(childfragmentsinstack.size() - 2).equals(mine_eventer_fragment.class.getName()))
                    viewPager.setCurrentItem(1, true);
                else
                    viewPager.setCurrentItem(2, true);
            } else {
                viewPager.setCurrentItem(0, true);
            }

            if(childfragmentsinstack.size() > 1)
                childfragmentsinstack.remove(childfragmentsinstack.size() - 1);
            if(childfragmentsinstack.size() == 0)
                childfragmentsinstack.add(alle_eventer_fragment.class.getName());
            return;
        }

        if (count <= 1) {
            finish();
            System.exit(0);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    CountDownTimer ctd = new CountDownTimer(Long.MAX_VALUE, 200) {
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

        /*if(!Bruker.get().isLoggetpa()) {
            Toast.makeText(this, "Fatal error!", Toast.LENGTH_SHORT).show();
            System.exit(0);
        }*/

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_profil));

        childfragmentsinstack = new ArrayList<>();
        childfragmentsinstack.add(alle_eventer_fragment.class.getName());

        ctd.start();

        if(!Utilities.hasNetwork(getApplicationContext())) {
            Bruker.get().setConnected(false);
            Toast.makeText(this, "You are not connected.", Toast.LENGTH_SHORT).show();
        } else if(Utilities.hasNetwork(getApplicationContext())) {
            Bruker.get().setConnected(true);
        }

        TextView tittel = findViewById(R.id.title_toolbar);

        ConstraintLayout main_content = findViewById(R.id.main_content);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        main_content.setBackground(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.forsidebilde), size.x, size.y, true)));

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

        ctd.start();
        super.onResume();
    }

    private void replaceFragment(int fragmentNum) {
        if(!Bruker.get().isConnected())
            return;

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
        ((TextView) findViewById(R.id.title_toolbar)).setText(getResources().getString(R.string.sjåfør));
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

        search_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.onSearchEventsClickAlle(ProfilActivity.this);
            }
        });

        findViewById(R.id.add_event).setVisibility(View.VISIBLE);
    }

    public void onChatMenyClick(View v) {
        replaceFragment(3);
        ((TextView) findViewById(R.id.title_toolbar)).setText("Messages"); // TODO : Translation
    }

    public void onProfilMenyClick(View v) {
        replaceFragment(2);
        ((TextView) findViewById(R.id.title_toolbar)).setText(Bruker.get().getBrukernavn());
        ImageButton now_cog = findViewById(R.id.search_events);
        now_cog.setImageDrawable(getResources().getDrawable(R.drawable.cog));

        now_cog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.add_event).setVisibility(View.INVISIBLE);
    }

    public void onAlleEventerClick(View v) {
        mine_eventer_btn = findViewById(R.id.mine_eventer_btn);
        mitt_arkiv_btn = findViewById(R.id.arkiv_btn);

        ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgrey));
        ViewCompat.setBackgroundTintList(mine_eventer_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));
        ViewCompat.setBackgroundTintList(mitt_arkiv_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));
        ViewPager viewPager = findViewById(R.id.pagerview_event);
        viewPager.setCurrentItem(0, true);
        }

    public void onMineEventerClick(View v) {
        alle_eventer_btn = findViewById(R.id.alle_eventer_btn);
        mitt_arkiv_btn = findViewById(R.id.arkiv_btn);

        ViewCompat.setBackgroundTintList(alle_eventer_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));
        ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgrey));
        ViewCompat.setBackgroundTintList(mitt_arkiv_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));
        ViewPager viewPager = findViewById(R.id.pagerview_event);
        viewPager.setCurrentItem(1, true);
    }

    public void onMittArkivClick(View v) {
        alle_eventer_btn = findViewById(R.id.alle_eventer_btn);
        mine_eventer_btn = findViewById(R.id.mine_eventer_btn);

        ViewCompat.setBackgroundTintList(alle_eventer_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));
        ViewCompat.setBackgroundTintList(mine_eventer_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));
        ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgrey));
        ViewPager viewPager = findViewById(R.id.pagerview_event);
        viewPager.setCurrentItem(2, true);
    }

    public void onFinnBilClick(View v) {
        AppCompatButton min_bil_btn = findViewById(R.id.min_bil_btn);

        ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgrey));
        ViewCompat.setBackgroundTintList(min_bil_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));
        ViewPager viewPager = findViewById(R.id.pagerview_bil);
        viewPager.setCurrentItem(0, true);
    }

    public void onMinBilClick(View v) {
        AppCompatButton finn_bil_btn = findViewById(R.id.finn_bil_btn);

        ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightgrey));
        ViewCompat.setBackgroundTintList(finn_bil_btn, ContextCompat.getColorStateList(getApplicationContext(), R.color.lightlightgrey));
        ViewPager viewPager = findViewById(R.id.pagerview_bil);
        viewPager.setCurrentItem(1, true);
    }

    public void onLeggTilEventClick(View v) {
        final Dialog dialog = new Dialog(this);

        dialog.setOwnerActivity(this);

        dialog.requestWindowFeature(1);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(R.layout.legg_til_event);

        Button button = dialog.findViewById(R.id.create_event_btn1);
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
                        GetLocationInfo getLocationInfo = new GetLocationInfo(dialog, gate.getText().toString(), Integer.valueOf(s.toString().isEmpty() ? "0" : s.toString()), new Event(""),
                                null, false);
                        getLocationInfo.execute();
                    }
                }, 1000);
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
                        GetLocationInfo getLocationInfo = new GetLocationInfo(dialog, s.toString(),
                                Integer.valueOf(postnr.getText().toString().isEmpty() ? "0" : postnr.getText().toString()), new Event(""), null, false);
                        getLocationInfo.execute();
                    }
                }, 1000);
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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1001);
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
                            dato.setText(String.format(Locale.ENGLISH, "%02d. %s %d", dayOfMonth, calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
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
                    Calendar c = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(ProfilActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Calendar calendar;
                            calendar = Calendar.getInstance();
                            calendar.set(Calendar.MONTH, month);
                            datotil.setText(String.format(Locale.ENGLISH, "%02d. %s %d", dayOfMonth, calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
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
                EditText titletext = dialog.findViewById(R.id.create_eventText);
                EditText beskrivelse = dialog.findViewById(R.id.beskrivelse_create);
                CheckBox vis_gjesteliste = dialog.findViewById(R.id.vis_gjesteliste);
                EditText maks_deltakere = dialog.findViewById(R.id.maks_deltakere);
                EditText aldersgrense = dialog.findViewById(R.id.aldersgrense);

                if(titletext.length() >= 4 && !dato.getText().toString().isEmpty() && !time.getText().toString().isEmpty()) {
                    if(!datotil.getText().toString().isEmpty() && !timetil.getText().toString().isEmpty()) {
                        GregorianCalendar datefrom, dateto;

                        dateto = Utilities.getDateFromString(String.format(Locale.ENGLISH, "%s %s", datotil.getText().toString(), timetil.getText().toString()), "dd. MMMM. yyyy HH:mm");
                        datefrom = Utilities.getDateFromString(String.format(Locale.ENGLISH, "%s %s", dato.getText().toString(), time.getText().toString()), "dd. MMMM. yyyy HH:mm");

                        if(dateto != null && datefrom != null && !dateto.before(datefrom)) {

                            Event creating_event = new Event(0, titletext.getText().toString(), gate.getText().toString(), Bruker.get().getCountry(), Bruker.get().getBrukernavn(),
                                    alle_deltakere.isChecked(),0.0, 0.0, datefrom, dateto, Integer.valueOf(aldersgrense.getText().toString()),
                                    new ArrayList<>(Collections.singletonList(Participant.convertBrukerParticipant(Bruker.get(), EventStilling.VERT))), Integer.valueOf(maks_deltakere.getText().toString()),
                                    Integer.valueOf(postnr.getText().toString()), "", beskrivelse.getText().toString(), vis_gjesteliste.isChecked(), vis_adresse.isChecked(), new ArrayList<Requester>(),
                                    false);

                            GetLocationInfo getLocationInfo = new GetLocationInfo(dialog, gate.getText().toString(), Integer.valueOf(postnr.getText().toString()), creating_event,
                                    imageChange.getImage(), true);
                            getLocationInfo.execute();
                        }
                    } else if(datotil.getText().toString().isEmpty() && timetil.getText().toString().isEmpty()) {
                        GregorianCalendar datefrom;

                        datefrom = Utilities.getDateFromString(String.format(Locale.ENGLISH, "%s %s", dato.getText().toString(), time.getText().toString()), "dd. MMMM. yyyy HH:mm");

                        if(datefrom != null) {
                            Event creating_event = new Event(0, titletext.getText().toString(), gate.getText().toString(), Bruker.get().getCountry(), Bruker.get().getBrukernavn(),
                                    alle_deltakere.isChecked(),0.0, 0.0, datefrom, null, Integer.valueOf(aldersgrense.getText().toString()),
                                    new ArrayList<>(Collections.singletonList(Participant.convertBrukerParticipant(Bruker.get(), EventStilling.VERT))), Integer.valueOf(maks_deltakere.getText().toString()),
                                    Integer.valueOf(postnr.getText().toString()), "", beskrivelse.getText().toString(), vis_gjesteliste.isChecked(), vis_adresse.isChecked(), new ArrayList<Requester>(),
                                    false);

                            GetLocationInfo getLocationInfo = new GetLocationInfo(dialog, gate.getText().toString(), Integer.valueOf(postnr.getText().toString()), creating_event,
                                    imageChange.getImage(), true);
                            getLocationInfo.execute();
                        }
                    }
                }
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1001 && resultCode == RESULT_OK && data.getData() != null) {
            Uri image = data.getData();

            imageChange.setUri(image);
            imageChange.setImage(new File(image.getPath()));
        }
    }
}
