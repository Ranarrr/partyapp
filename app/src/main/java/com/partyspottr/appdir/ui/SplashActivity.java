package com.partyspottr.appdir.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import com.google.firebase.auth.FirebaseAuth;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.networking.LoginUser;
import com.partyspottr.appdir.classes.networking.LogoutUser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Ranarrr on 14-Mar-18.
 *
 * @author Ranarrr
 */

public class SplashActivity extends AppCompatActivity {
    public static FirebaseAuth mAuth;
    private boolean hasFineLocation;
    private boolean hasCoarseLocation;
    private boolean hasSendSMS;
    private boolean once;
    private static boolean passedfirst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        Bruker.get().Init(this);
        //FacebookSdk.sdkInitialize(this);
        mAuth = FirebaseAuth.getInstance();

        MainActivity.typeface = Typeface.createFromAsset(getAssets(), "valeraround.otf");

        hasFineLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        hasCoarseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        hasSendSMS = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;

        if(!Bruker.get().isLoggetpa() && !Bruker.get().getBrukernavn().isEmpty() && !Bruker.get().getPassord().isEmpty()
                && !Bruker.get().getEmail().isEmpty() && mAuth.getCurrentUser() == null) {
            LoginUser loginUser = new LoginUser(this, Bruker.get().getBrukernavn(), Bruker.get().getPassord());
            loginUser.execute();
        }

        if(!hasFineLocation || !hasCoarseLocation) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Utilities.LOCATION_REQUEST_CODE);
        }

        once = false;
        passedfirst = false;

        if(hasFineLocation && hasCoarseLocation) {
            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                Criteria criteria = new Criteria();
                String bestprovider = locationManager.getBestProvider(criteria, false);

                Location location = locationManager.getLastKnownLocation(bestprovider);

                if(location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                try {
                    for(Address address : geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1)) {
                        if(address != null) {
                            Bruker.get().setCountry(address.getCountryName());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(!hasSendSMS) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, Utilities.SEND_SMS_REQUEST_CODE);
        }

        /*if(AccessToken.getCurrentAccessToken() != null) {
            Intent intent = new Intent(this, ProfilActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bruker.get().setLoggetpa(true);
            startActivity(intent);
        }*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!Bruker.get().isLoggetpa() && hasSendSMS && hasCoarseLocation && hasFineLocation) {
                    Intent main = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(main);
                    SplashActivity.this.finish();
                }

                passedfirst = true;
            }
        }, 3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Utilities.SEND_SMS_REQUEST_CODE) {
            for(int i = 0; i < permissions.length; i++) {
                if(permissions[i].equals(Manifest.permission.SEND_SMS)) {
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        hasSendSMS = true;
                    }
                }
            }
        }

        if(requestCode == Utilities.LOCATION_REQUEST_CODE) {
            for(int i = 0; i < permissions.length; i++) {
                if(permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        hasCoarseLocation = true;
                        hasFineLocation = true;
                    }
                }
            }
        }

        if(hasSendSMS && hasFineLocation && hasCoarseLocation && (requestCode == Utilities.SEND_SMS_REQUEST_CODE || requestCode == Utilities.LOCATION_REQUEST_CODE) && passedfirst) {
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
        } else if((!hasSendSMS || !hasFineLocation || !hasCoarseLocation) && (requestCode == Utilities.SEND_SMS_REQUEST_CODE || requestCode == Utilities.LOCATION_REQUEST_CODE) && once) {
            new AlertDialog.Builder(this)
                    .setTitle("Required permissions")
                    .setMessage("These permissions are required to use Partyspottr, and they are only used to better your experience with this app.")
                    .setCancelable(false)
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!hasSendSMS)
                                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.SEND_SMS}, Utilities.SEND_SMS_REQUEST_CODE);
                            if(!hasCoarseLocation || !hasFineLocation)
                                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    Utilities.LOCATION_REQUEST_CODE);
                        }
                    })
                    .show();
        }

        once = true;

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}