package com.partyspottr.appdir.classes.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.Event;
import com.partyspottr.appdir.classes.Friend;
import com.partyspottr.appdir.classes.Participant;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.enums.EventStilling;
import com.partyspottr.appdir.ui.ProfilActivity;
import com.partyspottr.appdir.ui.other_ui.Profile;

import java.util.ArrayList;
import java.util.List;

import static com.partyspottr.appdir.ui.MainActivity.typeface;

/**
 * Created by Ranarrr on 23-Feb-18.
 *
 * @author Ranarrr
 */

public class GuestListAdapter extends BaseAdapter {
    private static List<Bitmap> profilepics;

    private List<Participant> GuestList;
    private Activity thisActivity;
    private long eventId;

    public GuestListAdapter(Activity activity, long eventid, List<Participant> guestlist) {
        GuestList = Participant.SortParticipants(guestlist);
        thisActivity = activity;
        eventId = eventid;
        profilepics = new ArrayList<>();
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
            final TextView brukernavn = convertView.findViewById(R.id.gjest_brukernavn);
            TextView by_land = convertView.findViewById(R.id.gjest_by_land);
            final ImageButton more_options = convertView.findViewById(R.id.gjest_more_options);
            final ImageView profile_pic = convertView.findViewById(R.id.gjest_profilbilde);

            brukernavn.setTypeface(typeface);
            by_land.setTypeface(typeface);

            if(profilepics.size() > position && profilepics.get(position) != null)
                profile_pic.setImageBitmap(profilepics.get(position));
            else if(profilepics.size() <= position) {
                StorageReference ref = ProfilActivity.storage.getReference().child(participant.getBrukernavn());

                ref.getBytes(2048 * 2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = Utilities.decodeSampledBitmapFromByteArray(bytes, 96, 96);
                        profilepics.add(bmp);
                        profile_pic.setImageBitmap(bmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        profilepics.add(null);
                    }
                });
            }

            if(!participant.getCountry().equals("Dominican Republic")) { // because android studio reserves the resource name "do"
                String identifier = CountryCodes.getCountrySign(participant.getCountry()).toLowerCase();

                int resource = thisActivity.getResources().getIdentifier(identifier, "drawable", thisActivity.getPackageName());

                if(resource > 0) {
                    Drawable drawable = thisActivity.getResources().getDrawable(resource);

                    countryflag.setImageDrawable(drawable);
                }
            } else
                countryflag.setImageResource(R.drawable.dominican_republic);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(thisActivity, Profile.class);
                    intent.putExtra("user", participant.getBrukernavn());
                    thisActivity.startActivity(intent);
                }
            });

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
                                    if(!participant.getBrukernavn().equals(Bruker.get().getBrukernavn())) {

                                        final DatabaseReference brukerref = FirebaseDatabase.getInstance().getReference("users").child(participant.getBrukernavn());
                                        brukerref.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    if(snapshot.getKey() != null && snapshot.getKey().equals("requests")) {
                                                        List<Friend> list = new Gson().fromJson(snapshot.getValue(String.class), Utilities.listFriendsType);

                                                        list.add(Friend.BrukerToFriend(Bruker.get()));

                                                        brukerref.child("requests").setValue(new Gson().toJson(list)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                    Toast.makeText(thisActivity, "Sent friend request!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                                            @Override
                                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
                                            @Override
                                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                                        });
                                    }

                                    return true;

                                case R.id.make_host:
                                    if(!participant.getStilling().equals(EventStilling.VERT)) {
                                        new AlertDialog.Builder(thisActivity, R.style.mydatepickerdialog)
                                                .setTitle("Confirmation")
                                                .setMessage("Are you sure you want to make this person a host?")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Event event = Bruker.get().getEventFromID(eventId);

                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(eventId));

                                                        event.getParticipants().set(Participant.getParticipantPos(event.getParticipants(), participant.getBrukernavn()), new Participant(participant.getBrukernavn(),
                                                                participant.getCountry(), participant.getTown(), EventStilling.VERT));

                                                        ref.child("participants").setValue(new Gson().toJson(event.getParticipants())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(thisActivity, "Made participant a host!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(thisActivity, "Failed to make this participant a host.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }})
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {}})
                                                .show();
                                    }

                                    return true;

                                case R.id.remove_host:
                                    if(participant.getBrukernavn().equals(Bruker.get().getEventFromID(eventId).getHostStr())) {
                                        Toast.makeText(thisActivity, "You cannot remove the original host.", Toast.LENGTH_SHORT).show();
                                        return true;
                                    } else {
                                        Event event = Bruker.get().getEventFromID(eventId);

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(eventId));

                                        event.getParticipants().set(Participant.getParticipantPos(event.getParticipants(), participant.getBrukernavn()), new Participant(participant.getBrukernavn(),
                                                participant.getCountry(), participant.getTown(), EventStilling.GJEST));

                                        ref.child("participants").setValue(new Gson().toJson(event.getParticipants())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(thisActivity, "Made participant a host!", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(thisActivity, "Failed to make this participant a host.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    return true;

                                case R.id.remove:
                                    if(!participant.getBrukernavn().equals(Bruker.get().getBrukernavn())) {
                                        new AlertDialog.Builder(thisActivity, R.style.mydatepickerdialog).setTitle("Remove user").setMessage("Are you sure you want to remove this user?")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        final Event event = Bruker.get().getEventFromID(eventId);

                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(eventId));

                                                        event.getParticipants().remove(Participant.getParticipantPos(event.getParticipants(), participant.getBrukernavn()));

                                                        ref.child("participants").setValue(new Gson().toJson(event.getParticipants())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                ListView lv_guestlist = thisActivity.findViewById(R.id.lv_gjesteliste);

                                                                if(lv_guestlist != null)
                                                                    lv_guestlist.setAdapter(new GuestListAdapter(thisActivity, eventId, event.getParticipants()));

                                                                Toast.makeText(thisActivity, "Removed participant!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(thisActivity, "Failed to remove participant.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }})
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {}})
                                                .show();
                                    } else {
                                        if(Bruker.get().getEventFromID(eventId).getHostStr().equalsIgnoreCase(Bruker.get().getBrukernavn())) {
                                            new android.app.AlertDialog.Builder(thisActivity, R.style.mydatepickerdialog)
                                                    .setTitle("Confirmation")
                                                    .setMessage("Are you sure you want to delete this event?")
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            final Event event = Bruker.get().getEventFromID(eventId);
                                                            if(event.isHasimage()) {
                                                                StorageReference asfafa = ProfilActivity.storage.getReference().child(event.getHostStr() + "_" + event.getNameofevent());
                                                                asfafa.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(event.getEventId()));
                                                                        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Toast.makeText(thisActivity, "Deleted event!", Toast.LENGTH_SHORT).show();
                                                                                thisActivity.onBackPressed();
                                                                                thisActivity.onBackPressed();
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(thisActivity, "Failed to delete event!", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            } else {
                                                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(event.getEventId()));
                                                                ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(thisActivity, "Deleted event!", Toast.LENGTH_SHORT).show();
                                                                        thisActivity.onBackPressed();
                                                                        thisActivity.onBackPressed();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(thisActivity, "Failed to delete event!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    })
                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {}
                                                    })
                                                    .show();
                                        } else {
                                            new AlertDialog.Builder(thisActivity, R.style.mydatepickerdialog)
                                                    .setTitle("Leave event")
                                                    .setMessage("Are you sure you want to leave the event?")
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            final Event event = Bruker.get().getEventFromID(eventId);

                                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(eventId)).child("participants");

                                                            event.getParticipants().remove(Participant.getParticipantPos(event.getParticipants(), participant.getBrukernavn()));

                                                            ref.setValue(new Gson().toJson(event.getParticipants())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    ListView lv_guestlist = thisActivity.findViewById(R.id.lv_gjesteliste);

                                                                    if(lv_guestlist != null)
                                                                        lv_guestlist.setAdapter(new GuestListAdapter(thisActivity, eventId, event.getParticipants()));

                                                                    Toast.makeText(thisActivity, "You left the event.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(thisActivity, "Failed to remove you from the event.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    })
                                                    .setNegativeButton("No", null)
                                                    .show();
                                        }
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
                        if(!participant.getBrukernavn().equalsIgnoreCase(Bruker.get().getBrukernavn()))
                            popupMenu.getMenu().removeItem(R.id.remove);

                        popupMenu.getMenu().removeItem(R.id.make_host);
                    }

                    if(participant.getBrukernavn().equalsIgnoreCase(Bruker.get().getBrukernavn())) {
                        popupMenu.getMenu().removeItem(R.id.make_host);
                        popupMenu.getMenu().removeItem(R.id.som_venn);
                        popupMenu.getMenu().removeItem(R.id.remove_host);
                    }

                    if(participant.getBrukernavn().equalsIgnoreCase(Bruker.get().getEventFromID(eventId).getHostStr()))
                        popupMenu.getMenu().removeItem(R.id.remove_host);

                    if(!participant.getStilling().equals(EventStilling.VERT))
                        popupMenu.getMenu().removeItem(R.id.remove_host);

                    popupMenu.show();

                    // Dim out all the other list items if they exist
                    int firstPos = lv_gjestliste.getFirstVisiblePosition() - lv_gjestliste.getHeaderViewsCount();
                    final int ourPos = Participant.getParticipantPos(GuestList, participant.getBrukernavn()) - firstPos;
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

                    // bring them back to normal after the menu is gone
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

            by_land.setText(participant.getTown() != null ? participant.getTown() : participant.getCountry());
        }

        return convertView;
    }
}
