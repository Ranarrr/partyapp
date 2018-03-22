package com.partyspottr.appdir.ui.registerui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.networking.CreateUser;
import com.partyspottr.appdir.ui.MainActivity;

public class Register5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register5);

        Button finishbtn = findViewById(R.id.button6);
        TextView title = findViewById(R.id.textView3);

        finishbtn.setTypeface(MainActivity.typeface);
        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser createUser = new CreateUser(Register5Activity.this);
                createUser.execute();
            }
        });

        title.setTypeface(MainActivity.typeface);

    }
}
