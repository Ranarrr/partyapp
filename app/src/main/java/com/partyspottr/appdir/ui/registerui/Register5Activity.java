package com.partyspottr.appdir.ui.registerui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.networking.CreateUser;

public class Register5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register5);
    }

    public void onClickRegistrerBtn(View v) {
        CreateUser createUser = new CreateUser(this);
        createUser.execute();
    }
}
