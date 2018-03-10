package com.partyspottr.appdir.ui.other_ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.Participant;
import com.partyspottr.appdir.classes.Requester;
import com.partyspottr.appdir.classes.adapters.GuestListAdapter;
import com.partyspottr.appdir.classes.networking.AddParticipant;
import com.partyspottr.appdir.classes.networking.AddRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.partyspottr.appdir.ui.ProfilActivity.typeface;

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

        ImageView bilde = findViewById(R.id.imageView);

        //bilde.setImageBitmap(bmp);

        tittel.setText(event.getNameofevent());
        sted.setText(String.format(Locale.ENGLISH, "%s", event.getAddress()));
        poststed.setText(String.format(Locale.ENGLISH, "%04d, %s", event.getPostalcode(), event.getTown()));

        if(event.getDescription() != null)
            beskrivelse.setText(event.getDescription());

        StringBuilder finaldatofra = new StringBuilder(), finaldatotil = new StringBuilder();

        if(event.getDatefrom() == null) {
            return;
        } else {
            if(event.getDateto() == null) {
                if(event.getDatefrom().get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                    finaldatofra.append(String.format(Locale.ENGLISH, "Starter %02d %s kl: %02d:%02d", event.getDatefrom().get(Calendar.DAY_OF_MONTH),
                            event.getDatefrom().getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(),
                            event.getDatefrom().get(Calendar.HOUR_OF_DAY), event.getDatefrom().get(Calendar.MINUTE)));

                    Spannable spannable = new SpannableString(finaldatofra.toString());

                    spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 0, "Starter".length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                    spannable.setSpan(new ForegroundColorSpan(Color.GRAY), finaldatofra.indexOf("kl:"), (finaldatofra.indexOf("kl:") + "kl:".length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                    datofra.setText(spannable, TextView.BufferType.SPANNABLE);
                } else {
                    finaldatofra.append(String.format(Locale.ENGLISH, "Starter %02d %s %d kl: %02d:%02d", event.getDatefrom().get(Calendar.DAY_OF_MONTH),
                            event.getDatefrom().getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), event.getDatefrom().get(Calendar.YEAR),
                            event.getDatefrom().get(Calendar.HOUR_OF_DAY), event.getDatefrom().get(Calendar.MINUTE)));
                }

                datotil.setVisibility(View.GONE);
            } else {
                if(event.getDateto().get(Calendar.YEAR) == event.getDatefrom().get(Calendar.YEAR)) {
                    if(event.getDateto().get(Calendar.DAY_OF_MONTH) == event.getDatefrom().get(Calendar.DAY_OF_MONTH)) {
                        if(event.getDatefrom().get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                            finaldatofra.append(String.format(Locale.ENGLISH, "Fra %02d %s kl: %02d:%02d - %02d:%02d", event.getDatefrom().get(Calendar.DAY_OF_MONTH),
                                    event.getDatefrom().getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), event.getDatefrom().get(Calendar.HOUR_OF_DAY),
                                    event.getDatefrom().get(Calendar.MINUTE), event.getDateto().get(Calendar.HOUR_OF_DAY), event.getDateto().get(Calendar.MINUTE)));

                            Spannable spannable = new SpannableString(finaldatofra.toString());

                            spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 0, "Fra".length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                            spannable.setSpan(new ForegroundColorSpan(Color.GRAY), finaldatofra.indexOf("kl:"), (finaldatofra.indexOf("kl:") + "kl:".length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                            datofra.setText(spannable, TextView.BufferType.SPANNABLE);
                        } else {
                            finaldatofra.append(String.format(Locale.ENGLISH, "Fra %02d %s %d kl: %02d:%02d - %02d:%02d", event.getDatefrom().get(Calendar.DAY_OF_MONTH),
                                    event.getDatefrom().getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), event.getDatefrom().get(Calendar.YEAR),
                                    event.getDatefrom().get(Calendar.HOUR_OF_DAY), event.getDatefrom().get(Calendar.MINUTE), event.getDateto().get(Calendar.HOUR_OF_DAY), event.getDateto().get(Calendar.MINUTE)));
                        }

                        datotil.setVisibility(View.GONE);
                    } else {
                        finaldatofra.append(String.format(Locale.ENGLISH, "Fra %02d %s kl: %02d:%02d", event.getDatefrom().get(Calendar.DAY_OF_MONTH),
                                event.getDatefrom().getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), event.getDatefrom().get(Calendar.HOUR_OF_DAY),
                                event.getDatefrom().get(Calendar.MINUTE)));

                        finaldatotil.append(String.format(Locale.ENGLISH, "Til %02d %s kl: %02d:%02d", event.getDateto().get(Calendar.DAY_OF_MONTH),
                                event.getDateto().getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), event.getDateto().get(Calendar.HOUR_OF_DAY),
                                event.getDateto().get(Calendar.MINUTE)));
                    }
                } else {
                    finaldatofra.append(String.format(Locale.ENGLISH, "Fra %02d %s %d kl: %02d:%02d", event.getDatefrom().get(Calendar.DAY_OF_MONTH),
                            event.getDatefrom().getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), event.getDatefrom().get(Calendar.YEAR),
                            event.getDatefrom().get(Calendar.HOUR_OF_DAY), event.getDatefrom().get(Calendar.MINUTE)));

                    finaldatotil.append(String.format(Locale.ENGLISH, "Til %02d %s %d kl: %02d:%02d", event.getDateto().get(Calendar.DAY_OF_MONTH),
                            event.getDateto().getDisplayName(Calendar.MONTH, Calendar.SHORT, getResources().getConfiguration().locale).toLowerCase(), event.getDateto().get(Calendar.YEAR),
                            event.getDateto().get(Calendar.HOUR_OF_DAY), event.getDateto().get(Calendar.MINUTE)));
                }
            }
        }


        datotil.setText(finaldatotil.toString());

        if(finaldatotil.toString().isEmpty()) {
            datotil.setVisibility(View.GONE);
            datofra.setBottom((int)getResources().getDimension(R.dimen._10sdp));
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
                                        //foresporsel_list.setAdapter(new GuestListAdapter(thisActivity, event.getParticipants()));
                                        return;
                                    }

                                    List<Requester> list = new ArrayList<>();
                                    for(Requester requester : event.getRequests()) {
                                        if(requester.getBrukernavn().contains(s)) {
                                            list.add(requester);
                                        }
                                    }

                                    //foresporsel_list.setAdapter(new GuestListAdapter(thisActivity, list));
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
                        AddRequest addRequest = new AddRequest(EventDetails.this, event.getEventId());
                        addRequest.execute();
                    } else {
                        if(!event.isBrukerInList(Bruker.get().getBrukernavn())) {
                            AddParticipant addParticipant = new AddParticipant(EventDetails.this, event.getEventId());
                            addParticipant.execute();
                        }
                    }
                }
            }
        });

        if(event.isBrukerInList(Bruker.get().getBrukernavn()) && !event.getHostStr().equals(Bruker.get().getBrukernavn())) {
            details_deltaforesprsler.setImageDrawable(getResources().getDrawable(R.drawable.joint));
        }

        if(event.getHostStr().equals(Bruker.get().getBrukernavn())) {
            //details_deltaforesprsler.;

            host.setText(String.format(Locale.ENGLISH, "Host: %s (deg)", event.getHostStr()));
        } else {
            host.setText(String.format(Locale.ENGLISH, "Host: %s", event.getHostStr()));
        }

        aldersgrense_details.setText(String.format(Locale.ENGLISH,"Aldersgrense: %d",event.getAgerestriction()));

        antall_deltakere.setText(String.format(Locale.ENGLISH, "%d av %d plasser er tatt.", event.getParticipants().size(), event.getMaxparticipants()));

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

                    final GuestListAdapter guestListAdapter = new GuestListAdapter(EventDetails.this, event.getParticipants());

                    lv_gjesteliste.setAdapter(guestListAdapter);

                    final EditText gjesteliste_søk = dialog.findViewById(R.id.gjesteliste_søk);
                    ImageButton gjesteliste_søk_btn = dialog.findViewById(R.id.gjesteliste_søk_btn);

                    gjesteliste_søk_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gjesteliste_søk.setVisibility(gjesteliste_søk.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                            gjesteliste_søk.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    if(s.toString().isEmpty()) {
                                        lv_gjesteliste.setAdapter(new GuestListAdapter(EventDetails.this, event.getParticipants()));
                                        return;
                                    }

                                    List<Participant> list = new ArrayList<>();
                                    for(Participant participant : event.getParticipants()) {
                                        if(participant.getBrukernavn().contains(s)) {
                                            list.add(participant);
                                        }
                                    }

                                    lv_gjesteliste.setAdapter(new GuestListAdapter(EventDetails.this, list));
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

                        lv_gjesteliste.setAdapter(new GuestListAdapter(EventDetails.this, event.getParticipants()));

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