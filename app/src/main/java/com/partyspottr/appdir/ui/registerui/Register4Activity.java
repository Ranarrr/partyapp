package com.partyspottr.appdir.ui.registerui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;

import java.util.Locale;

public class Register4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);

        TextView nameText = findViewById(R.id.nameTextVal);
        TextView DOBText = findViewById(R.id.DOBTextVal);
        TextView phoneText = findViewById(R.id.phoneTextVal);
        TextView emailText = findViewById(R.id.emailTextVal);
        TextView usernameText = findViewById(R.id.usernameTextVal);
        TextView passwordText = findViewById(R.id.passwordTextVal);

        nameText.setText(String.format("%s %s", Bruker.get().getFornavn(), Bruker.get().getEtternavn()));
        DOBText.setText(String.format(Locale.ENGLISH, "%d.%d.%d", Bruker.get().getDay_of_month(), Bruker.get().getMonth(), Bruker.get().getYear()));
        phoneText.setText(Bruker.get().getMobilnummer());
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

    public void onClickFortsettBtn(View v) {
        Intent intent = new Intent(this, Register5Activity.class);
        startActivity(intent);
    }

    public void onClickChangeName(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onClickChangeDOB(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onClickChangePhone(View v) {
        Intent intent = new Intent(this, Register2Activity.class);
        startActivity(intent);
    }

    public void onClickChangeEmail(View v) {
        Intent intent = new Intent(this, Register2Activity.class);
        startActivity(intent);
    }

    public void onClickChangeUsername(View v) {
        Intent intent = new Intent(this, Register3Activity.class);
        startActivity(intent);
    }

    public void onClickChangePassword(View v) {
        Intent intent = new Intent(this, Register3Activity.class);
        startActivity(intent);
    }
}
