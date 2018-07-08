package com.partyspottr.appdir.ui.mainfragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.ImageChange;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.CountryCodes;
import com.partyspottr.appdir.classes.networking.LogoutUser;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.ProfilActivity;
import com.partyspottr.appdir.ui.other_ui.CropProfileImg;
import com.partyspottr.appdir.ui.other_ui.Drikkeleker;
import com.partyspottr.appdir.ui.other_ui.SettingActivity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Ranarrr on 26-Feb-18.
 *
 * @author Ranarrr
 */

public class profilfragment extends Fragment {
    public static ImageChange profile_imagechange = new ImageChange();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profilfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView fornavn_etternavn = view.findViewById(R.id.fornavn_etternavn);
        TextView by = view.findViewById(R.id.profil_by);
        ImageView countryflag = view.findViewById(R.id.countryflag_profil);
        TextView oneliner = view.findViewById(R.id.profil_oneliner);
        Button instillinger = view.findViewById(R.id.profil_instillinger);
        final Button logout = view.findViewById(R.id.log_out_btn);
        final ImageButton profilbilde = view.findViewById(R.id.profilbilde);
        Button drikkeleker = view.findViewById(R.id.profil_drikkeleker);
        Button hjelp = view.findViewById(R.id.profil_hjelp);
        Button oppgrader = view.findViewById(R.id.profil_premium);

        if(getActivity() == null)
            return;

        ((TextView) getActivity().findViewById(R.id.title_toolbar)).setText(Bruker.get().getBrukernavn());
        getActivity().findViewById(R.id.search_events).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.add_event).setVisibility(View.INVISIBLE);

        TextView title = getActivity().findViewById(R.id.title_toolbar);

        drikkeleker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Drikkeleker.class);
                startActivity(intent);
            }
        });

        if(Bruker.get().getProfilepic() == null)
            profilbilde.setImageDrawable(getResources().getDrawable(R.drawable.mannmeny));
        else
            profilbilde.setImageBitmap(Bruker.get().getProfilepic());

        profilbilde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getActivity(), CropProfileImg.class);
                    startActivity(intent);
                } else
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Utilities.READ_EXTERNAL_STORAGE_PROFILE_CODE);
            }
        });

        profile_imagechange.addChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                profilbilde.setImageBitmap(profile_imagechange.getBmp());
                Bruker.get().setProfilepic(profile_imagechange.getBmp());

                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType("image/jpg")
                        .build();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                profile_imagechange.getBmp().compress(Bitmap.CompressFormat.PNG, 100, stream);
                UploadTask uploadTask = ProfilActivity.storage.getReference().child(Bruker.get().getBrukernavn()).putBytes(stream.toByteArray(), metadata);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(getActivity() != null)
                            Toast.makeText(getActivity(), "Failed to change profile picture.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(getActivity() != null)
                            Toast.makeText(getActivity(), "Changed profile picture!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        title.setText(Bruker.get().getBrukernavn());

        fornavn_etternavn.setTypeface(MainActivity.typeface);
        by.setTypeface(MainActivity.typeface);
        oneliner.setTypeface(MainActivity.typeface);
        logout.setTypeface(MainActivity.typeface);
        drikkeleker.setTypeface(MainActivity.typeface);
        instillinger.setTypeface(MainActivity.typeface);
        hjelp.setTypeface(MainActivity.typeface);
        oppgrader.setTypeface(MainActivity.typeface);

        instillinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity(), R.style.mydatepickerdialog)
                        .setTitle(getActivity().getResources().getString(R.string.logg_ut))
                        .setMessage(getString(R.string.logg_ut_melding)) // TODO: Translation
                        .setPositiveButton(getActivity().getResources().getString(R.string.nei), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .setNegativeButton(getActivity().getResources().getString(R.string.ja), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogoutUser logoutUser = new LogoutUser(getActivity());
                                logoutUser.execute();
                            }
                        })
                        .show();
            }
        });

        if(Bruker.get().getTown() == null || Bruker.get().getTown().isEmpty()) {
            by.setText(Bruker.get().getCountry());
        } else {
            String str = String.format(Locale.ENGLISH, "%s, %s", Bruker.get().getCountry(), Bruker.get().getTown());
            if(str.length() > 25) {
                by.setText(String.format(Locale.ENGLISH, "%s\n%s", Bruker.get().getCountry(), Bruker.get().getTown()));
            } else {
                by.setText(str);
            }
        }

        fornavn_etternavn.setText(String.format(Locale.ENGLISH, "%s %s, %d", Bruker.get().getFornavn(), Bruker.get().getEtternavn(), Utilities.calcAge(new GregorianCalendar(Bruker.get().getYear(),
                Bruker.get().getMonth(), Bruker.get().getDay_of_month()))));

        if(Bruker.get().getOneliner() == null || Bruker.get().getOneliner().isEmpty()) {
            oneliner.setVisibility(View.GONE);
        } else {
            oneliner.setText(Bruker.get().getOneliner());
        }

        if(Bruker.get().getCountry() != null) {
            if(!Bruker.get().getCountry().equals("Dominican Republic")) { // because android studio reserves the resource name "do"
                String identifier = CountryCodes.getCountrySign(Bruker.get().getCountry()).toLowerCase();

                int resource = getActivity().getResources().getIdentifier(identifier, "drawable", getActivity().getPackageName());

                if(resource > 0) {
                    Drawable drawable = getActivity().getResources().getDrawable(resource);

                    countryflag.setImageDrawable(drawable);
                }
            } else {
                countryflag.setImageResource(R.drawable.dominican_republic);
            }
        }
    }
}
