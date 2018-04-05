package com.partyspottr.appdir.ui.other_ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.Bruker;
import com.partyspottr.appdir.classes.ChatMessage;
import com.partyspottr.appdir.classes.ChatPreview;
import com.partyspottr.appdir.classes.Chatter;
import com.partyspottr.appdir.classes.Utilities;
import com.partyspottr.appdir.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Chat extends AppCompatActivity {
    private List<ChatMessage> m_list;
    private int amountChatTime;
    private int chattime;
    private ChatPreview preview;
    private String lastfooter;

    private int doesChatMessageExist(ChatMessage chatMessage) {
        for(int i = 0; i < m_list.size(); i++) {
            if(m_list.get(i).getTime() == chatMessage.getTime() && m_list.get(i).getMessage().equals(chatMessage.getMessage()))
                return i;
        }

        return -1;
    }

    private void SortListByTime() {
        Collections.sort(m_list, new Comparator<ChatMessage>() {
            @Override
            public int compare(ChatMessage o1, ChatMessage o2) {
                if(o1.getTime() > o2.getTime())
                    return 1;
                else if(o1.getTime() < o2.getTime())
                    return -1;

                return 0;
            }
        });
    }

    private int getAmountPreviousFooters(int idx, LinearLayout layout) {
        int ret = 0;

        for(int i = 0; i <= idx; i++) {
            if(layout.getChildAt(i + ret).findViewById(R.id.text_footer) != null) {
                ret++;
            }
        }

        return ret;
    }

    private void removeFooterIfInvalid(LinearLayout layout) {
        for(int i = 0; i < layout.getChildCount(); i++) {
            if(layout.getChildAt(i).findViewById(R.id.text_footer) != null) {
                if(i + 1 >= layout.getChildCount()) {
                    layout.removeViewAt(i);
                    amountChatTime--;
                    return;
                }

                if(layout.getChildAt(i + 1).findViewById(R.id.text_footer) != null) {
                    layout.removeViewAt(i);
                    amountChatTime--;
                }
            }
        }
    }

    private String getLastFooter(LinearLayout layout) {
        String ret = "";

        for(int i = 0; i < layout.getChildCount(); i++) {
            TextView text = layout.getChildAt(i).findViewById(R.id.text_footer);
            if(text != null) {
                ret = text.getText().toString();
            }
        }

        return ret;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatmessage);

        amountChatTime = 0;
        m_list = new ArrayList<>();
        chattime = 0;

        Toolbar toolbar = findViewById(R.id.toolbar6);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            return;
        } else {
            preview = ChatPreview.findPreviewInList(extras.getString("chatter1"), extras.getString("chatter2"), Bruker.get().getChatMessageList());

            if(preview == null)
                return;
        }

        final EditText write_msg = findViewById(R.id.write_msg);
        ImageButton send_msg_btn = findViewById(R.id.chat_send);
        TextView title = findViewById(R.id.chat_title);

        title.setTypeface(MainActivity.typeface);
        write_msg.setTypeface(MainActivity.typeface);

        if(!preview.isGroupchat()) {
            Chatter other_chatter = Chatter.getChatterNotEqualToBruker(preview.getChatters());

            if(other_chatter != null)
                title.setText(String.format(Locale.ENGLISH, "%s %s", other_chatter.getFornavn(), other_chatter.getEtternavn()));
        } else {
            title.setText(preview.getGroupname());
        }

        if(getWindow() != null)
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("messages")
                .child(preview.isGroupchat() ? "group_" + preview.getGroupname() : preview.getChatters().get(0).getBrukernavn() + "_" + preview.getChatters().get(1).getBrukernavn());

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage message = dataSnapshot.getValue(ChatMessage.class);

                if(message == null)
                    return;

                if(doesChatMessageExist(message) == -1) {
                    m_list.add(message);

                    addChat(preview, message, null);
                } else {
                    LinearLayout layout = findViewById(R.id.ll_chat);

                    if(layout != null) {
                        if(m_list.size() + amountChatTime > layout.getChildCount()) {
                            addChat(preview, message, null);
                        }
                    } else {
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        write_msg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND) {
                    m_list.add(new ChatMessage(write_msg.getText().toString(), Bruker.get().getBrukernavn()));
                    SortListByTime();
                    FirebaseDatabase.getInstance().getReference("messagepreviews").child(preview.isGroupchat() ? "group_" + preview.getGroupname() : preview.getChatters().get(0).getBrukernavn() +
                            "_" + preview.getChatters().get(1).getBrukernavn()).setValue(new ChatPreview(write_msg.getText().toString(), preview.isGroupchat() ? preview.getGroupname() : "", preview.isGroupchat(),
                            preview.getChatters()));
                    FirebaseDatabase.getInstance().getReference("messages").child(preview.getChatters().get(0).getBrukernavn() + "_" + preview.getChatters().get(1).getBrukernavn()).setValue(m_list);
                    write_msg.getText().clear();
                    return true;
                }

                return false;
            }
        });

        send_msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(write_msg.getText().length() <= 0)
                    return;

                m_list.add(new ChatMessage(write_msg.getText().toString(), Bruker.get().getBrukernavn()));
                SortListByTime();
                FirebaseDatabase.getInstance().getReference("messagepreviews").child(preview.isGroupchat() ? "group_" + preview.getGroupname() : preview.getChatters().get(0).getBrukernavn() +
                        "_" + preview.getChatters().get(1).getBrukernavn()).setValue(new ChatPreview(write_msg.getText().toString(), preview.isGroupchat() ? preview.getGroupname() : "",
                        preview.isGroupchat(), preview.getChatters()));
                FirebaseDatabase.getInstance().getReference("messages").child(preview.getChatters().get(0).getBrukernavn() + "_" + preview.getChatters().get(1).getBrukernavn()).setValue(m_list);
                write_msg.getText().clear();
            }
        });

        write_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ScrollView scrollView = findViewById(R.id.scroll_chat);

                        if(scrollView != null)
                            scrollView.fullScroll(View.FOCUS_DOWN);

                    }
                }, 200);
            }
        });
    }

    // to use index for inserting chats for childchanged. NOT WORKING ATM
    private void addChat(final ChatPreview preview, final ChatMessage chatMessage, @Nullable Integer index) {
        LayoutInflater inflater = getLayoutInflater();

        View v;
        final LinearLayout layout = findViewById(R.id.ll_chat);
        final ScrollView scrollView = findViewById(R.id.scroll_chat);

        if(layout == null)
            return;

        if(chatMessage.getBruker().equals(Bruker.get().getBrukernavn()))
            v = inflater.inflate(R.layout.chatmessageitem_you, layout, false);
        else
            v = inflater.inflate(R.layout.chatmessageitem, layout, false);

        if(v != null) {
            TextView message = v.findViewById(R.id.msgitem_msg);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView time = v.findViewById(R.id.msgitem_time);
                    if(time != null) {
                        time.setVisibility(time.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                        if(chatMessage.getTime() > 0) {
                            time.setText(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(chatMessage.getTime())));
                        }
                    }
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(!chatMessage.getBruker().equals(Bruker.get().getBrukernavn()))
                        return true;

                    Context context = new ContextThemeWrapper(Chat.this, R.style.popup);
                    PopupMenu popupMenu = new PopupMenu(context, v);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete_chat:
                                    int index = doesChatMessageExist(chatMessage);

                                    if(index > -1) {
                                        if(index == m_list.size() - 1 && m_list.size() - 2 > -1)
                                            FirebaseDatabase.getInstance().getReference("messagepreviews")
                                                    .child(preview.isGroupchat() ? "group_" + preview.getGroupname() : preview.getChatters().get(0).getBrukernavn() + "_" +
                                                            preview.getChatters().get(1).getBrukernavn()).setValue(new ChatPreview(m_list.get(m_list.size() - 2).getMessage(),
                                                    preview.isGroupchat() ? preview.getGroupname() : "", preview.isGroupchat(), m_list.get(m_list.size() - 2).getTime(), preview.getChatters()));
                                        else if(m_list.size() - 2 <= -1)
                                            FirebaseDatabase.getInstance().getReference("messagepreviews")
                                                    .child(preview.isGroupchat() ? "group_" + preview.getGroupname() : preview.getChatters().get(0).getBrukernavn() + "_" +
                                                            preview.getChatters().get(1).getBrukernavn()).setValue(new ChatPreview("", preview.isGroupchat() ? preview.getGroupname() : "",
                                                    preview.isGroupchat(), new Date().getTime(), preview.getChatters()));

                                        m_list.remove(index);
                                        layout.removeViewAt(index + getAmountPreviousFooters(index, layout));
                                        removeFooterIfInvalid(layout);
                                        FirebaseDatabase.getInstance().getReference("messages").child(preview.getChatters().get(0).getBrukernavn() + "_" + preview.getChatters().get(1).getBrukernavn()).setValue(m_list);
                                    }

                                    return true;

                                default:
                                    return true;
                            }
                        }
                    });

                    popupMenu.inflate(R.menu.more_options_chatmessage);
                    popupMenu.show();

                    return true;
                }
            });

            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(v.getLayoutParams());

            params.setMargins(0, 0, 0, 10);

            v.setLayoutParams(params);

            message.setText(chatMessage.getMessage());
            message.setTypeface(MainActivity.typeface);

            if(chatMessage.getTime() > 0) {
                GregorianCalendar calendar = new GregorianCalendar(), today = new GregorianCalendar();
                calendar.setTime(new Date(chatMessage.getTime()));
                today.setTime(new Date(System.currentTimeMillis()));

                if(chattime != Utilities.getDateStrChat(calendar) || !lastfooter.equals(getLastFooter(layout))) {
                    chattime = Utilities.getDateStrChat(calendar);

                    amountChatTime++;

                    View footer = inflater.inflate(R.layout.chatmessage_footer, layout, false);

                    TextView text = footer.findViewById(R.id.text_footer);

                    text.setTypeface(MainActivity.typeface);

                    switch (chattime) {
                        case 1:
                            text.setText("Today");
                            break;

                        case 2:
                            text.setText("Yesterday");
                            break;

                        case 3:
                            text.setText(String.format(Locale.ENGLISH, "%s", calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())));
                            break;

                        case 4:
                            text.setText(String.format(Locale.ENGLISH, "%d %s", calendar.get(Calendar.DAY_OF_MONTH), calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())));
                            break;

                        case 5:
                            text.setText(String.format(Locale.ENGLISH, "%d %s %d", calendar.get(Calendar.DAY_OF_MONTH), calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()), calendar.get(Calendar.YEAR)));
                            break;

                        default:
                            break;
                    }

                    if(chattime != 0)
                        layout.addView(footer);

                }

                chattime = Utilities.getDateStrChat(calendar);
                lastfooter = getLastFooter(layout);
            }

            if(index != null)
                layout.addView(v, index);
            else
                layout.addView(v);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }, 100);
        }
    }
}
