package com.partyspottr.appdir.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.networking.LoginUser;
import com.partyspottr.appdir.classes.networking.LogoutUser;

import java.util.Arrays;

/**
 * Created by Ranarrr on 14-Mar-18.
 *
 * @author Ranarrr
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        Bruker.get().Init(this);
        FacebookSdk.sdkInitialize(this);

        MainActivity.typeface = Typeface.createFromAsset(getAssets(), "valeraround.otf");

        if(!Bruker.get().isLoggetpa() && !Bruker.get().getBrukernavn().isEmpty() && !Bruker.get().getPassord().isEmpty()) {
            LoginUser loginUser = new LoginUser(this, Bruker.get().getBrukernavn(), Bruker.get().getPassord());
            loginUser.execute();
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1002);
        }

        if(AccessToken.getCurrentAccessToken() != null) {
            Intent intent = new Intent(this, ProfilActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bruker.get().setLoggetpa(true);
            startActivity(intent);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!Bruker.get().isLoggetpa() && ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    Intent main = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(main);
                    SplashActivity.this.finish();
                }
            }
        }, 3500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1002) {
            for(int i = 0; i < permissions.length; i++) {
                if(permissions[i].equals(Manifest.permission.SEND_SMS)) {
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        new AlertDialog.Builder(this)
                                .setTitle("Suck my dick")
                                .setMessage("Accept or die")
                                .setCancelable(false)
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1002);
                            }
                                })
                                .show();
                        return;
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!Bruker.get().isLoggetpa()) {
                                    Intent main = new Intent(SplashActivity.this, MainActivity.class);
                                    SplashActivity.this.startActivity(main);
                                    SplashActivity.this.finish();
                                }
                            }
                        }, 1000);
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}