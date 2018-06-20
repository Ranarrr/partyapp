package com.partyspottr.appdir.ui.registerui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.ui.MainActivity;

import java.util.Locale;

/**
 * Created by Ranarrr on 26-Jan-18.
 *
 * @author Ranarrr
 */

public class Register4Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);

        TextView nameText = findViewById(R.id.nameTextVal);
        TextView DOBText = findViewById(R.id.DOBTextVal);
        TextView emailText = findViewById(R.id.emailTextVal);
        TextView usernameText = findViewById(R.id.usernameTextVal);
        TextView passwordText = findViewById(R.id.passwordTextVal);
        TextView title = findViewById(R.id.textView3);
        Button continuebtn = findViewById(R.id.button5);

        continuebtn.setTypeface(MainActivity.typeface);
        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register4Activity.this, Register5Activity.class);
                startActivity(intent);
            }
        });

        Button changename = findViewById(R.id.changeName);
        Button changedob = findViewById(R.id.changeDOB);
        Button changeemail = findViewById(R.id.changeEmail);
        Button changeuser = findViewById(R.id.changeUsername);
        Button changepass = findViewById(R.id.changePassword);

        changename.setTypeface(MainActivity.typeface);
        changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register4Activity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        changedob.setTypeface(MainActivity.typeface);
        changedob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register4Activity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        changeemail.setTypeface(MainActivity.typeface);
        changeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register4Activity.this, Register2Activity.class);
                startActivity(intent);
            }
        });

        changeuser.setTypeface(MainActivity.typeface);
        changeuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register4Activity.this, Register3Activity.class);
                startActivity(intent);
            }
        });

        changepass.setTypeface(MainActivity.typeface);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register4Activity.this, Register3Activity.class);
                startActivity(intent);
            }
        });

        nameText.setTypeface(MainActivity.typeface);
        DOBText.setTypeface(MainActivity.typeface);
        emailText.setTypeface(MainActivity.typeface);
        usernameText.setTypeface(MainActivity.typeface);
        passwordText.setTypeface(MainActivity.typeface);
        title.setTypeface(MainActivity.typeface);

        nameText.setText(String.format("%s %s", Bruker.get().getFornavn(), Bruker.get().getEtternavn()));
        DOBText.setText(String.format(Locale.ENGLISH, "%d.%d.%d", Bruker.get().getDay_of_month(), Bruker.get().getMonth(), Bruker.get().getYear()));
        emailText.setText(Bruker.get().getEmail());
        usernameText.setText(Bruker.get().getBrukernavn());
        String pass = Bruker.get().getPassord();

        String substrin = pass.substring(3, pass.length() - 3);
        StringBuilder replacement = new StringBuilder();

        for(int i = 0; i < substrin.length(); i++) {
            replacement.append("•");
        }

        passwordText.setText(pass.replace(substrin, replacement.toString()));
    }
}