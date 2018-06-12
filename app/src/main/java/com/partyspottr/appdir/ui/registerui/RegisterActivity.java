package com.partyspottr.appdir.ui.registerui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.enums.måneder;
import com.partyspottr.appdir.ui.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Ranarrr on 26-Jan-18.
 *
 * @author Ranarrr
 */

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Spinner dd_spinner = findViewById(R.id.dd_spinner);
        final Spinner mm_spinner = findViewById(R.id.mm_spinner);
        final Spinner yyyy_spinner = findViewById(R.id.yyyy_spinner);

        final EditText fornavnText = findViewById(R.id.fornavnText);
        final EditText etternavnText = findViewById(R.id.etternavnText);
        TextView name = findViewById(R.id.textView2);
        TextView birth = findViewById(R.id.textView);
        TextView title = findViewById(R.id.textView3);
        Button continuebtn = findViewById(R.id.button);

        ArrayAdapter<Integer> mm_adapter = new ArrayAdapter<>(this, R.layout.spinner_mine, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));

        List<Integer> list = new ArrayList<>();

        for(int i = Calendar.getInstance().get(Calendar.YEAR); i > Calendar.getInstance().get(Calendar.YEAR) - 99; i--) {
            list.add(i);
        }

        ArrayAdapter<Integer> yyyy_adapter = new ArrayAdapter<>(this, R.layout.spinner_mine, list);

        dd_spinner.setAdapter(new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_mine, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31)));
        mm_spinner.setAdapter(mm_adapter);
        yyyy_spinner.setAdapter(yyyy_adapter);

        mm_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int mndNum = (int) mm_spinner.getSelectedItem();
                int ddNum = (int) dd_spinner.getSelectedItem();
                int yyyyNum = (int) yyyy_spinner.getSelectedItem();

                if(mndNum == måneder.JANUAR.ordinal() || mndNum == måneder.MARS.ordinal() || mndNum == måneder.MAI.ordinal() || mndNum == måneder.JULI.ordinal() || mndNum == måneder.AUGUST.ordinal()
                        || mndNum == måneder.OKTOBER.ordinal() || mndNum == måneder.DESEMBER.ordinal()) {
                    dd_spinner.setAdapter(new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_mine, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31)));

                    dd_spinner.setSelection(ddNum - 1);

                } else if(mndNum == måneder.APRIL.ordinal() || mndNum == måneder.JUNI.ordinal() || mndNum == måneder.SEPTEMBER.ordinal() || mndNum == måneder.NOVEMBER.ordinal()) {
                    dd_spinner.setAdapter(new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_mine, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30)));

                    if(ddNum > 30)
                        dd_spinner.setSelection(29);
                    else
                        dd_spinner.setSelection(ddNum - 1);

                } else if(mndNum == måneder.FEBRUAR.ordinal() && new GregorianCalendar().isLeapYear(yyyyNum)) {
                    dd_spinner.setAdapter(new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_mine, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29)));

                    if(ddNum > 29)
                        dd_spinner.setSelection(28);
                    else
                        dd_spinner.setSelection(ddNum - 1);

                } else if(mndNum == måneder.FEBRUAR.ordinal() && !new GregorianCalendar().isLeapYear(yyyyNum)) {
                    dd_spinner.setAdapter(new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_mine, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28)));

                    if(ddNum > 28)
                        dd_spinner.setSelection(27);
                    else
                        dd_spinner.setSelection(ddNum - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        yyyy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int ddNum = (int) dd_spinner.getSelectedItem();
                int mndNum = (int) mm_spinner.getSelectedItem();
                int yyyyNum = (int) yyyy_spinner.getSelectedItem();

                if(mndNum == måneder.FEBRUAR.ordinal() && new GregorianCalendar().isLeapYear(yyyyNum)) {
                    dd_spinner.setAdapter(new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_mine, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29)));

                    if(ddNum > 29)
                        dd_spinner.setSelection(28);
                    else
                        dd_spinner.setSelection(ddNum - 1);

                } else if(mndNum == måneder.FEBRUAR.ordinal() && !new GregorianCalendar().isLeapYear(yyyyNum)) {
                    dd_spinner.setAdapter(new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_mine, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28)));

                    if(ddNum > 28)
                        dd_spinner.setSelection(27);
                    else
                        dd_spinner.setSelection(ddNum - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        fornavnText.setTypeface(MainActivity.typeface);
        etternavnText.setTypeface(MainActivity.typeface);
        name.setTypeface(MainActivity.typeface);
        birth.setTypeface(MainActivity.typeface);
        title.setTypeface(MainActivity.typeface);
        continuebtn.setTypeface(MainActivity.typeface);

        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dagNum = (int) dd_spinner.getSelectedItem();
                int mndNum = (int) mm_spinner.getSelectedItem();
                int yyyyNum = (int) yyyy_spinner.getSelectedItem();

                Bruker.get().setDOB(dagNum, mndNum, yyyyNum);

                if(fornavnText.length() > 1) {
                    ViewCompat.setBackgroundTintList(fornavnText, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));

                    if(etternavnText.length() > 1) {
                        ViewCompat.setBackgroundTintList(etternavnText, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));

                        Bruker.get().setFornavn(fornavnText.getText().toString());
                        Bruker.get().setEtternavn(etternavnText.getText().toString());

                        Intent intent = new Intent(getApplicationContext(), Register2Activity.class);
                        startActivity(intent);

                    } else {
                        ViewCompat.setBackgroundTintList(etternavnText, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
                    }
                } else {
                    ViewCompat.setBackgroundTintList(fornavnText, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
                }
            }
        });

        if(Bruker.get().getFornavn() != null && !Bruker.get().getFornavn().isEmpty()) {
            fornavnText.setText(Bruker.get().getFornavn());
            etternavnText.setText(Bruker.get().getEtternavn());

            mm_spinner.setSelection(Bruker.get().getMonth() - 1);
            dd_spinner.setSelection(Bruker.get().getDay_of_month() - 1);

            for(int i = 0; i < yyyy_adapter.getCount(); i++) {
                if(yyyy_adapter.getItem(i).equals(Bruker.get().getYear()))
                    yyyy_spinner.setSelection(i);
            }
        }
    }
}