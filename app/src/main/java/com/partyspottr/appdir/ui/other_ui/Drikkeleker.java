package com.partyspottr.appdir.ui.other_ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.ui.other_ui.drikkeleker.NeverHaveIEver;

public class Drikkeleker extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drikkeleker);

        Button neverhaveiever = findViewById(R.id.drikkeleker_NHIE);

        neverhaveiever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Drikkeleker.this, NeverHaveIEver.class);
                startActivity(intent);
            }
        });
    }
}
