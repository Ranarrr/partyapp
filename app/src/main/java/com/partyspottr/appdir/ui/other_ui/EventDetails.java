package com.partyspottr.appdir.ui.other_ui;

import android.Manifest;
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
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.Participant;
import com.partyspottr.appdir.classes.Requester;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.GuestListAdapter;
import com.partyspottr.appdir.classes.adapters.RequestAdapter;
import com.partyspottr.appdir.classes.networking.AddEventRequest;
import com.partyspottr.appdir.classes.networking.AddFriendRequest;
import com.partyspottr.appdir.classes.networking.AddParticipant;
import com.partyspottr.appdir.classes.networking.GetLocationInfo;
import com.partyspottr.appdir.classes.networking.RemoveEvent;
import com.partyspottr.appdir.classes.networking.RemoveEventRequest;
import com.partyspottr.appdir.classes.networking.RemoveParticipant;
import com.partyspottr.appdir.enums.EventStilling;
import com.partyspottr.appdir.ui.ProfilActivity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
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

        //final Bitmap bmp = BitmapFactory.decodeByteArray(event.getImagebyte(), 0, event.getImagebyte().length);

        final TextView tittel = findViewById(R.id.details_tittel);
        ConstraintLayout event_sted_layout = findViewById(R.id.event_sted_layout);
        TextView sted = findViewById(R.id.details_sted);
        TextView poststed = findViewById(R.id.details_poststed);
        TextView datofra = findViewById(R.id.details_dato_fra);
        TextView aldersgrense_details = findViewById(R.id.aldersgrense_details);
        TextView host = findViewById(R.id.details_host);
        ImageButton more_options = findViewById(R.id.event_details_options);
        TextView datotil = findViewById(R.id.details_dato_til);
        TextView beskrivelse = findViewById(R.id.beskrivelse_details);
        TextView vis_gjesteliste = findViewById(R.id.details_visgjesteliste);
        vis_gjesteliste.setPaintFlags(vis_gjesteliste.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        TextView antall_deltakere = findViewById(R.id.details_antall_deltakere);
        Toolbar toolbar = findViewById(R.id.toolbar5);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tittel.setTypeface(typeface);
        sted.setTypeface(typeface);
        poststed.setTypeface(typeface);
        datofra.setTypeface(typeface);
        aldersgrense_details.setTypeface(typeface);
        host.setTypeface(typeface);
        datotil.setTypeface(typeface);
        beskrivelse.setTypeface(typeface);
        vis_gjesteliste.setTypeface(typeface);
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
                                new AlertDialog.Builder(EventDetails.this)
                                    .setTitle("Confirmation")
                                    .setMessage("Are you sure you want to delete this event?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            RemoveEvent removeEvent = new RemoveEvent(EventDetails.this, event.getEventId());
                                            removeEvent.execute();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {}
                                    })
                                    .show();

                                return true;

                            case R.id.edit_event:
                                final Dialog dialog = new Dialog(EventDetails.this);

                                dialog.setOwnerActivity(EventDetails.this);

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
                                final CheckBox vis_gjesteliste = dialog.findViewById(R.id.vis_gjesteliste);
                                final TextView by = dialog.findViewById(R.id.by_textview);

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
                                        if(ActivityCompat.checkSelfPermission(EventDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                            Intent intent = new Intent();
                                            intent.setType("image/*");
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), Utilities.SELECT_IMAGE_CODE);
                                        } else {
                                            ActivityCompat.requestPermissions(EventDetails.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Utilities.READ_EXTERNAL_STORAGE_CODE);
                                        }
                                    }
                                });

                                ProfilActivity.imageChange.addChangeListener(new PropertyChangeListener() {
                                    @Override
                                    public void propertyChange(PropertyChangeEvent evt) {
                                        try {
                                            legg_til_bilde.setImageBitmap(Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), ProfilActivity.imageChange.getUri()), (int) getResources().getDimension(R.dimen._150sdp), (int) getResources().getDimension(R.dimen._75sdp), true));
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
                                            TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetails.this, new TimePickerDialog.OnTimeSetListener() {
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
                                            DatePickerDialog dialog = new DatePickerDialog(EventDetails.this, new DatePickerDialog.OnDateSetListener() {
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
                                            TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetails.this, new TimePickerDialog.OnTimeSetListener() {
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
                                            DatePickerDialog dialog = new DatePickerDialog(EventDetails.this, new DatePickerDialog.OnDateSetListener() {
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
                                            new android.support.v7.app.AlertDialog.Builder(EventDetails.this)
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

                                        if(ProfilActivity.imageChange.getImage() == null) {
                                            new android.support.v7.app.AlertDialog.Builder(EventDetails.this)
                                                    .setTitle("Image")
                                                    .setMessage("Are you sure you do not want to add an image?")
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface somedialog, int which) {
                                                            Utilities.CheckAddEvent(dato, time, datotil, timetil, titletext, gate, aldersgrense, maks_deltakere, postnr, by, beskrivelse, dialog, vis_gjesteliste, alle_deltakere, vis_adresse, true);
                                                        }
                                                    })
                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {}})
                                                    .show();
                                        } else
                                            Utilities.CheckAddEvent(dato, time, datotil, timetil, titletext, gate, aldersgrense, maks_deltakere, postnr, by, beskrivelse, dialog, vis_gjesteliste, alle_deltakere, vis_adresse, true);
                                    }
                                });

                                dialog.show();

                                if(dialog.getWindow() != null) {
                                    dialog.getWindow().setAttributes(lp);
                                }

                                dato.setText(Utilities.getDateStringFromMillis(event.getDatefrom(), true));
                                time.setText(Utilities.getDateStringFromMillis(event.getDatefrom(), false));

                                if(event.getDateto() != 0) {
                                    sluttidspunkt.performClick();
                                    datotil.setText(Utilities.getDateStringFromMillis(event.getDateto(), true));
                                    timetil.setText(Utilities.getDateStringFromMillis(event.getDateto(), false));
                                }

                                if(event.isHasimage()) {
                                    StorageReference picRef = ProfilActivity.storage.getReference().child(event.getHostStr() + "_" + event.getNameofevent());

                                    picRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            legg_til_bilde.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                            ProfilActivity.imageChange.setBmp(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            legg_til_bilde.setImageDrawable(EventDetails.this.getResources().getDrawable(R.drawable.error_loading_image));
                                        }
                                    });
                                }

                                titletext.setText(event.getNameofevent());
                                gate.setText(event.getAddress());
                                aldersgrense.setText(event.getAgerestriction());
                                maks_deltakere.setText(event.getMaxparticipants());
                                postnr.setText(event.getPostalcode());
                                by.setText(event.getTown());
                                beskrivelse.setText(event.getDescription());
                                vis_gjesteliste.setChecked(event.isShowguestlist());
                                alle_deltakere.setChecked(event.isPrivateEvent());
                                vis_adresse.setChecked(event.isShowaddress());

                                return true;

                            default:
                                return true;
                        }
                    }
                });

                popupMenu.inflate(R.menu.more_options_event_details);

                popupMenu.show();
            }
        });

        final ImageView bilde = findViewById(R.id.imageView);

        if(event.isHasimage()) {
            StorageReference picRef = ProfilActivity.storage.getReference().child(event.getHostStr() + "_" + event.getNameofevent());

            picRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    bilde.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    bilde.setImageDrawable(getResources().getDrawable(R.drawable.error_loading_image));
                }
            });
        }

        tittel.setText(event.getNameofevent());
        sted.setText(String.format(Locale.ENGLISH, "%s", event.getAddress()));
        poststed.setText(String.format(Locale.ENGLISH, "%s, %s", event.getPostalcode(), event.getTown()));

        if(event.getDescription() != null)
            beskrivelse.setText(event.getDescription());

        event_sted_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?q=loc:" + event.getLatitude() + "," + event.getLongitude() + " (" + event.getNameofevent() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    EventDetails.this.startActivity(intent);
                } else {
                    Toast.makeText(EventDetails.this, "You do not have google maps installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        }

        final AppCompatButton details_deltaforesprsler = findViewById(R.id.details_delta_btn);

        details_deltaforesprsler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(event.getHostStr().equals(Bruker.get().getBrukernavn())) {
                    final Dialog dialog1 = new Dialog(EventDetails.this);
                    dialog1.requestWindowFeature(1);
                    dialog1.setContentView(R.layout.foresporsler);
                    dialog1.setCancelable(true);
                    dialog1.setCanceledOnTouchOutside(true);

                    Toolbar toolbar = dialog1.findViewById(R.id.toolbar_foresporsler);
                    TextView toolbar_title = dialog1.findViewById(R.id.foresporsel_TB_text);
                    ImageButton search = dialog1.findViewById(R.id.foresporsel_search);
                    final EditText foresporsel_search = dialog1.findViewById(R.id.foresporsel_search_field);
                    final ListView foresporsel_list = dialog1.findViewById(R.id.lv_foresporsler);

                    foresporsel_list.setAdapter(new RequestAdapter(EventDetails.this, event.getRequests(), event.getEventId()));

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
                                        foresporsel_list.setAdapter(new RequestAdapter(EventDetails.this, event.getRequests(), event.getEventId()));
                                        return;
                                    }

                                    List<Requester> list = new ArrayList<>();
                                    for(Requester requester : event.getRequests()) {
                                        if(requester.getBrukernavn().contains(s)) {
                                            list.add(requester);
                                        }
                                    }

                                    foresporsel_list.setAdapter(new RequestAdapter(EventDetails.this, list, event.getEventId()));
                                }

                                @Override
                                public void afterTextChanged(Editable s) {}
                            });
                        }
                    });

                    toolbar_title.setTypeface(typeface);

                    toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));

                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.onBackPressed();
                        }
                    });

                    dialog1.show();
                } else {
                    if(event.isPrivateEvent()) {
                        if(!event.isBrukerRequesting(Bruker.get().getBrukernavn()) && !event.isBrukerInList(Bruker.get().getBrukernavn())) {
                            AddEventRequest addRequest = new AddEventRequest(EventDetails.this, event.getEventId());
                            addRequest.execute();
                        } else
                            Toast.makeText(EventDetails.this, "You have already requested to join! Wait for the host to accept or deny you.", Toast.LENGTH_LONG).show();
                    } else {
                        if(!event.isBrukerInList(Bruker.get().getBrukernavn())) {
                            AddParticipant addParticipant = new AddParticipant(EventDetails.this, event.getEventId(), null, event.getParticipants());
                            addParticipant.execute();
                        }
                    }
                }
            }
        });

        if(event.isBrukerInList(Bruker.get().getBrukernavn()) && !event.getHostStr().equals(Bruker.get().getBrukernavn())) {
            //details_deltaforesprsler.(getResources().getDrawable(R.drawable.joint));
        } else if(event.isBrukerRequesting(Bruker.get().getBrukernavn()) && !event.getHostStr().equals(Bruker.get().getBrukernavn())) {
            //details_deltaforesprsler.setImageDrawable(getResources().getDrawable(R.drawable.request_waiting));
            details_deltaforesprsler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(EventDetails.this)
                            .setTitle("Remove")
                            .setMessage("Do you want to remove your request?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RemoveEventRequest eventRequest = new RemoveEventRequest(EventDetails.this, event.getEventId(), Bruker.get().getBrukernavn(), false);
                                    eventRequest.execute();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            })
                            .show();
                }
            });
        }

        if(event.getHostStr().equals(Bruker.get().getBrukernavn())) {
            //details_deltaforesprsler.setImageDrawable(getResources().getDrawable(R.drawable.view_queue));

            host.setText(String.format(Locale.ENGLISH, "Host: %s (deg)", event.getHostStr()));
        } else {
            host.setText(String.format(Locale.ENGLISH, "Host: %s", event.getHostStr()));
        }

        aldersgrense_details.setText(String.format(Locale.ENGLISH,"Aldersgrense: %d",event.getAgerestriction()));

        antall_deltakere.setText(String.format(Locale.ENGLISH, "%d av %d skal.", event.getParticipants().size(), event.getMaxparticipants()));

        if(event.isShowguestlist()) {
            vis_gjesteliste.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(EventDetails.this);
                    dialog.requestWindowFeature(1);
                    dialog.setContentView(R.layout.gjesteliste);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);

                    Toolbar toolbar = dialog.findViewById(R.id.toolbar3);
                    final ListView lv_gjesteliste = dialog.findViewById(R.id.lv_gjesteliste);

                    lv_gjesteliste.setAdapter(new GuestListAdapter(EventDetails.this, event.getEventId(), event.getParticipants()));

                    final EditText gjesteliste_search = dialog.findViewById(R.id.gjesteliste_search);
                    ImageButton gjesteliste_search_btn = dialog.findViewById(R.id.gjesteliste_search_btn);

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

                    toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));

                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.onBackPressed();
                        }
                    });

                    dialog.show();
                }
            });
        } else {
            if(event.isBrukerInList(Bruker.get().getBrukernavn())) {
                antall_deltakere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog gjesteliste = new Dialog(EventDetails.this);
                        gjesteliste.requestWindowFeature(1);
                        gjesteliste.setContentView(R.layout.gjesteliste);
                        gjesteliste.setCancelable(true);
                        gjesteliste.setCanceledOnTouchOutside(true);

                        Toolbar toolbar = gjesteliste.findViewById(R.id.toolbar3);
                        ListView lv_gjesteliste = gjesteliste.findViewById(R.id.lv_gjesteliste);
                        TextView gjesteliste_title = gjesteliste.findViewById(R.id.gjesteliste_TB_text);

                        gjesteliste_title.setTypeface(typeface);

                        lv_gjesteliste.setAdapter(new GuestListAdapter(EventDetails.this, event.getEventId(), event.getParticipants()));

                        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));

                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gjesteliste.onBackPressed();
                            }
                        });

                        gjesteliste.show();
                    }
                });
            }
        }
    }
}