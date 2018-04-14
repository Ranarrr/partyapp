package com.partyspottr.appdir.ui.registerui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.networking.CheckUsername;
import com.partyspottr.appdir.ui.MainActivity;

/**
 * Created by Ranarrr on 26-Jan-18.
 *
 * @author Ranarrr
 */

public class Register3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        EditText brukerNavn = findViewById(R.id.brukernavnText);
        EditText passord = findViewById(R.id.passordText);
        TextView title = findViewById(R.id.textView3);
        TextView header = findViewById(R.id.textView4);
        TextView header2 = findViewById(R.id.textView5);
        Button continuebtn = findViewById(R.id.fortsettBtn2);

        brukerNavn.setTypeface(MainActivity.typeface);
        passord.setTypeface(MainActivity.typeface);
        title.setTypeface(MainActivity.typeface);
        header.setTypeface(MainActivity.typeface);
        header2.setTypeface(MainActivity.typeface);
        continuebtn.setTypeface(MainActivity.typeface);

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
