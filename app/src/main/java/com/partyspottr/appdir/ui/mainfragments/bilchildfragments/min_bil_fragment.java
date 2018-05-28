package com.partyspottr.appdir.ui.mainfragments.bilchildfragments;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.partyspottr.appdir.classes.networking.ChauffeurRemoveTime;
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
    public static int passasjerer, timer_tid, minutter_tid;
    public static CountDownTimer countDownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.chauffeur_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        passasjerer = 0;
        timer_tid = 0;
        minutter_tid = -1;

        final ToggleButton pas_1, pas_2, pas_3, pas_4, pas_5, pas_6, pas_7,
        timer_1, timer_2, timer_3, timer_4, timer_5,
        min_0, min_15, min_30, min_45;

        pas_1 = view.findViewById(R.id.passasjer_1);
        pas_2 = view.findViewById(R.id.passasjer_2);
        pas_3 = view.findViewById(R.id.passasjer_3);
        pas_4 = view.findViewById(R.id.passasjer_4);
        pas_5 = view.findViewById(R.id.passasjer_5);
        pas_6 = view.findViewById(R.id.passasjer_6);
        pas_7 = view.findViewById(R.id.passasjer_7);

        timer_1 = view.findViewById(R.id.timer_1);
        timer_2 = view.findViewById(R.id.timer_2);
        timer_3 = view.findViewById(R.id.timer_3);
        timer_4 = view.findViewById(R.id.timer_4);
        timer_5 = view.findViewById(R.id.timer_5);

        min_0 = view.findViewById(R.id.minutter_0);
        min_15 = view.findViewById(R.id.minutter_15);
        min_30 = view.findViewById(R.id.minutter_30);
        min_45 = view.findViewById(R.id.minutter_45);

        pas_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(pas_2, pas_3, pas_4, pas_5, pas_6, pas_7);
                    passasjerer = 1;
                } else {
                    passasjerer = 0;
                }
            }
        });

        pas_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(pas_1, pas_3, pas_4, pas_5, pas_6, pas_7);
                    passasjerer = 2;
                } else {
                    passasjerer = 0;
                }
            }
        });

        pas_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(pas_1, pas_2, pas_4, pas_5, pas_6, pas_7);
                    passasjerer = 3;
                } else {
                    passasjerer = 0;
                }
            }
        });

        pas_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(pas_1, pas_2, pas_3, pas_5, pas_6, pas_7);
                    passasjerer = 4;
                } else {
                    passasjerer = 0;
                }
            }
        });

        pas_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(pas_1, pas_2, pas_3, pas_4, pas_6, pas_7);
                    passasjerer = 5;
                } else {
                    passasjerer = 0;
                }
            }
        });

        pas_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(pas_1, pas_2, pas_3, pas_4, pas_5, pas_7);
                    passasjerer = 6;
                } else {
                    passasjerer = 0;
                }
            }
        });

        pas_7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(pas_1, pas_2, pas_3, pas_4, pas_5, pas_6);
                    passasjerer = 7;
                } else {
                    passasjerer = 0;
                }
            }
        });

        timer_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(timer_2, timer_3, timer_4, timer_5);
                    timer_tid = 1;
                } else {
                    timer_tid = 0;
                }
            }
        });

        timer_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(timer_1, timer_3, timer_4, timer_5);
                    timer_tid = 2;
                } else {
                    timer_tid = 0;
                }
            }
        });

        timer_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(timer_1, timer_2, timer_4, timer_5);
                    timer_tid = 3;
                } else {
                    timer_tid = 0;
                }
            }
        });

        timer_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(timer_1, timer_2, timer_3, timer_5);
                    timer_tid = 4;
                } else {
                    timer_tid = 0;
                }
            }
        });

        timer_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(timer_1, timer_2, timer_3, timer_4);
                    timer_tid = 5;
                } else {
                    timer_tid = 0;
                }
            }
        });

        min_0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(min_15, min_30, min_45);
                    minutter_tid = 0;
                } else {
                    minutter_tid = -1;
                }
            }
        });

        min_15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(min_0, min_30, min_45);
                    minutter_tid = 15;
                } else {
                    minutter_tid = -1;
                }
            }
        });

        min_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(min_0, min_15, min_45);
                    minutter_tid = 30;
                } else {
                    minutter_tid = -1;
                }
            }
        });

        min_45.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Utilities.setUnChecked(min_0, min_15, min_30);
                    minutter_tid = 45;
                } else {
                    minutter_tid = -1;
                }
            }
        });

        final TextView title = getActivity().findViewById(R.id.chauffeur_title);
        final ConstraintLayout current_car = getActivity().findViewById(R.id.chauffeur_current_car);
        ImageView car_img = getActivity().findViewById(R.id.car_img); // TODO : Fix car image
        final Button registrate = getActivity().findViewById(R.id.chauffeur_registrate);
        final ConstraintLayout registrate_car_layout = getActivity().findViewById(R.id.chauffeur_add_car);
        final Button registrate_car = getActivity().findViewById(R.id.registrate_car);
        final ConstraintLayout legg_til_tid = getActivity().findViewById(R.id.chauffeur_legg_til_tid);
        final Button start_ny_tid = getActivity().findViewById(R.id.chauffeur_start);
        TextView ny_tid_title = view.findViewById(R.id.ny_tid_title);
        final ConstraintLayout timer_layout = getActivity().findViewById(R.id.chauffeur_timer_layout);

        registrate.setTypeface(MainActivity.typeface);
        title.setTypeface(MainActivity.typeface);

        if(Bruker.get().isHascar()) {
            registrate.setVisibility(View.GONE);
            registrate_car_layout.setVisibility(View.GONE);
            current_car.setVisibility(View.VISIBLE);
            setAfterRegistrated(title, legg_til_tid, start_ny_tid, timer_layout, registrate_car_layout, current_car);
        } else {
            title.setText("Registrer bil"); // TODO : Fix translation

            legg_til_tid.setVisibility(View.GONE);
            current_car.setVisibility(View.GONE);
            timer_layout.setVisibility(View.GONE);

            ny_tid_title.setVisibility(View.GONE);

            registrate.setVisibility(View.VISIBLE);

            registrate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AutoCompleteTextView merke = getActivity().findViewById(R.id.car_merke);
                    final TextInputLayout farge = getActivity().findViewById(R.id.car_farge);

                    merke.setTypeface(MainActivity.typeface);
                    farge.setTypeface(MainActivity.typeface);

                    if(!Bruker.get().isPremium()) {
                        registrate.setVisibility(View.GONE);

                        registrate_car_layout.setVisibility(View.VISIBLE);

                        merke.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, CarBrands.m_CarBrands));

                        registrate_car.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(farge.getEditText() != null && merke.getText().toString().length() > 2 && farge.getEditText().getText().toString().length() > 2 && CarBrands.doesListContain(merke.getText().toString())) {
                                    Location loc = Utilities.getLatLng(getActivity());
                                    StringRequest stringRequest = new StringRequest(Utilities.getGETMethodArgStr("create_chauffeur", "socketElem",
                                            Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT), "brukernavnElem", Bruker.get().getBrukernavn(), "carlistElem",
                                            new Gson().toJson(Collections.singletonList(new Car(merke.getText().toString(), farge.getEditText().getText().toString(), false))),
                                            "timeDrivingFrom", "0", "timeDrivingTo", "0", "rating", "0", "capacity", "0", "age",
                                            String.valueOf(Utilities.calcAge(new GregorianCalendar(Bruker.get().getYear(), Bruker.get().getMonth(), Bruker.get().getDay_of_month()))),
                                            "fornavn", Bruker.get().getFornavn(), "etternavn", Bruker.get().getEtternavn(), "longitude", loc == null ? "0" : String.valueOf(loc.getLongitude()), "latitude",
                                            loc == null ? "0" : String.valueOf(loc.getLatitude())), new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject respons = new JSONObject(response);
                                                if (respons.getInt("success") == 1) {
                                                    Bruker.get().setHascar(true);
                                                    Bruker.get().getChauffeur().setFornavn(Bruker.get().getFornavn());
                                                    Bruker.get().getChauffeur().setEtternavn(Bruker.get().getEtternavn());
                                                    Bruker.get().getChauffeur().setChauffeur_time_to(0);
                                                    Bruker.get().getChauffeur().setChauffeur_time_from(0);
                                                    Bruker.get().getChauffeur().addCar(new Car(merke.getText().toString(), farge.getEditText().getText().toString(), false));
                                                    Bruker.get().getChauffeur().setM_brukernavn(Bruker.get().getBrukernavn());
                                                    Bruker.get().getChauffeur().setM_age(Utilities.calcAge(new GregorianCalendar(Bruker.get().getYear(), Bruker.get().getMonth(), Bruker.get().getDay_of_month())));
                                                    Bruker.get().setCurrent_car(Bruker.get().getChauffeur().getListOfCars().get(0));
                                                    Bruker.get().getChauffeur().LagreChauffeur();
                                                    setAfterRegistrated(title, legg_til_tid, start_ny_tid, timer_layout, registrate_car_layout, current_car);
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
        }
    }

    private void setAfterRegistrated(TextView title, final ConstraintLayout legg_til_tid, final Button legg_til_tid_btn, final ConstraintLayout timer_layout, ConstraintLayout registrate_car_layout,
                                     ConstraintLayout current_car) {
        final Chauffeur brukerChauffeur = Bruker.get().getChauffeur();

        TextView navn = getActivity().findViewById(R.id.chauffeur_navn);
        TextView bil = getActivity().findViewById(R.id.chauffeur_bil);
        TextView plassering = getActivity().findViewById(R.id.chauffeur_plassering);

        final TextView ny_tid_title = getActivity().findViewById(R.id.ny_tid_title);
        final TextView antall_passasjerer = getActivity().findViewById(R.id.antall_passasjerer);
        final TextView timer = getActivity().findViewById(R.id.ny_tid_timer);
        final TextView minutter = getActivity().findViewById(R.id.ny_tid_minutter);
        final TextView time_progress = getActivity().findViewById(R.id.time_progress);
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

        registrate_car_layout.setVisibility(View.GONE);
        current_car.setVisibility(View.VISIBLE);
        ny_tid_title.setVisibility(View.VISIBLE);

        navn.setText(String.format(Locale.ENGLISH, "%s %s (%d)", Bruker.get().getFornavn(), Bruker.get().getEtternavn(), brukerChauffeur.getM_age()));
        bil.setText(String.format(Locale.ENGLISH, "%s %s", Bruker.get().getCurrent_car().getFarge(), Bruker.get().getCurrent_car().getMerke()));
        plassering.setText(String.format(Locale.ENGLISH, "%s", Bruker.get().getTown()));

        title.setText("Min bil"); // TODO : Fix translation

        legg_til_tid.setVisibility(View.VISIBLE);

        if(Bruker.get().getChauffeur().getChauffeur_time_from() == 0 && Bruker.get().getChauffeur().getChauffeur_time_to() == 0 || Bruker.get().getChauffeur().getChauffeur_time_to() < System.currentTimeMillis()) {
            ny_tid_title.setText("Start kjøreøkt");
            timer_layout.setVisibility(View.GONE);

            legg_til_tid_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(passasjerer == 0 || timer_tid == 0 || minutter_tid == -1)
                        return;

                    GregorianCalendar to = new GregorianCalendar();
                    to.setTimeInMillis(System.currentTimeMillis());

                    to.add(Calendar.HOUR_OF_DAY, timer_tid);

                    if(minutter_tid > 0)
                        to.add(Calendar.MINUTE, minutter_tid);

                    ChauffeurAddNewTime chauffeurAddNewTime = new ChauffeurAddNewTime(getActivity(), to.getTimeInMillis(), passasjerer);
                    chauffeurAddNewTime.execute();

                    /*
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
                     */
                }
            });
        } else if(Bruker.get().getChauffeur().getChauffeur_time_from() != 0 && Bruker.get().getChauffeur().getChauffeur_time_to() != 0) {
            legg_til_tid.setVisibility(View.GONE);

            timer_layout.setVisibility(View.VISIBLE);

            avslutt_tid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChauffeurRemoveTime chauffeurRemoveTime = new ChauffeurRemoveTime(getActivity());
                    chauffeurRemoveTime.execute();
                }
            });

            final long current = Bruker.get().getChauffeur().getChauffeur_time_to() - new Date().getTime();

            ny_tid_title.setText("Din økt");

            if(current > 1800000)
                time_progress.setTextColor(getResources().getColor(R.color.greentint));
            else
                time_progress.setTextColor(getResources().getColor(R.color.colorPrimary));

            countDownTimer = new CountDownTimer(current, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    GregorianCalendar timetoset = new GregorianCalendar();
                    timetoset.setTimeInMillis(millisUntilFinished);

                    if(millisUntilFinished < 1800000)
                        if(getActivity() != null)
                            time_progress.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));

                    time_progress.setText(String.format(Locale.ENGLISH, "%d:%02d:%02d", timetoset.get(Calendar.HOUR_OF_DAY), timetoset.get(Calendar.MINUTE),
                            timetoset.get(Calendar.SECOND)));
                }

                @Override
                public void onFinish() {
                    Bruker.get().getChauffeur().setChauffeur_time_to(0);
                    Bruker.get().getChauffeur().setChauffeur_time_from(0);
                    Bruker.get().getChauffeur().setM_capacity(0);
                    Bruker.get().getChauffeur().LagreChauffeur();

                    ny_tid_title.setText("Start kjøreøkt");
                    legg_til_tid.setVisibility(View.VISIBLE);
                    timer_layout.setVisibility(View.GONE);
                }
            };

            countDownTimer.start();
        }
    }
}
