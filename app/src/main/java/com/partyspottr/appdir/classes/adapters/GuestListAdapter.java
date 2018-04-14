package com.partyspottr.appdir.classes.adapters;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.ChatPreview;
import com.partyspottr.appdir.classes.Chatter;
import com.partyspottr.appdir.classes.Participant;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.classes.networking.AddFriendRequest;
import com.partyspottr.appdir.classes.networking.AddParticipant;
import com.partyspottr.appdir.classes.networking.RemoveParticipant;
import com.partyspottr.appdir.enums.EventStilling;
import com.partyspottr.appdir.ui.MainActivity;
import com.partyspottr.appdir.ui.other_ui.Profile;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.partyspottr.appdir.ui.MainActivity.typeface;

/**
 * Created by Ranarrr on 23-Feb-18.
 *
 * @author Ranarrr
 */

public class GuestListAdapter extends BaseAdapter {

    private List<Participant> GuestList;
    private Activity thisActivity;
    private long eventId;

    public GuestListAdapter(Activity activity, long eventid, List<Participant> guestlist) {
        GuestList = Participant.SortParticipants(guestlist);
        thisActivity = activity;
        eventId = eventid;
    }

    @Override
    public int getCount() {
        return GuestList.size();
    }

    @Override
    public Object getItem(int position) {
        return GuestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Participant participant = GuestList.get(position);

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) thisActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(layoutInflater != null) {
                convertView = layoutInflater.inflate(R.layout.gjest, parent, false);
            }
        }

        if(convertView != null) {
            //ImageView profilbilde = convertView.findViewById(R.id.gjest_profilbilde);
            ImageView countryflag = convertView.findViewById(R.id.countryflag_IV);
            TextView brukernavn = convertView.findViewById(R.id.gjest_brukernavn);
            TextView by_land = convertView.findViewById(R.id.gjest_by_land);
            final ImageButton more_options = convertView.findViewById(R.id.gjest_more_options);

            brukernavn.setTypeface(typeface);
            by_land.setTypeface(typeface);

            if(!participant.getCountry().equals("Dominican Republic")) { // because android studio reserves the resource name "do"
                String identifier = CountryCodes.getCountrySign(participant.getCountry()).toLowerCase();

                int resource = thisActivity.getResources().getIdentifier(identifier, "drawable", thisActivity.getPackageName());

                if(resource > 0) {
                    Drawable drawable = thisActivity.getResources().getDrawable(resource);

                    countryflag.setImageDrawable(drawable);
                }
            } else {
                countryflag.setImageResource(R.drawable.dominican_republic);
            }

            more_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = new ContextThemeWrapper(thisActivity, R.style.popup);
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    final ListView lv_gjestliste = parent.findViewById(R.id.lv_gjesteliste);
                    final AlphaAnimation mFadeOut = new AlphaAnimation(1.0f, 0.3f);
                    final AlphaAnimation mFadeIn = new AlphaAnimation(0.3f, 1.0f);

                    mFadeOut.setFillAfter(true);
                    mFadeIn.setFillAfter(true);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.vis_profil:
                                    Intent intent = new Intent(thisActivity, Profile.class);
                                    intent.putExtra("user", participant.getBrukernavn());
                                    thisActivity.startActivity(intent);

                                    return true;

                                case R.id.som_venn:
                                    AddFriendRequest addFriendRequest = new AddFriendRequest(thisActivity, participant.getBrukernavn());
                                    addFriendRequest.execute();

                                    return true;

                                case R.id.make_host:
                                    if(!participant.getStilling().equals(EventStilling.VERT)) {
                                        RemoveParticipant removeParticipant = new RemoveParticipant(thisActivity, eventId, participant.getBrukernavn(), GuestList);
                                        removeParticipant.execute();
                                        AddParticipant addParticipant = new AddParticipant(thisActivity, eventId, participant, GuestList);
                                        addParticipant.execute();
                                    } else {
                                        Toast.makeText(thisActivity, "This participant is already a host!", Toast.LENGTH_SHORT).show();
                                    }

                                    return true;

                                case R.id.remove:
                                    if(!participant.getBrukernavn().equals(Bruker.get().getBrukernavn())) {
                                        RemoveParticipant removeParticipant = new RemoveParticipant(thisActivity, eventId, participant.getBrukernavn(), GuestList);
                                        removeParticipant.execute();
                                    }

                                    return true;

                                default:
                                    return true;
                            }
                        }
                    });

                    Participant bruker = Participant.getBrukerInList(GuestList);

                    popupMenu.inflate(R.menu.more_options_menu_vert);

                    if((bruker != null && bruker.getStilling() != EventStilling.VERT) || bruker == null) {
                        popupMenu.getMenu().removeItem(R.id.remove);
                        popupMenu.getMenu().removeItem(R.id.make_host);
                    }

                    popupMenu.show();

                    // Dim out all the other list items if they exist
                    int firstPos = lv_gjestliste.getFirstVisiblePosition() - lv_gjestliste.getHeaderViewsCount();
                    final int ourPos = Participant.getParticipantPos(GuestList, participant) - firstPos;
                    int count = lv_gjestliste.getChildCount();
                    for (int i = 0; i < count; i++) {
                        if (i == ourPos) {
                            continue;
                        }

                        final View child = lv_gjestliste.getChildAt(i);
                        if (child != null) {
                            child.clearAnimation();
                            child.startAnimation(mFadeOut);
                        }
                    }

                    // Make sure to bring them back to normal after the menu is gone
                    popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                        @Override
                        public void onDismiss(PopupMenu popupMenu) {
                            int count = lv_gjestliste.getChildCount();
                            for (int i = 0; i < count; i++) {
                                if (i == ourPos) {
                                    continue;
                                }

                                final View v = lv_gjestliste.getChildAt(i);
                                if (v != null) {
                                    v.clearAnimation();
                                    v.startAnimation(mFadeIn);
                                }
                            }
                        }
                    });
                }
            });

            brukernavn.setText(participant.getBrukernavn());

            if(participant.getTown() != null) {
                by_land.setText(participant.getTown());
            } else {
                by_land.setText(participant.getCountry());
            }
        }

        return convertView;
    }
}
