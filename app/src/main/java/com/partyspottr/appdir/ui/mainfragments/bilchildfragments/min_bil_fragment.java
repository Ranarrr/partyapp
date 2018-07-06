package com.partyspottr.appdir.ui.mainfragments.bilchildfragments;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Car;
import com.partyspottr.appdir.classes.Chauffeur;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.CarBrands;
import com.partyspottr.appdir.ui.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ranarrr on 30-Jan-18.
 *
 * @author Ranarrr
 */

public class min_bil_fragment extends Fragment {
    public static CountDownTimer countDownTimer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.chauffeur_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView title = view.findViewById(R.id.chauffeur_title);
        final ConstraintLayout current_car = view.findViewById(R.id.chauffeur_current_car);
        ImageView car_img = view.findViewById(R.id.car_img); // TODO : Fix car image
        final Button registrate = view.findViewById(R.id.chauffeur_registrate);
        final ConstraintLayout registrate_car_layout = view.findViewById(R.id.chauffeur_add_car);
        final Button registrate_car = view.findViewById(R.id.registrate_car);
        final ConstraintLayout legg_til_tid = view.findViewById(R.id.chauffeur_legg_til_tid);
        final Button start_ny_tid = view.findViewById(R.id.chauffeur_start);
        TextView ny_tid_title = view.findViewById(R.id.ny_tid_title);
        final ConstraintLayout timer_layout = view.findViewById(R.id.chauffeur_timer_layout);

        if(getActivity() == null)
            return;

        Spinner passasjerer = view.findViewById(R.id.passasjer_spinner);
        ArrayAdapter<Integer> passasjer_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_mine, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        passasjerer.setAdapter(passasjer_adapter);

        Spinner timer = view.findViewById(R.id.timer_spinner);
        ArrayAdapter<Integer> timer_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_mine, Arrays.asList(1, 2, 3, 4, 5));
        timer.setAdapter(timer_adapter);

        Spinner minutter = view.findViewById(R.id.minutter_spinner);
        ArrayAdapter<Integer> minutter_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_mine, Arrays.asList(0, 15, 30, 45));
        minutter.setAdapter(minutter_adapter);

        Spinner biler = view.findViewById(R.id.bil_spinner);
        List<String> listcars = new ArrayList<>();

        for(Car car : Bruker.get().getChauffeur().getListOfCars()) {
            listcars.add(String.format(Locale.ENGLISH, "%s %s", car.getFarge(), car.getMerke()));
        }

        ArrayAdapter<String> biler_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_mine, listcars);
        biler.setAdapter(biler_adapter);

        registrate.setTypeface(MainActivity.typeface);
        title.setTypeface(MainActivity.typeface);

        if(Bruker.get().isHascar()) {
            registrate.setVisibility(View.GONE);
            registrate_car_layout.setVisibility(View.GONE);
            current_car.setVisibility(View.VISIBLE);
            setAfterRegistrated(title, legg_til_tid, start_ny_tid, timer_layout, registrate_car_layout, current_car, view);
        } else {
            title.setText(getString(R.string.register_car));

            legg_til_tid.setVisibility(View.GONE);
            current_car.setVisibility(View.GONE);
            timer_layout.setVisibility(View.GONE);

            ny_tid_title.setVisibility(View.GONE);

            registrate.setVisibility(View.VISIBLE);

            registrate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AutoCompleteTextView merke = getActivity().findViewById(R.id.car_merke);
                    final AppCompatAutoCompleteTextView farge = getActivity().findViewById(R.id.car_farge);

                    merke.setTypeface(MainActivity.typeface);
                    farge.setTypeface(MainActivity.typeface);

                    if(!Bruker.get().isPremium()) {
                        registrate.setVisibility(View.GONE);

                        registrate_car_layout.setVisibility(View.VISIBLE);

                        merke.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, CarBrands.m_CarBrands));

                        registrate_car.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(merke.getText().toString().length() > 2 && farge.getText().toString().length() > 2 && CarBrands.doesListContain(merke.getText().toString())) {
                                    final Location loc = Utilities.getLatLng(getActivity());

                                    final DatabaseReference chauffeurref = FirebaseDatabase.getInstance().getReference("chauffeurs").child(Bruker.get().getBrukernavn());

                                    chauffeurref.child("brukernavn").setValue(Bruker.get().getBrukernavn()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            chauffeurref.child(Chauffeur.carlistElem).setValue(new Gson().toJson(Collections.singletonList(new Car(merke.getText().toString(), farge.getText().toString(),
                                                    false))));
                                            chauffeurref.child(Chauffeur.time_fromElem).setValue(0);
                                            chauffeurref.child(Chauffeur.time_toElem).setValue(0);
                                            chauffeurref.child(Chauffeur.rating).setValue(0);
                                            chauffeurref.child(Chauffeur.capacity).setValue(0);
                                            chauffeurref.child(Chauffeur.age).setValue(String.valueOf(Utilities.calcAge(new GregorianCalendar(Bruker.get().getYear(), Bruker.get().getMonth(), Bruker.get().getDay_of_month()))));
                                            chauffeurref.child(Chauffeur.fornavnElem).setValue(Bruker.get().getFornavn());
                                            chauffeurref.child(Chauffeur.etternavnElem).setValue(Bruker.get().getEtternavn());
                                            chauffeurref.child(Chauffeur.longitudeElem).setValue(loc == null ? "0" : String.valueOf(loc.getLongitude()));
                                            chauffeurref.child(Chauffeur.latitudeElem).setValue(loc == null ? "0" : String.valueOf(loc.getLatitude()));

                                            Bruker.get().setHascar(true);
                                            Bruker.get().getChauffeur().setFornavn(Bruker.get().getFornavn());
                                            Bruker.get().getChauffeur().setEtternavn(Bruker.get().getEtternavn());
                                            Bruker.get().getChauffeur().setChauffeur_time_to(0);
                                            Bruker.get().getChauffeur().setChauffeur_time_from(0);
                                            Bruker.get().getChauffeur().addCar(new Car(merke.getText().toString(), farge.getText().toString(), false));
                                            Bruker.get().getChauffeur().setM_brukernavn(Bruker.get().getBrukernavn());
                                            Bruker.get().getChauffeur().setM_age(Utilities.calcAge(new GregorianCalendar(Bruker.get().getYear(), Bruker.get().getMonth(), Bruker.get().getDay_of_month())));
                                            Bruker.get().setCurrent_car(Bruker.get().getChauffeur().getListOfCars().get(0));
                                            Bruker.get().getChauffeur().LagreChauffeur();
                                            Bruker.get().LagreBruker();
                                            setAfterRegistrated(title, legg_til_tid, start_ny_tid, timer_layout, registrate_car_layout, current_car, view);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if(getActivity() != null)
                                                Toast.makeText(getActivity(), getString(R.string.failed_registrating_car), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        new AlertDialog.Builder(getActivity(), R.style.mydatepickerdialog)
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
                                     ConstraintLayout current_car, @NonNull final View view) {
        final Chauffeur brukerChauffeur = Bruker.get().getChauffeur();

        TextView navn = view.findViewById(R.id.chauffeur_navn);
        TextView bil = view.findViewById(R.id.chauffeur_bil);
        TextView plassering = view.findViewById(R.id.chauffeur_plassering);

        final TextView ny_tid_title = view.findViewById(R.id.ny_tid_title);
        final TextView antall_passasjerer = view.findViewById(R.id.antall_passasjerer);
        final TextView timer = view.findViewById(R.id.ny_tid_timer);
        final TextView minutter = view.findViewById(R.id.ny_tid_minutter);
        final TextView time_progress = view.findViewById(R.id.time_progress);
        final Button avslutt_tid = view.findViewById(R.id.chauffeur_avslutt);

        avslutt_tid.setTypeface(MainActivity.typeface);
        ny_tid_title.setTypeface(MainActivity.typeface);
        antall_passasjerer.setTypeface(MainActivity.typeface);
        timer.setTypeface(MainActivity.typeface);
        minutter.setTypeface(MainActivity.typeface);
        navn.setTypeface(MainActivity.typeface);
        bil.setTypeface(MainActivity.typeface);
        plassering.setTypeface(MainActivity.typeface);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatSpinner timer_tid = view.findViewById(R.id.timer_spinner);
                AppCompatSpinner minutter_tid = view.findViewById(R.id.minutter_spinner);
                final AppCompatSpinner passasjerer = view.findViewById(R.id.passasjer_spinner);

                final GregorianCalendar to = new GregorianCalendar();
                to.setTimeInMillis(System.currentTimeMillis());

                to.add(Calendar.HOUR_OF_DAY, (int) timer_tid.getSelectedItem());

                if ((int) minutter_tid.getSelectedItem() > 0)
                    to.add(Calendar.MINUTE, (int) minutter_tid.getSelectedItem());

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chauffeurs").child(Bruker.get().getBrukernavn());
                ref.child(Chauffeur.time_fromElem).setValue(System.currentTimeMillis()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ref.child(Chauffeur.time_toElem).setValue(to.getTimeInMillis());
                        ref.child(Chauffeur.capacity).setValue(passasjerer.getSelectedItem());

                        Bruker.get().getChauffeur().setChauffeur_time_from(System.currentTimeMillis());
                        Bruker.get().getChauffeur().setChauffeur_time_to(to.getTimeInMillis());
                        Bruker.get().getChauffeur().setM_capacity(((int) passasjerer.getSelectedItem()));
                        Bruker.get().getChauffeur().LagreChauffeur();

                        final ConstraintLayout legg_til_tid_layout = view.findViewById(R.id.chauffeur_legg_til_tid);
                        final TextView time_progress = view.findViewById(R.id.time_progress);
                        final TextView ny_tid_title = view.findViewById(R.id.ny_tid_title);
                        final ConstraintLayout timer_layout = view.findViewById(R.id.chauffeur_timer_layout);

                        if (legg_til_tid_layout != null && time_progress != null && ny_tid_title != null && timer_layout != null) {
                            legg_til_tid_layout.setVisibility(View.GONE);
                            timer_layout.setVisibility(View.VISIBLE);
                            ny_tid_title.setText(getString(R.string.your_session));

                            final long current = Bruker.get().getChauffeur().getChauffeur_time_to() - new Date().getTime();

                            min_bil_fragment.countDownTimer = new CountDownTimer(current, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    GregorianCalendar timetoset = new GregorianCalendar();
                                    timetoset.setTimeInMillis(millisUntilFinished);

                                    time_progress.setText(String.format(Locale.ENGLISH, "%d:%02d:%02d", timetoset.get(Calendar.HOUR_OF_DAY) - 1, timetoset.get(Calendar.MINUTE),
                                            timetoset.get(Calendar.SECOND)));
                                }

                                @Override
                                public void onFinish() {
                                    legg_til_tid_layout.setVisibility(View.VISIBLE);
                                    timer_layout.setVisibility(View.GONE);
                                    ny_tid_title.setText(getString(R.string.start_new_time));
                                }
                            };

                            min_bil_fragment.countDownTimer.start();
                        }
                    }
                });
            }
        };

        registrate_car_layout.setVisibility(View.GONE);
        current_car.setVisibility(View.VISIBLE);
        ny_tid_title.setVisibility(View.VISIBLE);

        navn.setText(String.format(Locale.ENGLISH, "%s %s (%d)", Bruker.get().getFornavn(), Bruker.get().getEtternavn(), brukerChauffeur.getM_age()));
        bil.setText(String.format(Locale.ENGLISH, "%s %s", Bruker.get().getCurrent_car().getFarge(), Bruker.get().getCurrent_car().getMerke()));
        plassering.setText(String.format(Locale.ENGLISH, "%s", Bruker.get().getTown()));

        title.setText(getString(R.string.my_car));

        legg_til_tid.setVisibility(View.VISIBLE);

        if(Bruker.get().getChauffeur().getChauffeur_time_from() == 0 && Bruker.get().getChauffeur().getChauffeur_time_to() == 0 || Bruker.get().getChauffeur().getChauffeur_time_to() < System.currentTimeMillis()) {
            ny_tid_title.setText(getString(R.string.start_new_time));
            timer_layout.setVisibility(View.GONE);

            legg_til_tid_btn.setOnClickListener(onClickListener);

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
                //}
            //});
        } else if(Bruker.get().getChauffeur().getChauffeur_time_from() != 0 && Bruker.get().getChauffeur().getChauffeur_time_to() != 0) {
            legg_til_tid.setVisibility(View.GONE);

            timer_layout.setVisibility(View.VISIBLE);

            avslutt_tid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chauffeurs").child(Bruker.get().getBrukernavn());

                    ref.child(Chauffeur.time_toElem).setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ref.child(Chauffeur.time_fromElem).setValue(0);
                            ref.child(Chauffeur.capacity).setValue(0);

                            TextView ny_tid_title = view.findViewById(R.id.ny_tid_title);
                            ConstraintLayout legg_til_tid = view.findViewById(R.id.chauffeur_legg_til_tid);
                            ConstraintLayout timer_layout = view.findViewById(R.id.chauffeur_timer_layout);
                            final Button start_ny_tid = view.findViewById(R.id.chauffeur_start);

                            min_bil_fragment.countDownTimer.cancel();

                            ny_tid_title.setText(getString(R.string.start_new_time));
                            legg_til_tid.setVisibility(View.VISIBLE);
                            timer_layout.setVisibility(View.GONE);

                            start_ny_tid.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO: DO IT
                                }
                            });
                        }
                    });
                }
            });

            final long current = Bruker.get().getChauffeur().getChauffeur_time_to() - new Date().getTime();

            ny_tid_title.setText(getString(R.string.your_session));

            countDownTimer = new CountDownTimer(current, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    GregorianCalendar timetoset = new GregorianCalendar();
                    timetoset.setTimeInMillis(millisUntilFinished);

                    time_progress.setText(String.format(Locale.ENGLISH, "%d:%02d:%02d", timetoset.get(Calendar.HOUR_OF_DAY) - 1, timetoset.get(Calendar.MINUTE),
                            timetoset.get(Calendar.SECOND)));
                }

                @Override
                public void onFinish() {
                    Bruker.get().getChauffeur().setChauffeur_time_to(0);
                    Bruker.get().getChauffeur().setChauffeur_time_from(0);
                    Bruker.get().getChauffeur().setM_capacity(0);
                    Bruker.get().getChauffeur().LagreChauffeur();

                    ny_tid_title.setText(getString(R.string.start_new_time));
                    legg_til_tid.setVisibility(View.VISIBLE);
                    timer_layout.setVisibility(View.GONE);
                }
            };

            countDownTimer.start();
        }
    }
}
