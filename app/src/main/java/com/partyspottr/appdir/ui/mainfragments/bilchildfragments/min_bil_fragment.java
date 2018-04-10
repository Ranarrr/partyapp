package com.partyspottr.appdir.ui.mainfragments.bilchildfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Car;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.ui.MainActivity;

import java.util.GregorianCalendar;

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
        final Button legg_til_tid_btn = getActivity().findViewById(R.id.legg_til_tid_btn);
        final EditText time_from = getActivity().findViewById(R.id.time_from);
        final EditText time_to = getActivity().findViewById(R.id.time_to);

        time_from.setTypeface(MainActivity.typeface);
        time_to.setTypeface(MainActivity.typeface);
        legg_til_tid_btn.setTypeface(MainActivity.typeface);
        registrate.setTypeface(MainActivity.typeface);
        img_add.setTypeface(MainActivity.typeface);
        title.setTypeface(MainActivity.typeface);

        if(Bruker.get().isHascar()) {
            title.setText("Min bil"); // TODO : Fix translation

            current_car.setVisibility(View.VISIBLE);

            if(Bruker.get().getCurrent_car().isHasImg()) {
                img_add.setText("Endre bilde"); // TODO : Fix translation
            } else {
                img_add.setText("Legg til bilde");
            }



            legg_til_tid.setVisibility(View.VISIBLE);

            legg_til_tid_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GregorianCalendar from, to;

                    from = Utilities.getDateFromString(time_from.getText().toString(), "HH:mm");

                    if(!time_from.getText().toString().isEmpty() && !time_to.getText().toString().isEmpty()) {

                    }
                }
            });
        } else {
            title.setText("Registrer bil"); // TODO : Fix translation

            registrate.setVisibility(View.VISIBLE);

            registrate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registrate.setVisibility(View.INVISIBLE);

                    registrate_car_layout.setVisibility(View.VISIBLE);

                    registrate_car.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextInputLayout merke = getActivity().findViewById(R.id.car_merke);
                            TextInputLayout modell = getActivity().findViewById(R.id.car_modell);
                            TextInputLayout farge = getActivity().findViewById(R.id.car_farge);

                            merke.setTypeface(MainActivity.typeface);
                            modell.setTypeface(MainActivity.typeface);
                            farge.setTypeface(MainActivity.typeface);

                            if(merke.getEditText() != null && modell.getEditText() != null && farge.getEditText() != null && merke.getEditText().getText().toString().length() > 2 &&
                                    modell.getEditText().getText().toString().length() >= 2 && farge.getEditText().getText().toString().length() > 2) {
                                Bruker.get().setHascar(true);
                                Bruker.get().addCar(new Car(merke.getEditText().getText().toString(), modell.getEditText().getText().toString(), farge.getEditText().getText().toString(), false));

                                registrate_car_layout.setVisibility(View.INVISIBLE);

                                title.setText("Min bil"); // TODO : Fix translation

                                current_car.setVisibility(View.VISIBLE);

                                if(Bruker.get().getCurrent_car().isHasImg()) {
                                    img_add.setText("Endre bilde"); // TODO : Fix translation
                                } else {
                                    img_add.setText("Legg til bilde");
                                }

                                legg_til_tid.setVisibility(View.VISIBLE);

                                legg_til_tid_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        GregorianCalendar from, to;

                                        from = Utilities.getDateFromString(time_from.getText().toString(), "HH:mm");
                                        to = Utilities.getDateFromString(time_to.getText().toString(), "HH:mm");

                                        if(from != null && to != null) {
                                            if(!to.before(from)) {

                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }
}
