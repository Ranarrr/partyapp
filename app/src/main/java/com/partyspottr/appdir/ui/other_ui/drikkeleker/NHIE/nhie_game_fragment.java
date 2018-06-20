package com.partyspottr.appdir.ui.other_ui.drikkeleker.NHIE;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class nhie_game_fragment extends Fragment {
    private List<Integer> lastarrint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.nhie_game, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView statement = view.findViewById(R.id.nhie_game_statement);

        statement.setTypeface(MainActivity.typeface);

        lastarrint = new ArrayList<>();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arr = getContext().getResources().getStringArray(R.array.nhie_game_array);
                int arrint = new Random().nextInt(arr.length);
                while(lastarrint.contains(arrint))
                    arrint = new Random().nextInt(arr.length);

                statement.setText(arr[arrint]);
                if(lastarrint.size() == 5)
                    lastarrint.remove(0);

                lastarrint.add(arrint);
            }
        });

        view.performClick();
    }
}