package com.partyspottr.appdir.ui.mainfragments.bilchildfragments;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Car;
import com.partyspottr.appdir.classes.Chauffeur;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.CarBrands;
import com.partyspottr.appdir.classes.networking.ChauffeurAddNewTime;
import com.partyspottr.appdir.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Ranarrr on 30-Jan-18.
 *
 * @author Ranarrr
 */

public class min_bil_fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.chauffeur_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView title = getActivity().findViewById(R.id.chauffeur_title);
        final ConstraintLayout current_car = getActivity().findViewById(R.id.chauffeur_current_car);
        ImageView car_img = getActivity().findViewById(R.id.car_img); // TODO : Fix car image
        final TextView img_add = getActivity().findViewById(R.id.chauffeur_img_add);
        final Button registrate = getActivity().findViewById(R.id.chauffeur_registrate);
        final ConstraintLayout registrate_car_layout = getActivity().findViewById(R.id.chauffeur_add_car);
        final Button registrate_car = getActivity().findViewById(R.id.registrate_car);
        final ConstraintLayout legg_til_tid = getActivity().findViewById(R.id.chauffeur_legg_til_tid);
        //final Button legg_til_tid_btn = getActivity().findViewById(R.id.legg_til_tid_btn);
        //final EditText time_to = getActivity().findViewById(R.id.time_to);

        //time_to.setTypeface(MainActivity.typeface);
        //legg_til_tid_btn.setTypeface(MainActivity.typeface);
        registrate.setTypeface(MainActivity.typeface);
        img_add.setTypeface(MainActivity.typeface);
        title.setTypeface(MainActivity.typeface);

        //noinspection AndroidLintClickableViewAccessibility
        /*time_to.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar nowtime = Calendar.getInstance();

                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            GregorianCalendar to, from = new GregorianCalendar();
                            from.setTimeInMillis(System.currentTimeMillis());

                            to = from;

                            if(hourOfDay < from.get(Calendar.HOUR_OF_DAY)) {
                                to.add(Calendar.DAY_OF_MONTH, 1);
                            }

                            if(to.before(from)) {
                                Toast.makeText(getActivity(), "Please select a time atleast 30 minutes after the time you start driving.", Toast.LENGTH_LONG).show();
                                time_to.setText(String.format(Locale.ENGLISH, "%02d:%02d", from.get(Calendar.HOUR_OF_DAY), from.get(Calendar.MINUTE)));
                            } else if(to.getTimeInMillis() - from.getTimeInMillis() > 21600000) {
                                Toast.makeText(getActivity(), "You can not have more than 6 hours registrated as driver.", Toast.LENGTH_LONG).show();
                                if (from.get(Calendar.HOUR_OF_DAY) + 6 >= 24) {
                                        time_to.setText(String.format(Locale.ENGLISH, "%02d:%02d", (from.get(Calendar.HOUR_OF_DAY) + 6) - 24, from.get(Calendar.MINUTE)));
                                }
                            } else {
                                time_to.setText(String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minute));
                            }
                        }
                    }, nowtime.get(Calendar.HOUR_OF_DAY), nowtime.get(Calendar.MINUTE), true);
                    timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, getActivity().getResources().getString(R.string.angi), timePickerDialog);
                    timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, getActivity().getResources().getString(R.string.avbryt), timePickerDialog);
                    timePickerDialog.show();

                    return true;
                }

                return false;
            }
        });

        if(Bruker.get().isHascar()) {
            setAfterRegistrated(registrate_car_layout, title, current_car, img_add, legg_til_tid, legg_til_tid_btn, time_to);
        } else {
            title.setText("Registrer bil"); // TODO : Fix translation

            registrate.setVisibility(View.VISIBLE);

            registrate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AutoCompleteTextView merke = getActivity().findViewById(R.id.car_merke);
                    final TextInputLayout farge = getActivity().findViewById(R.id.car_farge);

                    merke.setTypeface(MainActivity.typeface);
                    farge.setTypeface(MainActivity.typeface);

                    if(!Bruker.get().isPremium()) {
                        registrate.setVisibility(View.INVISIBLE);

                        registrate_car_layout.setVisibility(View.VISIBLE);

                        merke.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, CarBrands.m_CarBrands));

                        registrate_car.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(farge.getEditText() != null && merke.getText().toString().length() > 2 && farge.getEditText().getText().toString().length() > 2 && CarBrands.doesListContain(merke.getText().toString())) {
                                    StringRequest stringRequest = new StringRequest(Utilities.getGETMethodArgStr("create_chauffeur", "socketElem",
                                            Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT), "brukernavnElem", Bruker.get().getBrukernavn(), "carlistElem", new Gson().toJson(Collections.singletonList(new Car(merke.getText().toString(), farge.getEditText().getText().toString(), false))).replace("\\", ""), "timeDrivingFrom", "0", "timeDrivingTo", "0", "rating", "0", "capacity", "0", "age",
                                            String.valueOf(Utilities.calcAge(new GregorianCalendar(Bruker.get().getYear(), Bruker.get().getMonth(), Bruker.get().getDay_of_month())))), new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject respons = new JSONObject(response);
                                                if (respons.getInt("success") == 1) {
                                                    Bruker.get().setHascar(true);
                                                    Bruker.get().getChauffeur().addCar(new Car(merke.getText().toString(), farge.getEditText().getText().toString(), false));
                                                    Bruker.get().getChauffeur().setM_brukernavn(Bruker.get().getBrukernavn());
                                                    Bruker.get().getChauffeur().setM_age(Utilities.calcAge(new GregorianCalendar(Bruker.get().getYear(), Bruker.get().getMonth(), Bruker.get().getDay_of_month())));
                                                    Bruker.get().setCurrent_car(Bruker.get().getChauffeur().getListOfCars().get(0));
                                                    Bruker.get().getChauffeur().LagreChauffeur();
                                                    setAfterRegistrated(registrate_car_layout, title, current_car, img_add, legg_til_tid, legg_til_tid_btn, time_to);
                                                } else {
                                                    Toast.makeText(getActivity(), "Failed to registrate car!", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {}
                                    });

                                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                                    queue.add(stringRequest);
                                }
                            }
                        });
                    } else {
                        new AlertDialog.Builder(getActivity())
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
                }
            });
        }*/
    }

    private void setAfterRegistrated(ConstraintLayout registrate_car_layout, TextView title, ConstraintLayout current_car, TextView img_add, ConstraintLayout legg_til_tid, final Button legg_til_tid_btn,
                                     final EditText time_to) {
        final Chauffeur brukerChauffeur = Bruker.get().getChauffeur();

        TextView navn = getActivity().findViewById(R.id.chauffeur_navn);
        TextView bil = getActivity().findViewById(R.id.chauffeur_bil);
        TextView plassering = getActivity().findViewById(R.id.chauffeur_plassering);

        final TextView ny_tid_title = getActivity().findViewById(R.id.ny_tid_title);
        final TextView antall_passasjerer = getActivity().findViewById(R.id.antall_passasjerer);
        final SeekBar maks_passasjerer = getActivity().findViewById(R.id.antall_passasjerer_bar);
        final TextView timer = getActivity().findViewById(R.id.ny_tid_timer);
        final TextView minutter = getActivity().findViewById(R.id.ny_tid_minutter);
        final ProgressBar time_progressbar = getActivity().findViewById(R.id.chauffeur_progressbar);
        final Button forny_tid = getActivity().findViewById(R.id.chauffeur_forny);
        final Button avslutt_tid = getActivity().findViewById(R.id.chauffeur_avslutt);

        avslutt_tid.setTypeface(MainActivity.typeface);
        forny_tid.setTypeface(MainActivity.typeface);
        ny_tid_title.setTypeface(MainActivity.typeface);
        antall_passasjerer.setTypeface(MainActivity.typeface);
        timer.setTypeface(MainActivity.typeface);
        minutter.setTypeface(MainActivity.typeface);
        navn.setTypeface(MainActivity.typeface);
        bil.setTypeface(MainActivity.typeface);
        plassering.setTypeface(MainActivity.typeface);

        navn.setText(String.format(Locale.ENGLISH, "%s %s (%d)", Bruker.get().getFornavn(), Bruker.get().getEtternavn(), brukerChauffeur.getM_age()));
        bil.setText(String.format(Locale.ENGLISH, "%s %s", Bruker.get().getCurrent_car().getFarge(), Bruker.get().getCurrent_car().getMerke()));
        plassering.setText(String.format(Locale.ENGLISH, "i %s", Bruker.get().getTown()));

        registrate_car_layout.setVisibility(View.INVISIBLE);

        title.setText("Min bil"); // TODO : Fix translation

        current_car.setVisibility(View.VISIBLE);

        if(Bruker.get().getCurrent_car().isHasImg()) {
            img_add.setText("Endre bilde"); // TODO : Fix translation
        } else {
            img_add.setText("Legg til bilde");
        }

        legg_til_tid.setVisibility(View.VISIBLE);

        if(Bruker.get().getChauffeur().getChauffeur_time_from() == 0 && Bruker.get().getChauffeur().getChauffeur_time_to() == 0) {
            ny_tid_title.setText("Start kjøreøkt");
            forny_tid.setVisibility(View.INVISIBLE);
            avslutt_tid.setVisibility(View.INVISIBLE);
            time_progressbar.setVisibility(View.INVISIBLE);

            legg_til_tid_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GregorianCalendar to = new GregorianCalendar(), now = new GregorianCalendar();
                    now.setTimeInMillis(System.currentTimeMillis());
                    to.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
                    to.set(Calendar.MINUTE, now.get(Calendar.MINUTE));

                    GregorianCalendar temp = Utilities.getDateFromString(time_to.getText().toString(), "HH:mm");

                    if(temp != null && temp.before(to)) {
                        to = now;
                        to.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
                        to.add(Calendar.DAY_OF_MONTH, 1);
                        to.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
                        to.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
                    }

                    if(!Bruker.get().isPremium()) {
                        if(to.after(now) && to.getTimeInMillis() - now.getTimeInMillis() <= 21600000) {
                            ChauffeurAddNewTime chauffeurAddNewTime = new ChauffeurAddNewTime(getActivity(), to.getTimeInMillis());
                            chauffeurAddNewTime.execute();
                        }
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Premium")
                                .setMessage("To use this feature you need to have premium.\nWould you like to purchase premium? (Pressing yes will not prompt you with a purchase, but with information.)")
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
                }
            });
        } else if(Bruker.get().getChauffeur().getChauffeur_time_from() != 0 && Bruker.get().getChauffeur().getChauffeur_time_to() != 0) {
            legg_til_tid_btn.setVisibility(View.INVISIBLE);
            antall_passasjerer.setVisibility(View.INVISIBLE);
            maks_passasjerer.setVisibility(View.INVISIBLE);
            timer.setVisibility(View.INVISIBLE);
            minutter.setVisibility(View.INVISIBLE);
            time_to.setVisibility(View.INVISIBLE);

            final long time = Bruker.get().getChauffeur().getChauffeur_time_to() - Bruker.get().getChauffeur().getChauffeur_time_from();
            if(time >= Integer.MAX_VALUE) {
                time_progressbar.setMax((int) time / 1000);
            } else {
                time_progressbar.setMax((int) time);
            }

            final long current = Bruker.get().getChauffeur().getChauffeur_time_to() - new Date().getTime();
            if(current >= Integer.MAX_VALUE) {
                time_progressbar.setProgress((int) current / 1000);
            } else {
                time_progressbar.setProgress((int) current);
            }

            new CountDownTimer(current, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    GregorianCalendar timetoset = new GregorianCalendar();
                    timetoset.setTimeInMillis(millisUntilFinished);

                    //ny_tid_title.setText(String.format(Locale.ENGLISH, "Gjenstående tid som sjåfør: %02d t %02d min %02d sek", timetoset.get(Calendar.HOUR_OF_DAY), timetoset.get(Calendar.MINUTE),
                    //        timetoset.get(Calendar.SECOND)));

                    time_progressbar.setProgress((int) +(millisUntilFinished - time));
                }

                @Override
                public void onFinish() {
                    legg_til_tid_btn.setVisibility(View.VISIBLE);
                    antall_passasjerer.setVisibility(View.VISIBLE);
                    maks_passasjerer.setVisibility(View.VISIBLE);
                    timer.setVisibility(View.VISIBLE);
                    minutter.setVisibility(View.VISIBLE);
                    time_to.setVisibility(View.VISIBLE);

                    ny_tid_title.setText("Start kjøreøkt");
                    forny_tid.setVisibility(View.INVISIBLE);
                    avslutt_tid.setVisibility(View.INVISIBLE);
                    time_progressbar.setVisibility(View.INVISIBLE);
                }
            }.start();
        }
    }
}
