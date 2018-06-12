package com.partyspottr.appdir.classes.adapters;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.ChatPreview;
import com.partyspottr.appdir.classes.Chatter;
import com.partyspottr.appdir.classes.Chauffeur;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChauffeurAdapter extends BaseAdapter {
    private List<Chauffeur> chauffeurList;
    private Activity thisActivity;

    public ChauffeurAdapter(Activity activity, List<Chauffeur> list) {
        thisActivity = activity;

        if(list != null && list.size() == 0) {
            chauffeurList = new ArrayList<>();
            chauffeurList.add(new Chauffeur(0.0, "£££", 0, 0));
        } else {
            chauffeurList = list;
            chauffeurList.add(0, new Chauffeur(0.0, "$$$", 0, 0));
        }
    }

    @Override
    public int getCount() {
        return chauffeurList.size();
    }

    @Override
    public Object getItem(int position) {
        return chauffeurList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final Chauffeur chauffeur = chauffeurList.get(position);

        if(chauffeur.getM_rating() == 0.0 && chauffeur.getM_age() == 0 && chauffeur.getM_capacity() == 0 && chauffeur.getM_brukernavn().equals("$$$")) {
            View footer = thisActivity.getLayoutInflater().inflate(R.layout.chatmessage_footer, parent, false);
            TextView text = footer.findViewById(R.id.text_footer);
            text.setText("Sjåfører i nærheten:");
            return footer;
        }

        if(chauffeur.getM_rating() == 0.0 && chauffeur.getM_age() == 0 && chauffeur.getM_capacity() == 0 && chauffeur.getM_brukernavn().equals("£££")) {
            View v = thisActivity.getLayoutInflater().inflate(R.layout.chatmessage_footer, parent, false);
            ((TextView) v.findViewById(R.id.text_footer)).setText("Finner ingen sjåfører.");
            return v;
        }

        if(convertView == null) {
            convertView = thisActivity.getLayoutInflater().inflate(R.layout.chauffeur, parent, false);

                if(convertView != null) {
                    TextView sted = convertView.findViewById(R.id.chauffeur_sted);
                    TextView maks = convertView.findViewById(R.id.chauffeur_max);
                    TextView navn = convertView.findViewById(R.id.chauffeur_navn);
                    ImageButton send_msg = convertView.findViewById(R.id.chauffeur_msg);

                    sted.setTypeface(MainActivity.typeface);
                    maks.setTypeface(MainActivity.typeface);
                    navn.setTypeface(MainActivity.typeface);

                    send_msg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog send_msg = new Dialog(thisActivity);
                            send_msg.setCancelable(true);
                            send_msg.setCanceledOnTouchOutside(true);
                            send_msg.requestWindowFeature(1);
                            send_msg.setContentView(R.layout.send_message_dialog);

                            if(send_msg.getWindow() != null) {
                                WindowManager.LayoutParams layoutParams = send_msg.getWindow().getAttributes();

                                DisplayMetrics dm = new DisplayMetrics();

                                thisActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

                                send_msg.getWindow().setBackgroundDrawable(null);

                                layoutParams.width = dm.widthPixels;

                                send_msg.getWindow().setAttributes(layoutParams);
                                send_msg.getWindow().setGravity(Gravity.BOTTOM);
                            }

                            send_msg.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    if (send_msg.getWindow() != null) {
                                        View view = send_msg.getWindow().getDecorView();

                                        ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0.0f).start();
                                    }
                                }
                            });

                            Button send = send_msg.findViewById(R.id.send_new_msg_btn);

                            send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditText message = send_msg.findViewById(R.id.send_new_msg_text);

                                    List<Chatter> list = new ArrayList<>();
                                    list.add(new Chatter(Bruker.get().getBrukernavn(), Bruker.get().getFornavn(), Bruker.get().getEtternavn()));
                                    list.add(new Chatter(chauffeur.getM_brukernavn(), chauffeur.getFornavn(), chauffeur.getEtternavn()));
                                    if(Bruker.get().startChat(new ChatPreview(message.getText().toString(), "", false, list), chauffeur.getM_brukernavn(), message.getText().toString())) {
                                        Toast.makeText(thisActivity, "Started a chat with " + chauffeur.getM_brukernavn(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            send_msg.show();
                        }
                    });

                    navn.setText(String.format(Locale.ENGLISH, "%s %s (%d)", chauffeur.getFornavn(), chauffeur.getEtternavn(), chauffeur.getM_age()));

                    maks.setText(String.format(Locale.ENGLISH, "%d", chauffeur.getM_capacity()));

                    Location loc1 = Utilities.getLatLng(thisActivity);

                    if(loc1 == null)
                        sted.setText("");
                    else {
                        float result[] = {0};
                        Location.distanceBetween(chauffeur.getLatitude(), chauffeur.getLongitude(), loc1.getLatitude(), loc1.getLongitude(), result);
                        sted.setText(String.format(Locale.ENGLISH, "%.1f km", result[0] == 0.0f ? result[0] : result[0] / 1000.f));
                    }
                }
            }

        return convertView;
    }
}