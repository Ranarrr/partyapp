package com.partyspottr.appdir.ui.other_ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.ImageChange;
import com.partyspottr.appdir.classes.Participant;
import com.partyspottr.appdir.classes.Requester;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.GuestListAdapter;
import com.partyspottr.appdir.classes.application.GlideApp;
import com.partyspottr.appdir.classes.networking.GetLocationInfo;
import com.partyspottr.appdir.enums.EventStilling;
import com.partyspottr.appdir.enums.ReturnWhere;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.ProfilActivity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.partyspottr.appdir.ui.MainActivity.typeface;

/**
 * Created by Ranarrr on 28-Feb-18.
 *
 * @author Ranarrr
 */

public class EventDetails extends AppCompatActivity {
    public static ImageChange edit_event_imagechange = new ImageChange();
    public static View.OnClickListener onclickDetails;

    private Place attainedPlace;

    @Override
    protected void onStop() {
        Utilities.setupOnStop(this);

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        final Event event;

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            return;
        } else {
            event = Bruker.get().getEventFromID(extras.getLong("eventid"));

            if(event == null)
                return;
        }

        final TextView tittel = findViewById(R.id.details_tittel);
        final ConstraintLayout event_sted_layout = findViewById(R.id.event_sted_layout);
        final TextView sted = findViewById(R.id.details_sted);
        final TextView poststed = findViewById(R.id.details_poststed);
        final TextView datofra = findViewById(R.id.details_dato_fra);
        final TextView aldersgrense_details = findViewById(R.id.aldersgrense_details);
        final TextView host = findViewById(R.id.details_host);
        final ImageButton more_options = findViewById(R.id.event_details_options);
        final TextView datotil = findViewById(R.id.details_dato_til);
        final TextView beskrivelse = findViewById(R.id.beskrivelse_details);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.event_details_swipe);
        final TextView antall_deltakere = findViewById(R.id.details_antall_deltakere);
        final Toolbar toolbar = findViewById(R.id.toolbar5);
        final ImageView bilde = findViewById(R.id.imageView);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));
        final TextView kategori = findViewById(R.id.event_details_kategori);
        final AppCompatButton details_deltaforesprsler = findViewById(R.id.details_delta_btn);

        kategori.setTypeface(MainActivity.typeface);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        onclickDetails = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EventDetails.this, R.style.mydatepickerdialog)
                        .setTitle("Remove")
                        .setMessage("Do you want to remove your request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // REMOVE REQUEST
                                final ProgressDialog progressDialog = new ProgressDialog(EventDetails.this, R.style.mydatepickerdialog);
                                progressDialog.setCancelable(false);
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.setMessage("Processing..");
                                progressDialog.show();

                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(event.getEventId())).child("requests");

                                Requester.removeRequest(event.getRequests(), Bruker.get().getBrukernavn());
                                ref.setValue(new Gson().toJson(event.getRequests())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EventDetails.this, "Removed request!", Toast.LENGTH_SHORT).show();
                                        DoRefresh(event, host, aldersgrense_details, antall_deltakere, tittel, sted, poststed, beskrivelse, datofra, datotil, bilde, details_deltaforesprsler, swipeRefreshLayout,
                                                kategori);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EventDetails.this, "Failed to remove your request.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.hide();
                                        ref.onDisconnect();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .show();
            }
        };

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DoRefresh(event, host, aldersgrense_details, antall_deltakere, tittel, sted, poststed, beskrivelse, datofra, datotil, bilde, details_deltaforesprsler, swipeRefreshLayout, kategori);

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //noinspection AndroidLintClickableViewAccessibility
        /*asdoisjad.setOnTouchListener(new OnSwipeGestureListener(this) { TODO : Gotta fix this, looks so much better than whatever shit, only the navi icon is shit
            @Override
            public void onSwipeRight() {
                onBackPressed();
            }
        });*/

        tittel.setTypeface(typeface);
        sted.setTypeface(typeface);
        poststed.setTypeface(typeface);
        datofra.setTypeface(typeface);
        aldersgrense_details.setTypeface(typeface);
        host.setTypeface(typeface);
        datotil.setTypeface(typeface);
        details_deltaforesprsler.setTypeface(MainActivity.typeface);
        beskrivelse.setTypeface(typeface);
        antall_deltakere.setTypeface(typeface);

        more_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = new ContextThemeWrapper(EventDetails.this, R.style.popup);
                PopupMenu popupMenu = new PopupMenu(context, v);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_event:
                                new AlertDialog.Builder(EventDetails.this, R.style.mydatepickerdialog)
                                    .setTitle("Confirmation")
                                    .setMessage("Are you sure you want to delete this event?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO : REMOVE EVENT
                                            if(event.isHasimage()) {
                                                StorageReference asfafa = ProfilActivity.storage.getReference().child(event.getHostStr() + "_" + event.getNameofevent());
                                                asfafa.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(event.getEventId()));
                                                        ref.removeValue();
                                                        Toast.makeText(EventDetails.this, "Deleted event!", Toast.LENGTH_SHORT).show();
                                                        EventDetails.this.onBackPressed();
                                                    }
                                                });
                                            } else {
                                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(event.getEventId()));
                                                ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(EventDetails.this, "Deleted event!", Toast.LENGTH_SHORT).show();
                                                        EventDetails.this.onBackPressed();
                                                    }
                                                });
                                            }
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {}
                                    })
                                    .show();

                                return true;

                            case R.id.check_guestlist_event:
                                final Dialog guestlist = new Dialog(EventDetails.this);
                                guestlist.requestWindowFeature(1);
                                guestlist.setContentView(R.layout.gjesteliste);
                                guestlist.setCancelable(true);
                                guestlist.setCanceledOnTouchOutside(true);

                                Toolbar toolbarguestlist = guestlist.findViewById(R.id.toolbar3);
                                final ListView lv_gjesteliste = guestlist.findViewById(R.id.lv_gjesteliste);

                                lv_gjesteliste.setAdapter(new GuestListAdapter(EventDetails.this, event.getEventId(), event.getParticipants()));

                                final EditText gjesteliste_search = guestlist.findViewById(R.id.gjesteliste_search);
                                ImageButton gjesteliste_search_btn = guestlist.findViewById(R.id.gjesteliste_search_btn);

                                gjesteliste_search_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        gjesteliste_search.setVisibility(gjesteliste_search.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                                        gjesteliste_search.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                if(s.toString().isEmpty()) {
                                                    lv_gjesteliste.setAdapter(new GuestListAdapter(EventDetails.this, event.getEventId(), event.getParticipants()));
                                                    return;
                                                }

                                                List<Participant> list = new ArrayList<>();
                                                for(Participant participant : event.getParticipants()) {
                                                    if(participant.getBrukernavn().contains(s)) {
                                                        list.add(participant);
                                                    }
                                                }

                                                lv_gjesteliste.setAdapter(new GuestListAdapter(EventDetails.this, event.getEventId(), list));
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {}
                                        });
                                    }
                                });

                                toolbarguestlist.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));

                                toolbarguestlist.setNavigationOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        guestlist.onBackPressed();
                                    }
                                });

                                guestlist.show();
                                return true;

                            case R.id.check_requests_event:
                                Utilities.showEventDetailRequests(EventDetails.this, event);
                                return true;

                            case R.id.edit_event:
                                final Dialog edit_event = new Dialog(EventDetails.this);

                                edit_event.setOwnerActivity(EventDetails.this);

                                edit_event.requestWindowFeature(1);

                                edit_event.setCancelable(true);
                                edit_event.setCanceledOnTouchOutside(true);

                                edit_event.setContentView(R.layout.legg_til_event);

                                final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                if(edit_event.getWindow() != null) {
                                    lp.copyFrom(edit_event.getWindow().getAttributes());
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
                                }

                                final Button button = edit_event.findViewById(R.id.create_event_btn1);
                                final TextView til_label = edit_event.findViewById(R.id.textView12);
                                final EditText dato = edit_event.findViewById(R.id.datofratext);
                                final EditText time = edit_event.findViewById(R.id.timefratext);
                                final EditText datotil = edit_event.findViewById(R.id.datotiltext);
                                final EditText timetil = edit_event.findViewById(R.id.timetiltext);
                                final CheckBox alle_deltakere = edit_event.findViewById(R.id.alle_deltakere_må);
                                final TextView fjern_sluttidspunkt = edit_event.findViewById(R.id.fjern_sluttidspunkt);
                                fjern_sluttidspunkt.setPaintFlags(fjern_sluttidspunkt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                final CheckBox vis_adresse = edit_event.findViewById(R.id.vis_adresse);
                                final ImageButton legg_til_bilde = edit_event.findViewById(R.id.legg_til_event_image);
                                final EditText maks_deltakere = edit_event.findViewById(R.id.maks_deltakere);
                                final EditText titletext = edit_event.findViewById(R.id.create_eventText);
                                final EditText beskrivelse = edit_event.findViewById(R.id.beskrivelse_create);
                                final EditText aldersgrense = edit_event.findViewById(R.id.aldersgrense);
                                final TextView sluttidspunkt = edit_event.findViewById(R.id.legg_til_sluttidspunkt);
                                sluttidspunkt.setPaintFlags(sluttidspunkt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                Toolbar create_event_toolbar = edit_event.findViewById(R.id.toolbar2);
                                final AppCompatSpinner kategorier = edit_event.findViewById(R.id.kategori_spinner);
                                final CheckBox vis_gjesteliste = edit_event.findViewById(R.id.vis_gjesteliste);
                                final TextView by = edit_event.findViewById(R.id.by_textview);
                                TextView title = edit_event.findViewById(R.id.create_event_title);
                                TextView choose_from_map = edit_event.findViewById(R.id.choose_from_map);

                                choose_from_map.setTypeface(MainActivity.typeface);

                                choose_from_map.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

                                title.setTypeface(MainActivity.typeface);
                                title.setText("Edit event");

                                attainedPlace = null;

                                if(kategorier != null) {
                                    kategorier.setAdapter(new ArrayAdapter<>(EventDetails.this, R.layout.spinner_mine, Arrays.asList("Narch", "Vors", "Party", "Concert", "Nightclub", "Birthday", "Festival"))); // TODO : OVERSETTE
                                    kategorier.setSelection(Utilities.getCategoryFromString(event.getCategory().toString()).ordinal());
                                }

                                choose_from_map.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                                        try {
                                            startActivityForResult(builder.build(EventDetails.this), Utilities.PLACE_PICKER_CODE);
                                        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                create_event_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        edit_event.onBackPressed();
                                    }
                                });

                                create_event_toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));

                                legg_til_bilde.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(ActivityCompat.checkSelfPermission(EventDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                            Intent intent = new Intent(EventDetails.this, CropImage.class);
                                            intent.putExtra("returnwhere", ReturnWhere.EDIT_EVENT.ordinal());
                                            startActivity(intent);
                                        } else
                                            ActivityCompat.requestPermissions(EventDetails.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Utilities.READ_EXTERNAL_STORAGE_CODE);
                                    }
                                });

                                edit_event_imagechange.addChangeListener(new PropertyChangeListener() {
                                    @Override
                                    public void propertyChange(PropertyChangeEvent evt) {
                                        legg_til_bilde.setImageBitmap(edit_event_imagechange.getBmp());
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
                                            TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetails.this, R.style.mydatepickerdialog, new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    time.setText(String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minute));
                                                }
                                            }, nowtime.get(Calendar.HOUR_OF_DAY), nowtime.get(Calendar.MINUTE), true);
                                            timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, EventDetails.this.getResources().getString(R.string.angi), timePickerDialog);
                                            timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, EventDetails.this.getResources().getString(R.string.avbryt), timePickerDialog);
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
                                            DatePickerDialog dialog = new DatePickerDialog(EventDetails.this, R.style.mydatepickerdialog, new DatePickerDialog.OnDateSetListener() {
                                                @Override
                                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                    Calendar calendar;
                                                    calendar = Calendar.getInstance();
                                                    calendar.set(Calendar.MONTH, month);
                                                    dato.setText(String.format(Locale.ENGLISH, "%02d %s %d", dayOfMonth, calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                                                            EventDetails.this.getResources().getConfiguration().locale), year));
                                                }
                                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                                            dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, EventDetails.this.getResources().getString(R.string.angi), dialog);
                                            dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, EventDetails.this.getResources().getString(R.string.avbryt), dialog);
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
                                            TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetails.this, R.style.mydatepickerdialog, new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    timetil.setText(String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minute));
                                                }
                                            }, nowtime.get(Calendar.HOUR_OF_DAY), nowtime.get(Calendar.MINUTE), true);
                                            timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, EventDetails.this.getResources().getString(R.string.angi), timePickerDialog);
                                            timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, EventDetails.this.getResources().getString(R.string.avbryt), timePickerDialog);
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
                                            DatePickerDialog dialog = new DatePickerDialog(EventDetails.this, R.style.mydatepickerdialog, new DatePickerDialog.OnDateSetListener() {
                                                @Override
                                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                    Calendar calendar;
                                                    calendar = Calendar.getInstance();
                                                    calendar.set(Calendar.MONTH, month);
                                                    datotil.setText(String.format(Locale.ENGLISH, "%02d %s %d", dayOfMonth, calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                                                            EventDetails.this.getResources().getConfiguration().locale), year));
                                                }
                                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                                            dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, EventDetails.this.getResources().getString(R.string.angi), dialog);
                                            dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, EventDetails.this.getResources().getString(R.string.avbryt), dialog);
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
                                        if(titletext.length() <= 3) {
                                            Toast.makeText(EventDetails.this, "Please choose a title longer than 3 characters.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if(dato.getText().toString().isEmpty() || time.getText().toString().isEmpty()) {
                                            Toast.makeText(EventDetails.this, "Please choose a starting date.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if(maks_deltakere.getText().toString().isEmpty() || Integer.valueOf(maks_deltakere.getText().toString()) <= 1) {
                                            Toast.makeText(EventDetails.this, "Max participants can not be lower than 2.", Toast.LENGTH_SHORT).show();
                                            return;
                                        } else if(!maks_deltakere.getText().toString().isEmpty() && (Integer.valueOf(maks_deltakere.getText().toString()) > 40 && !Bruker.get().isPremium())) {
                                            new android.support.v7.app.AlertDialog.Builder(EventDetails.this, R.style.mydatepickerdialog)
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
                                            Toast.makeText(EventDetails.this, "The agerestriction can not be lower than 14.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if(by.getText().toString().equals("By")) {
                                            Toast.makeText(EventDetails.this, "Please make sure the address is filled in correctly.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if(edit_event_imagechange.getBmp() == null && edit_event_imagechange.getBmp() == null) {
                                            new android.support.v7.app.AlertDialog.Builder(EventDetails.this, R.style.mydatepickerdialog)
                                                    .setTitle("Image")
                                                    .setMessage("Are you sure you do not want to add an image?")
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface somedialog, int which) {
                                                            Utilities.CheckAddEvent(dato, time, datotil, timetil, titletext, attainedPlace, aldersgrense, maks_deltakere, beskrivelse, edit_event, vis_gjesteliste,
                                                                    alle_deltakere, vis_adresse, kategorier, true, event.getEventId());
                                                        }
                                                    })
                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {}})
                                                    .show();
                                        } else
                                            Utilities.CheckAddEvent(dato, time, datotil, timetil, titletext, attainedPlace, aldersgrense, maks_deltakere, beskrivelse, edit_event, vis_gjesteliste, alle_deltakere,
                                                    vis_adresse, kategorier, true, event.getEventId());
                                    }
                                });

                                edit_event.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialog) {
                                        if(edit_event.getWindow() != null) {
                                            edit_event.getWindow().setAttributes(lp);
                                        }

                                        dato.setText(Utilities.getDateStringFromMillis(event.getDatefrom(), true));
                                        time.setText(Utilities.getDateStringFromMillis(event.getDatefrom(), false));

                                        if(event.getDateto() != 0) {
                                            sluttidspunkt.performClick();
                                            datotil.setText(Utilities.getDateStringFromMillis(event.getDateto(), true));
                                            timetil.setText(Utilities.getDateStringFromMillis(event.getDateto(), false));
                                        }

                                        final Bitmap bmp = Bruker.getEventImages().get(event.getHostStr() + "_" + event.getNameofevent() + "_" +
                                                String.valueOf(Bruker.getEventImageSizeByName(event.getHostStr(), event.getNameofevent())));

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                legg_til_bilde.setImageBitmap(bmp);
                                            }
                                        });

                                        edit_event_imagechange.setBmp(bmp);

                                        titletext.setText(event.getNameofevent());
                                        by.setText(String.format(Locale.ENGLISH, "%s, %s", event.getAddress(), event.getTown()));
                                        aldersgrense.setText(String.valueOf(event.getAgerestriction()));
                                        maks_deltakere.setText(String.valueOf(event.getMaxparticipants()));
                                        beskrivelse.setText(event.getDescription());
                                        vis_gjesteliste.setChecked(event.isShowguestlist());
                                        alle_deltakere.setChecked(event.isPrivateEvent());
                                        vis_adresse.setChecked(event.isShowaddress());
                                    }
                                });

                                edit_event.show();

                                return true;

                            default:
                                return true;
                        }
                    }
                });

                popupMenu.inflate(R.menu.more_options_event_details);

                Participant bruker = Participant.getBrukerInList(event.getParticipants());

                if((bruker != null && bruker.getStilling() != EventStilling.VERT) || bruker == null) {
                    popupMenu.getMenu().removeItem(R.id.check_requests_event);
                    popupMenu.getMenu().removeItem(R.id.edit_event);
                    popupMenu.getMenu().removeItem(R.id.delete_event);
                }

                if(Bruker.get().getBrukernavn().equalsIgnoreCase(event.getHostStr()) && !event.isPrivateEvent())
                    popupMenu.getMenu().removeItem(R.id.check_requests_event);

                if(!event.isShowguestlist() && bruker == null)
                    popupMenu.getMenu().removeItem(R.id.check_guestlist_event);

                popupMenu.show();
            }
        });

        event_sted_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EventDetails.this, R.style.mydatepickerdialog)
                        .setTitle("Show maps")
                        .setMessage("Do you want to exit and show location in google maps?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String uri = "http://maps.google.com/maps?q=loc:" + event.getLatitude() + "," + event.getLongitude() + " (" + event.getNameofevent() + ")";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                if (intent.resolveActivity(getPackageManager()) != null)
                                    EventDetails.this.startActivity(intent);
                                else
                                    Toast.makeText(EventDetails.this, "You do not have google maps installed.", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", null)
                .show();
            }
        });

        DoRefresh(event, host, aldersgrense_details, antall_deltakere, tittel, sted, poststed, beskrivelse, datofra, datotil, bilde, details_deltaforesprsler, swipeRefreshLayout, kategori);
    }

    private void DoRefresh(final Event event, final TextView host, final TextView aldersgrense_details, final TextView antall_deltakere, final TextView tittel, final TextView sted, final TextView poststed,
                           final TextView beskrivelse, final TextView datofra, final TextView datotil, final ImageView bilde, final AppCompatButton details_deltaforesprsler,
                           final SwipeRefreshLayout swipeRefreshLayout, final TextView kategori) {
        final Event updatedEvent = Bruker.get().getEventFromID(event.getEventId());

        if(updatedEvent == null) {
            onBackPressed();
            return;
        }

        event.CopyEvent(updatedEvent);

        swipeRefreshLayout.setRefreshing(true);

        String firstletter = event.getCategory().toString().substring(0, 1).toUpperCase();
        kategori.setText(String.format(Locale.ENGLISH, "%s%s", firstletter, event.getCategory().toString().toLowerCase().substring(1, event.getCategory().toString().length())));

        if(updatedEvent.getHostStr().equals(Bruker.get().getBrukernavn()))
            host.setText(String.format(Locale.ENGLISH, getString(R.string.host_deg), updatedEvent.getHostStr()));
        else
            host.setText(String.format(Locale.ENGLISH, getString(R.string.host), updatedEvent.getHostStr()));

        aldersgrense_details.setText(String.format(Locale.getDefault(),getString(R.string.agerestriction), updatedEvent.getAgerestriction()));

        antall_deltakere.setText(String.format(Locale.getDefault(), getString(R.string.zero_out_of_zero_going), updatedEvent.getParticipants().size(), updatedEvent.getMaxparticipants()));

        tittel.setText(updatedEvent.getNameofevent());

        if(!updatedEvent.isPrivateEvent() || updatedEvent.isBrukerInList(Bruker.get().getBrukernavn())) {
            sted.setVisibility(View.VISIBLE);
            poststed.setVisibility(View.VISIBLE);
            findViewById(R.id.location_img).setVisibility(View.VISIBLE);
            sted.setText(String.format(Locale.ENGLISH, "%s", updatedEvent.getAddress()));
            poststed.setText(String.format(Locale.ENGLISH, "%s", updatedEvent.getTown()));
        } else if(updatedEvent.isPrivateEvent() && !updatedEvent.isBrukerInList(Bruker.get().getBrukernavn())) {
            sted.setVisibility(View.GONE);
            poststed.setVisibility(View.GONE);
            findViewById(R.id.location_img).setVisibility(View.GONE);
        }

        if(updatedEvent.getDescription() != null)
            beskrivelse.setText(updatedEvent.getDescription());

        StringBuilder finaldatofra = new StringBuilder(), finaldatotil = new StringBuilder();

        if(event.getDatefrom() == 0) {
            onBackPressed();
            return;
        } else {
            GregorianCalendar datefrom = new GregorianCalendar();
            datefrom.setTimeInMillis(event.getDatefrom());
            if(event.getDateto() == 0) {
                if(datefrom.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                    finaldatofra.append(String.format(Locale.ENGLISH, "Starter %02d %s kl: %02d:%02d", datefrom.get(Calendar.DAY_OF_MONTH),
                            datefrom.getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(),
                            datefrom.get(Calendar.HOUR_OF_DAY), datefrom.get(Calendar.MINUTE)));

                    Spannable spannable = new SpannableString(finaldatofra.toString());

                    spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 0, "Starter".length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                    spannable.setSpan(new ForegroundColorSpan(Color.GRAY), finaldatofra.indexOf("kl:"), (finaldatofra.indexOf("kl:") + "kl:".length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                    datofra.setText(spannable, TextView.BufferType.SPANNABLE);
                } else {
                    finaldatofra.append(String.format(Locale.ENGLISH, "Starter %02d %s %d kl: %02d:%02d", datefrom.get(Calendar.DAY_OF_MONTH),
                            datefrom.getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), datefrom.get(Calendar.YEAR),
                            datefrom.get(Calendar.HOUR_OF_DAY), datefrom.get(Calendar.MINUTE)));
                }

                datotil.setVisibility(View.GONE);
            } else {
                GregorianCalendar dateto = new GregorianCalendar();
                dateto.setTimeInMillis(event.getDateto());
                if(dateto.get(Calendar.YEAR) == datefrom.get(Calendar.YEAR)) {
                    if(dateto.get(Calendar.DAY_OF_MONTH) == datefrom.get(Calendar.DAY_OF_MONTH)) { // TODO: ADD CHECKING MONTH
                        if(datefrom.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                            finaldatofra.append(String.format(Locale.ENGLISH, "Fra %s %02d %s kl: %02d:%02d - %02d:%02d", datefrom.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,
                                    getResources().getConfiguration().locale), datefrom.get(Calendar.DAY_OF_MONTH), datefrom.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                                    getResources().getConfiguration().locale).toLowerCase(), datefrom.get(Calendar.HOUR_OF_DAY), datefrom.get(Calendar.MINUTE), dateto.get(Calendar.HOUR_OF_DAY),
                                    dateto.get(Calendar.MINUTE)));

                            Spannable spannable = new SpannableString(finaldatofra.toString());

                            spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 0, "Fra".length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                            spannable.setSpan(new ForegroundColorSpan(Color.GRAY), finaldatofra.indexOf("kl:"), (finaldatofra.indexOf("kl:") + "kl:".length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                            datofra.setText(spannable, TextView.BufferType.SPANNABLE);
                        } else {
                            finaldatofra.append(String.format(Locale.ENGLISH, "Fra %02d %s %d kl: %02d:%02d - %02d:%02d", datefrom.get(Calendar.DAY_OF_MONTH),
                                    datefrom.getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), datefrom.get(Calendar.YEAR),
                                    datefrom.get(Calendar.HOUR_OF_DAY), datefrom.get(Calendar.MINUTE), dateto.get(Calendar.HOUR_OF_DAY), dateto.get(Calendar.MINUTE)));
                        }

                        datotil.setVisibility(View.GONE);
                    } else {
                        finaldatofra.append(String.format(Locale.ENGLISH, "Fra %02d %s kl: %02d:%02d", datefrom.get(Calendar.DAY_OF_MONTH),
                                datefrom.getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), datefrom.get(Calendar.HOUR_OF_DAY),
                                datefrom.get(Calendar.MINUTE)));

                        finaldatotil.append(String.format(Locale.ENGLISH, "Til %02d %s kl: %02d:%02d", dateto.get(Calendar.DAY_OF_MONTH),
                                dateto.getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), dateto.get(Calendar.HOUR_OF_DAY),
                                dateto.get(Calendar.MINUTE)));
                    }
                } else {
                    finaldatofra.append(String.format(Locale.ENGLISH, "Fra %02d %s %d kl: %02d:%02d", datefrom.get(Calendar.DAY_OF_MONTH),
                            datefrom.getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), datefrom.get(Calendar.YEAR),
                            datefrom.get(Calendar.HOUR_OF_DAY), datefrom.get(Calendar.MINUTE)));

                    finaldatotil.append(String.format(Locale.ENGLISH, "Til %02d %s %d kl: %02d:%02d", dateto.get(Calendar.DAY_OF_MONTH),
                            dateto.getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), dateto.get(Calendar.YEAR),
                            dateto.get(Calendar.HOUR_OF_DAY), dateto.get(Calendar.MINUTE)));
                }
            }
        }

        datotil.setText(finaldatotil.toString());

        if(finaldatotil.toString().isEmpty()) {
            datotil.setVisibility(View.GONE);
            ConstraintLayout layout = findViewById(R.id.datostraint);
            ConstraintSet set = new ConstraintSet();

            set.clone(layout);
            set.centerVertically(datofra.getId(), layout.getId());
            set.applyTo(layout);
        } else
            datotil.setVisibility(View.VISIBLE);

        if(event.isHasimage()) {
            final StorageReference picRef = ProfilActivity.storage.getReference().child(event.getHostStr() + "_" + event.getNameofevent());

            if (Bruker.getEventImageSizeByName(event.getHostStr(), event.getNameofevent()) > 0) {
                picRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        if (storageMetadata.getSizeBytes() == Bruker.getEventImageSizeByName(event.getHostStr(), event.getNameofevent())) {
                            bilde.setImageBitmap(Bruker.getEventImages().get(event.getHostStr() + "_" + event.getNameofevent() + "_" + String.valueOf(storageMetadata.getSizeBytes())));
                        } else {
                            picRef.getBytes(2048 * 2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    bilde.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                    Bruker.RemoveEventImage(event.getHostStr(), event.getNameofevent());
                                    Bruker.AddEventImage(event.getHostStr() + "_" + event.getNameofevent() + "_" + String.valueOf(bytes.length), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    bilde.setImageDrawable(EventDetails.this.getResources().getDrawable(R.drawable.error_loading_image));
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bilde.setImageBitmap(Bruker.getEventImages().get(event.getHostStr() + "_" + event.getNameofevent()));
                    }
                });
            } else {
                picRef.getBytes(2048 * 2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        bilde.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        Bruker.AddEventImage(event.getHostStr() + "_" + event.getNameofevent() + "_" + String.valueOf(bytes.length), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bilde.setImageDrawable(getResources().getDrawable(R.drawable.error_loading_image));
                    }
                });
            }
        } else
            bilde.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        if(event.isBrukerInList(Bruker.get().getBrukernavn()) && !event.getHostStr().equals(Bruker.get().getBrukernavn())) {
            // BRUKER HAS ALREADY JOINED
            details_deltaforesprsler.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check_full, 0, 0, 0);
            details_deltaforesprsler.setText("Joined!");
            details_deltaforesprsler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new android.support.v7.app.AlertDialog.Builder(EventDetails.this, R.style.mydatepickerdialog)
                            .setTitle("Leave event")
                            .setMessage("Are you sure you want to leave the event?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final Event leavingevent = Bruker.get().getEventFromID(event.getEventId());

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(leavingevent.getEventId())).child("participants");

                                    leavingevent.getParticipants().remove(Participant.getParticipantPos(leavingevent.getParticipants(), Bruker.get().getBrukernavn()));

                                    ref.setValue(new Gson().toJson(leavingevent.getParticipants())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ListView lv_guestlist = findViewById(R.id.lv_gjesteliste);

                                            if(lv_guestlist != null)
                                                lv_guestlist.setAdapter(new GuestListAdapter(EventDetails.this, leavingevent.getEventId(), leavingevent.getParticipants()));

                                            Toast.makeText(EventDetails.this, "You left the event.", Toast.LENGTH_SHORT).show();

                                            DoRefresh(event, host, aldersgrense_details, antall_deltakere, tittel, sted, poststed, beskrivelse, datofra, datotil, bilde, details_deltaforesprsler, swipeRefreshLayout,
                                                    kategori);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EventDetails.this, "Failed to remove you from the event.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });
        } else if(event.getHostStr().equals(Bruker.get().getBrukernavn())) {
            // BRUKER IS HOST
            details_deltaforesprsler.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            details_deltaforesprsler.setText("You are the host."); // TODO : TRANSLATE
            details_deltaforesprsler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utilities.showEventDetailRequests(EventDetails.this, event);
                }
            });
        } else if(event.isBrukerRequesting(Bruker.get().getBrukernavn())) {
            // BRUKER IS REQUESTING
            details_deltaforesprsler.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            details_deltaforesprsler.setText("Waiting...");
            details_deltaforesprsler.setOnClickListener(onclickDetails);
        } else if(!event.isBrukerInList(Bruker.get().getBrukernavn()) && !event.isBrukerRequesting(Bruker.get().getBrukernavn())) {
            // BRUKER IS NOT REQUESTING AND NOT IN LIST
            details_deltaforesprsler.setText(event.isPrivateEvent() ? getString(R.string.send_request) : getString(R.string.join_event));
            details_deltaforesprsler.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check_empty, 0, 0, 0);
            details_deltaforesprsler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(event.isPrivateEvent()) {
                        new AlertDialog.Builder(EventDetails.this, R.style.mydatepickerdialog)
                                .setTitle("Remove")
                                .setMessage("Do you want to request to join?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // REQUEST TO JOIN
                                        details_deltaforesprsler.setEnabled(false);
                                        final ProgressDialog progressDialog = new ProgressDialog(EventDetails.this, R.style.mydatepickerdialog);
                                        progressDialog.setMessage(progressDialog.getContext().getResources().getString(R.string.requesting));
                                        progressDialog.setCanceledOnTouchOutside(false);
                                        progressDialog.show();

                                        final Event event = Bruker.get().getEventFromID(updatedEvent.getEventId());

                                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(updatedEvent.getEventId())).child("requests");

                                        event.getRequests().add(Requester.convertBrukerRequester(Bruker.get()));
                                        ref.setValue(new Gson().toJson(event.getRequests())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                    details_deltaforesprsler.setEnabled(true);
                                                    details_deltaforesprsler.setOnClickListener(onclickDetails);

                                                    DoRefresh(event, host, aldersgrense_details, antall_deltakere, tittel, sted, poststed, beskrivelse, datofra, datotil, bilde, details_deltaforesprsler,
                                                            swipeRefreshLayout, kategori);

                                                    Toast.makeText(EventDetails.this, getResources().getString(R.string.requested_to_join), Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(EventDetails.this, getResources().getString(R.string.failed_to_request_to_join), Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.hide();
                                                ref.onDisconnect();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    } else {
                        // JOIN
                        details_deltaforesprsler.setEnabled(false);

                        final ProgressDialog join_progress = new ProgressDialog(EventDetails.this, R.style.mydatepickerdialog);
                        join_progress.setCanceledOnTouchOutside(false);
                        join_progress.setCancelable(false);
                        join_progress.setMessage("Joining..");
                        join_progress.show();

                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(event.getEventId())).child("participants");

                        Event seperate_event = Bruker.get().getEventFromID(event.getEventId());

                        seperate_event.getParticipants().add(Participant.convertBrukerParticipant(Bruker.get(), Bruker.get().isPremium() ? EventStilling.PREMIUM : EventStilling.GJEST));

                        ref.setValue(new Gson().toJson(seperate_event.getParticipants())).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        DoRefresh(event, host, aldersgrense_details, antall_deltakere, tittel, sted, poststed, beskrivelse, datofra, datotil, bilde, details_deltaforesprsler, swipeRefreshLayout,
                                                kategori);
                                    }
                                });

                                details_deltaforesprsler.setEnabled(true);

                                Toast.makeText(EventDetails.this, "Joined event.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EventDetails.this, "Failed to join event.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                join_progress.hide();
                                ref.onDisconnect();
                            }
                        });
                    }
                }
            });
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Utilities.PLACE_PICKER_CODE) {
            if(resultCode == RESULT_OK) {
                if(attainedPlace.getAddress() == null) {
                    Toast.makeText(this, "Failed to get location, please try again..", Toast.LENGTH_SHORT).show();

                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    try {
                        startActivityForResult(builder.build(EventDetails.this), Utilities.PLACE_PICKER_CODE);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                } else {
                    attainedPlace = PlacePicker.getPlace(this, data);
                    Toast.makeText(this, "Location set!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Utilities.READ_EXTERNAL_STORAGE_CODE) {
            for(int i = 0; i < permissions.length; i++) {
                if(permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(EventDetails.this, CropImage.class);
                        intent.putExtra("returnwhere", ReturnWhere.EDIT_EVENT.ordinal());
                        startActivity(intent);
                    }
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}