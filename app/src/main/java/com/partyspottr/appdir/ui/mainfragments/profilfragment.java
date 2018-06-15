package com.partyspottr.appdir.ui.mainfragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.ImageChange;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.adapters.CountryCodes;
import com.partyspottr.appdir.classes.networking.LogoutUser;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.other_ui.Drikkeleker;
import com.partyspottr.appdir.ui.other_ui.SettingActivity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Ranarrr on 26-Feb-18.
 *
 * @author Ranarrr
 */

public class profilfragment extends Fragment {
    private ImageChange imageChange = new ImageChange();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profilfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView fornavn_etternavn = view.findViewById(R.id.fornavn_etternavn);
        TextView by = view.findViewById(R.id.profil_by);
        ImageView countryflag = view.findViewById(R.id.countryflag_profil);
        TextView oneliner = view.findViewById(R.id.profil_oneliner);
        TextView title = getActivity().findViewById(R.id.title_toolbar);
        Button instillinger = view.findViewById(R.id.profil_instillinger);
        final Button logout = view.findViewById(R.id.log_out_btn);
        final ImageButton profilbilde = view.findViewById(R.id.profilbilde);
        Button drikkeleker = view.findViewById(R.id.profil_drikkeleker);

        drikkeleker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Drikkeleker.class);
                startActivity(intent);
            }
        });

        profilbilde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1003);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Utilities.READ_EXTERNAL_STORAGE_CODE);
                }
            }
        });

        imageChange.addChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    profilbilde.setImageBitmap(Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageChange.getUri()),
                            (int) getResources().getDimension(R.dimen._150sdp), (int) getResources().getDimension(R.dimen._75sdp), true));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        title.setText(Bruker.get().getBrukernavn());

        fornavn_etternavn.setTypeface(MainActivity.typeface);
        by.setTypeface(MainActivity.typeface);
        oneliner.setTypeface(MainActivity.typeface);
        logout.setTypeface(MainActivity.typeface);

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
                        .setMessage("Er du sikker på at du vil logge ut?") // TODO: Translation
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Utilities.SELECT_PROFILE_IMAGE_CODE && resultCode == RESULT_OK && data.getData() != null) {
            Uri image = data.getData();

            imageChange.setUri(image);
            String str = Utilities.getPathFromUri(getContext(), image);
            if(str != null)
                imageChange.setImage(new File(str));
        }
    }
}
