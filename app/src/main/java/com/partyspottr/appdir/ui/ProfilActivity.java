package com.partyspottr.appdir.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
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
import com.partyspottr.appdir.classes.networking.AddEvent;
import com.partyspottr.appdir.classes.networking.GetLocationInfo;
import com.partyspottr.appdir.classes.networking.LogoutUser;
import com.partyspottr.appdir.classes.networking.getUser;
import com.partyspottr.appdir.enums.EventStilling;
import com.partyspottr.appdir.ui.mainfragments.bilfragment;
import com.partyspottr.appdir.ui.mainfragments.eventfragment;
import com.partyspottr.appdir.ui.mainfragments.profilfragment;

import org.w3c.dom.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ranarrr on Unknown date.
 *
 * @author Ranarrr
 */

public class ProfilActivity extends AppCompatActivity {

    private AppCompatButton alle_eventer_btn;
    private AppCompatButton mine_eventer_btn;
    private AppCompatButton mitt_arkiv_btn;

    public static Typeface typeface;

    private ImageChange imageChange = new ImageChange();

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count <= 1) {
            super.onBackPressed();
            new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.logg_ut)).setMessage(getResources().getString(R.string.logg_ut_melding)).setPositiveButton(getResources().getString(R.string.ja), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(getApplicationContext() != null) {
                        LogoutUser logoutUser = new LogoutUser(ProfilActivity.this);
                        logoutUser.execute();
                    }
                }
            }).setNegativeButton(getResources().getString(R.string.nei), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(Bruker.get().isConnected())
                        replaceFragment(0);
                }
            }).show();
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

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_profil));

        ctd.start();

        TextView tittel = findViewById(R.id.title_toolbar);

        ConstraintLayout main_content = findViewById(R.id.main_content);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        main_content.setBackground(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.forsidebilde), size.x, size.y, true)));

        typeface = Typeface.createFromAsset(getAssets(), "valeraround.otf");

        tittel.setTypeface(typeface);

        getUser getuser = new getUser(this);
        getuser.execute();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                Criteria criteria = new Criteria();
                String bestprovider = locationManager.getBestProvider(criteria, false);

                Location location = locationManager.getLastKnownLocation(bestprovider);

                if(location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                try {
                    for(Address address : geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1)) {
                        if(address != null) {
                            Bruker.get().setCountry(address.getCountryName());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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
            default:
                fragment = new eventfragment();
                break;
        }

        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_content, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void onBilMenyClick(View v) {
        replaceFragment(1);
        TextView title = findViewById(R.id.title_toolbar);

        title.setText(getResources().getString(R.string.sjåfør));
    }

    public void onKalenderMenyClick(View v) {
        replaceFragment(0);
        TextView title = findViewById(R.id.title_toolbar);
        title.setText(getResources().getString(R.string.arrangementer));
    }

    public void onProfilMenyClick(View v) {
        replaceFragment(2);
        TextView title = findViewById(R.id.title_toolbar);
        title.setText(getResources().getString(R.string.profil));
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
                            dato.setText(String.format(Locale.ENGLISH, "%02d. %s %d", dayOfMonth, calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, ProfilActivity.this.getResources().getConfiguration().locale), year));
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
                            datotil.setText(String.format(Locale.ENGLISH, "%02d. %s %d", dayOfMonth, calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, ProfilActivity.this.getResources().getConfiguration().locale), year));
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
                EditText postnr = dialog.findViewById(R.id.create_postnr);
                EditText gate = dialog.findViewById(R.id.create_gate);
                EditText maks_deltakere = dialog.findViewById(R.id.maks_deltakere);
                EditText aldersgrense = dialog.findViewById(R.id.aldersgrense);

                if(titletext.length() >= 4 && !dato.getText().toString().isEmpty() && !time.getText().toString().isEmpty()) {
                    if(!datotil.getText().toString().isEmpty() && !timetil.getText().toString().isEmpty()) {
                        GregorianCalendar datefrom, dateto;

                        dateto = Utilities.getDateFromString(String.format(Locale.ENGLISH, "%s %s", datotil.getText().toString(), timetil.getText().toString()));
                        datefrom = Utilities.getDateFromString(String.format(Locale.ENGLISH, "%s %s", dato.getText().toString(), time.getText().toString()));

                        if(dateto != null && datefrom != null && !dateto.before(datefrom)) {

                            Event creating_event = new Event(titletext.getText().toString(), gate.getText().toString(), Bruker.get().getCountry(), Bruker.get().getBrukernavn(), alle_deltakere.isChecked(),
                                    0.0, 0.0, datefrom, dateto, Integer.valueOf(aldersgrense.getText().toString()),
                                    new ArrayList<>(Collections.singletonList(Participant.convertBrukerParticipant(Bruker.get(), EventStilling.VERT))), Integer.valueOf(maks_deltakere.getText().toString()),
                                    Integer.valueOf(postnr.getText().toString()), "", beskrivelse.getText().toString(), vis_gjesteliste.isChecked(), vis_adresse.isChecked(), new ArrayList<Requester>(), false);

                            GetLocationInfo getLocationInfo = new GetLocationInfo(ProfilActivity.this, gate.getText().toString(), Integer.valueOf(postnr.getText().toString()), creating_event, imageChange.getImage());
                            getLocationInfo.execute();
                        }
                    } else if(datotil.getText().toString().isEmpty() && timetil.getText().toString().isEmpty()) {
                        GregorianCalendar datefrom;

                        datefrom = Utilities.getDateFromString(String.format(Locale.ENGLISH, "%s %s", dato.getText().toString(), time.getText().toString()));

                        if(datefrom != null) {
                            Event creating_event = new Event(titletext.getText().toString(), gate.getText().toString(), Bruker.get().getCountry(), Bruker.get().getBrukernavn(), alle_deltakere.isChecked(),
                                    0.0, 0.0, datefrom, null, Integer.valueOf(aldersgrense.getText().toString()),
                                    new ArrayList<>(Collections.singletonList(Participant.convertBrukerParticipant(Bruker.get(), EventStilling.VERT))), Integer.valueOf(maks_deltakere.getText().toString()),
                                    Integer.valueOf(postnr.getText().toString()), "", beskrivelse.getText().toString(), vis_gjesteliste.isChecked(), vis_adresse.isChecked(), new ArrayList<Requester>(), false);

                            GetLocationInfo getLocationInfo = new GetLocationInfo(ProfilActivity.this, gate.getText().toString(), Integer.valueOf(postnr.getText().toString()), creating_event, imageChange.getImage());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1000) {
            for(int i = 0; i < permissions.length; i++) {
                if(permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "You have denied permission.", Toast.LENGTH_LONG).show();
                        LogoutUser logoutUser = new LogoutUser(this);
                        logoutUser.execute();
                        return;
                    }
                }
            }
        }
    }
}
