package com.partyspottr.appdir.ui.registerui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.networking.CheckUsername;

public class Register3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        EditText brukerNavn = findViewById(R.id.brukernavnText);
        EditText passord = findViewById(R.id.passordText);

        if(Bruker.get().getBrukernavn() != null && !Bruker.get().getBrukernavn().isEmpty()) {
            brukerNavn.setText(Bruker.get().getBrukernavn());
            passord.setText(Bruker.get().getPassord());
        }
    }

    public void onClickFortsettBtn(View v) {
        EditText brukerNavn = findViewById(R.id.brukernavnText);
        EditText passord = findViewById(R.id.passordText);

        if(passord.length() > 6) {
            Bruker.get().setPassord(passord.getText().toString());
            ViewCompat.setBackgroundTintList(passord, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));

            if(brukerNavn.length() > 1) {
                CheckUsername checkUsername = new CheckUsername(brukerNavn.getText().toString(), this);
                checkUsername.execute();
            } else {
                ViewCompat.setBackgroundTintList(brukerNavn, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.vennligst_6_minimum_passord), Toast.LENGTH_LONG).show();
            ViewCompat.setBackgroundTintList(passord, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
        }
    }
}
