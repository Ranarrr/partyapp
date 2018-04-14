package com.partyspottr.appdir.ui.registerui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.enums.måneder;
import com.partyspottr.appdir.ui.MainActivity;

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

        EditText fornavnText = findViewById(R.id.fornavnText);
        EditText etternavnText = findViewById(R.id.etternavnText);
        EditText ddNum = findViewById(R.id.ddNum);
        EditText mmNum = findViewById(R.id.mmNum);
        EditText yyyyNum = findViewById(R.id.ååååNum);
        TextView name = findViewById(R.id.textView2);
        TextView birth = findViewById(R.id.textView);
        TextView title = findViewById(R.id.textView3);
        Button continuebtn = findViewById(R.id.button);

        fornavnText.setTypeface(MainActivity.typeface);
        etternavnText.setTypeface(MainActivity.typeface);
        ddNum.setTypeface(MainActivity.typeface);
        mmNum.setTypeface(MainActivity.typeface);
        yyyyNum.setTypeface(MainActivity.typeface);
        name.setTypeface(MainActivity.typeface);
        birth.setTypeface(MainActivity.typeface);
        title.setTypeface(MainActivity.typeface);
        continuebtn.setTypeface(MainActivity.typeface);

        if(Bruker.get().getFornavn() != null && !Bruker.get().getFornavn().isEmpty()) {
            fornavnText.setText(Bruker.get().getFornavn());
            etternavnText.setText(Bruker.get().getEtternavn());

            ddNum.setText(String.valueOf(Bruker.get().getDay_of_month()));
            mmNum.setText(String.valueOf(Bruker.get().getMonth()));
            yyyyNum.setText(String.valueOf(Bruker.get().getYear()));
        }
    }

    public void onClickRegistrerBtn(View v) {
        EditText fornavnText = findViewById(R.id.fornavnText);
        EditText etternavnText = findViewById(R.id.etternavnText);

        EditText ddNum = findViewById(R.id.ddNum);
        EditText mmNum = findViewById(R.id.mmNum);
        EditText yyyyNum = findViewById(R.id.ååååNum);
        Integer dagNum = 0, mndNum = 0, yrNum = 0;

        if(ddNum.getText() != null && ddNum.length() > 0) {
            dagNum = Integer.valueOf(ddNum.getText().toString());
        }

        if(mmNum.getText() != null && mmNum.length() > 0) {
            mndNum = Integer.valueOf(mmNum.getText().toString());
        }

        if(yyyyNum.getText() != null && yyyyNum.length() > 0) {
            yrNum = Integer.valueOf(yyyyNum.getText().toString());
        }

        if(dagNum > 0 && dagNum < 32 || (dagNum == 31 && (mndNum == måneder.JANUAR.ordinal() || mndNum == måneder.MARS.ordinal() || mndNum == måneder.MAI.ordinal()
                || mndNum == måneder.JULI.ordinal() || mndNum == måneder.AUGUST.ordinal() || mndNum == måneder.OKTOBER.ordinal() || mndNum == måneder.DESEMBER.ordinal()))
                || (dagNum > 28 && dagNum < 32 && mndNum != måneder.FEBRUAR.ordinal())) {
            ViewCompat.setBackgroundTintList(ddNum, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));

            if(mndNum > 0 && mndNum < 13) {
                ViewCompat.setBackgroundTintList(mmNum, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));

                if(yrNum > 1929 && yrNum < 2002) {
                    Bruker.get().setDOB(dagNum, mndNum, yrNum);
                    ViewCompat.setBackgroundTintList(yyyyNum, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));

                    if(fornavnText.length() > 1) {
                        ViewCompat.setBackgroundTintList(fornavnText, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));

                        if(etternavnText.length() > 1) {
                            ViewCompat.setBackgroundTintList(etternavnText, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));
                            Bruker.get().setHarakseptert(true);
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
                } else {
                    ViewCompat.setBackgroundTintList(yyyyNum, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
                }
            } else {
                ViewCompat.setBackgroundTintList(mmNum, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
            }
        } else {
            ViewCompat.setBackgroundTintList(ddNum, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
        }
    }
}