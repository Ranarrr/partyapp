package com.partyspottr.appdir.ui.other_ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.Participant;
import com.partyspottr.appdir.classes.Requester;
import com.partyspottr.appdir.classes.adapters.GuestListAdapter;
import com.partyspottr.appdir.classes.adapters.RequestAdapter;
import com.partyspottr.appdir.classes.networking.AddEventRequest;
import com.partyspottr.appdir.classes.networking.AddParticipant;
import com.partyspottr.appdir.classes.networking.RemoveEventRequest;
import com.partyspottr.appdir.ui.ProfilActivity;

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

        final ImageButton details_deltaforesprsler = findViewById(R.id.details_delta_btn);

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
            details_deltaforesprsler.setImageDrawable(getResources().getDrawable(R.drawable.joint));
        } else if(event.isBrukerRequesting(Bruker.get().getBrukernavn()) && !event.getHostStr().equals(Bruker.get().getBrukernavn())) {
            details_deltaforesprsler.setImageDrawable(getResources().getDrawable(R.drawable.request_waiting));
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
            details_deltaforesprsler.setImageDrawable(getResources().getDrawable(R.drawable.view_queue));

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