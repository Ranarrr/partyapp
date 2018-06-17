package com.partyspottr.appdir.ui.registerui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.networking.CheckEmail;
import com.partyspottr.appdir.ui.MainActivity;

/**
 * Created by Ranarrr on 26-Jan-18.
 *
 * @author Ranarrr
 */

public class Register2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        final EditText emailText = findViewById(R.id.emailText);
        TextView header2 = findViewById(R.id.textView6);
        Button continuebtn = findViewById(R.id.button5);
        TextView title = findViewById(R.id.textView3);

        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!emailText.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString()).matches()) {
                    CheckEmail checkEmail = new CheckEmail(Register2Activity.this, emailText.getText().toString());
                    checkEmail.execute();
                } else {
                    ViewCompat.setBackgroundTintList(emailText, ContextCompat.getColorStateList(getApplicationContext(), R.color.redtint));
                }
            }
        });

        emailText.setTypeface(MainActivity.typeface);
        header2.setTypeface(MainActivity.typeface);
        continuebtn.setTypeface(MainActivity.typeface);
        title.setTypeface(MainActivity.typeface);

        if(Bruker.get().getEmail() != null && !Bruker.get().getEmail().isEmpty())
            emailText.setText(Bruker.get().getEmail());
    }
}
