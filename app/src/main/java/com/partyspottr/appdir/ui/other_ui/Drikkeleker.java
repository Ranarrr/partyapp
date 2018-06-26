package com.partyspottr.appdir.ui.other_ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.other_ui.drikkeleker.NeverHaveIEver;

import org.w3c.dom.Text;

public class Drikkeleker extends AppCompatActivity {
    @Override
    protected void onStop() {
        Utilities.setupOnStop();

        super.onStop();
    }

    @Override
    protected void onRestart() {
        if(!Bruker.get().isConnected()) {
            super.onRestart();
            return;
        }

        Utilities.setupOnRestart(this);

        super.onRestart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drikkeleker);

        Toolbar toolbar = findViewById(R.id.toolbar4);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));

        Button neverhaveiever = findViewById(R.id.drikkeleker_NHIE);
        Button trynottolaugh = findViewById(R.id.drikkeleker_TNTL);
        Button kingscup = findViewById(R.id.drikkeleker_KC);
        Button piccolo = findViewById(R.id.drikkeleker_piccolo);

        TextView title = findViewById(R.id.drikkeleker_title);

        neverhaveiever.setTypeface(MainActivity.typeface);
        trynottolaugh.setTypeface(MainActivity.typeface);
        kingscup.setTypeface(MainActivity.typeface);
        piccolo.setTypeface(MainActivity.typeface);

        title.setTypeface(MainActivity.typeface);

        neverhaveiever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Drikkeleker.this, NeverHaveIEver.class);
                startActivity(intent);
            }
        });
    }
}
