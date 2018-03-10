package com.partyspottr.appdir.ui.registerui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.adapters.CountryCodes;
import com.partyspottr.appdir.classes.networking.CheckEmail;

import java.util.Locale;

public class Register2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        EditText emailText = findViewById(R.id.emailText);
        EditText mobilnr = findViewById(R.id.mobil_nr_text);
        Spinner spinner = findViewById(R.id.spinnerPrefix);

        spinner.setAdapter(new CountryCodes(this));

        if(Bruker.get().getEmail() != null && !Bruker.get().getEmail().isEmpty()) {
            emailText.setText(Bruker.get().getEmail());
            mobilnr.setText(Bruker.get().getMobilnummer());
        }

        mobilnr.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    public void onClickFortsettBtn(View v) {
        EditText emailText = findViewById(R.id.emailText);
        EditText mobilnr = findViewById(R.id.mobil_nr_text);
        Spinner spinner = findViewById(R.id.spinnerPrefix);

        if(!emailText.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString()).matches()) {
            if(!mobilnr.getText().toString().isEmpty() && Patterns.PHONE.matcher(mobilnr.getText().toString()).matches()) {
                ViewCompat.setBackgroundTintList(mobilnr, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));
                Bruker.get().setMobilnummer(String.format(Locale.ENGLISH, "+%s %s", ((String) spinner.getSelectedItem()).split(",")[0], mobilnr.getText().toString()));
                Bruker.get().setCountry(((String) spinner.getSelectedItem()).split(",")[2]);
                CheckEmail checkEmail = new CheckEmail(this, emailText.getText().toString());
                checkEmail.execute();
            } else {
                ViewCompat.setBackgroundTintList(mobilnr, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
            }
        } else {
            ViewCompat.setBackgroundTintList(emailText, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
            if(!mobilnr.getText().toString().isEmpty() && Patterns.PHONE.matcher(mobilnr.getText().toString()).matches()) {
                ViewCompat.setBackgroundTintList(mobilnr, ContextCompat.getColorStateList(getApplicationContext(), R.color.greentint));
            }
        }
    }
}
