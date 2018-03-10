package com.partyspottr.appdir.ui.registerui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.enums.måneder;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(Bruker.get().getFornavn() != null && !Bruker.get().getFornavn().isEmpty()) {
            EditText fornavnText = findViewById(R.id.fornavnText);
            EditText etternavnText = findViewById(R.id.etternavnText);
            EditText ddNum = findViewById(R.id.ddNum);
            EditText mmNum = findViewById(R.id.mmNum);
            EditText ååååNum = findViewById(R.id.ååååNum);

            fornavnText.setText(Bruker.get().getFornavn());
            etternavnText.setText(Bruker.get().getEtternavn());

            ddNum.setText(String.valueOf(Bruker.get().getDay_of_month()));
            mmNum.setText(String.valueOf(Bruker.get().getMonth()));
            ååååNum.setText(String.valueOf(Bruker.get().getYear()));
        }
    }

    public void onClickRegistrerBtn(View v) {
        EditText fornavnText = findViewById(R.id.fornavnText);
        EditText etternavnText = findViewById(R.id.etternavnText);

        EditText ddNum = findViewById(R.id.ddNum);
        EditText mmNum = findViewById(R.id.mmNum);
        EditText ååååNum = findViewById(R.id.ååååNum);
        Integer dagNum = 0, mndNum = 0, årNum = 0;

        if(ddNum.getText() != null && ddNum.length() > 0) {
            dagNum = Integer.valueOf(ddNum.getText().toString());
        }

        if(mmNum.getText() != null && mmNum.length() > 0) {
            mndNum = Integer.valueOf(mmNum.getText().toString());
        }

        if(ååååNum.getText() != null && ååååNum.length() > 0) {
            årNum = Integer.valueOf(ååååNum.getText().toString());
        }

        if(dagNum > 0 && dagNum < 32 || (dagNum == 31 && (mndNum == måneder.JANUAR.ordinal() || mndNum == måneder.MARS.ordinal() || mndNum == måneder.MAI.ordinal()
                || mndNum == måneder.JULI.ordinal() || mndNum == måneder.AUGUST.ordinal() || mndNum == måneder.OKTOBER.ordinal() || mndNum == måneder.DESEMBER.ordinal()))
                || (dagNum > 28 && dagNum < 32 && mndNum != måneder.FEBRUAR.ordinal())) {
            ViewCompat.setBackgroundTintList(ddNum, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));

            if(mndNum > 0 && mndNum < 13) {
                ViewCompat.setBackgroundTintList(mmNum, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));

                if(årNum > 1929 && årNum < 2002) {
                    Bruker.get().setDOB(dagNum, mndNum, årNum);
                    ViewCompat.setBackgroundTintList(ååååNum, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));

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
                    ViewCompat.setBackgroundTintList(ååååNum, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
                }
            } else {
                ViewCompat.setBackgroundTintList(mmNum, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
            }
        } else {
            ViewCompat.setBackgroundTintList(ddNum, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
        }
    }
}