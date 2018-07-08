package com.partyspottr.appdir.ui.other_ui;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.networking.DeleteUser;

/**
 * Created by Ranarrr on 20-Mar-18.
 *
 * @author Ranarrr
 */

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onStop() {
        Utilities.setupOnStop(this);

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.innstillinger_toolbar);
        Button slett_bruker = findViewById(R.id.setting_slett_konto);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        slett_bruker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog slett_konto = new Dialog(SettingActivity.this);
                slett_konto.setCancelable(true);
                slett_konto.setCanceledOnTouchOutside(true);
                slett_konto.requestWindowFeature(1);
                slett_konto.setContentView(R.layout.slett_konto_bekreftelse);

                if(slett_konto.getWindow() != null) {
                    WindowManager.LayoutParams layoutParams = slett_konto.getWindow().getAttributes();

                    DisplayMetrics dm = new DisplayMetrics();

                    SettingActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);

                    slett_konto.getWindow().setBackgroundDrawable(null);

                    layoutParams.width = dm.widthPixels;

                    slett_konto.getWindow().setAttributes(layoutParams);
                    slett_konto.getWindow().setGravity(Gravity.BOTTOM);
                }

                slett_konto.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        if (slett_konto.getWindow() != null) {
                            View view = slett_konto.getWindow().getDecorView();

                            ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0.0f).start();
                        }
                    }
                });

                Button slett = slett_konto.findViewById(R.id.slett_konto_slett);

                slett.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText password = slett_konto.findViewById(R.id.slett_konto_pass);

                        if(password.getText().length() > 6) {
                            DeleteUser deleteUser = new DeleteUser(SettingActivity.this, password.getText().toString());
                            deleteUser.execute();
                        }
                    }
                });

                slett_konto.show();
            }
        });

    }
}