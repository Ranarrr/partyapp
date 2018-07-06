package com.partyspottr.appdir.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.networking.LoginUser;
import com.partyspottr.appdir.ui.registerui.RegisterActivity;

/**
 * Created by Ranarrr on 26-Jan-18.
 *
 * @author Ranarrr
 */

public class MainActivity extends AppCompatActivity {
    public static Typeface typeface;

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctd.start();

        Button loginbtn = findViewById(R.id.LoginBtn);
        EditText username = findViewById(R.id.brukernavnLoginText);
        EditText password = findViewById(R.id.passordLoginText);
        Button registrerbtn = findViewById(R.id.RegistrerBtn);

        registrerbtn.setTypeface(typeface);
        loginbtn.setTypeface(typeface);
        username.setTypeface(typeface);
        password.setTypeface(typeface);

        Utilities.getPosition(this);

        if(!Bruker.get().isLoggetpa() && Bruker.get().getBrukernavn() != null && !Bruker.get().getBrukernavn().isEmpty()) {
            username.setText(Bruker.get().getBrukernavn());
        }
    }

    CountDownTimer ctd = new CountDownTimer(Long.MAX_VALUE, 200) {
        public void onTick(long millisUntilFinished) {
            Button button = findViewById(R.id.LoginBtn);
            if(!Utilities.hasNetwork(getApplicationContext())) {
                Bruker.get().setConnected(false);
                button.setEnabled(false);
            } else if(Utilities.hasNetwork(getApplicationContext())) {
                Bruker.get().setConnected(true);
                button.setEnabled(true);
            }
        }

        public void onFinish() {}
    };

    @Override
    protected void onPause() {
        ctd.cancel();
        super.onPause();
    }

    @Override
    protected void onResume() {
        ctd.start();
        if(!Utilities.hasNetwork(this)) {
            Toast.makeText(this, getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
            Bruker.get().setConnected(false);
        } else {
            Bruker.get().setConnected(true);
        }
        super.onResume();
    }

    public void onClickRegistrerBtn(View v) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void onClickLogin(View v) {
        if(!Bruker.get().isConnected()) {
            Toast.makeText(this, getResources().getString(R.string.tilkoblingsfeil), Toast.LENGTH_SHORT).show();
            return;
        }

        EditText brukernavnText = findViewById(R.id.brukernavnLoginText);
        EditText passordText = findViewById(R.id.passordLoginText);

        if(brukernavnText.length() > 1) {
            if(passordText.length() > 6) {
                ViewCompat.setBackgroundTintList(passordText, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));

                LoginUser loginUser = new LoginUser(this, brukernavnText.getText().toString(), passordText.getText().toString());
                loginUser.execute();
                // LOGIN!
            } else {
                passordText.getText().clear();
                ViewCompat.setBackgroundTintList(passordText, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
            }
        } else {
            brukernavnText.getText().clear();
            ViewCompat.setBackgroundTintList(brukernavnText, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
        }

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }*/
}