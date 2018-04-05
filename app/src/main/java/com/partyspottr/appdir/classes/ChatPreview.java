package com.partyspottr.appdir.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ranarrr on 20-Mar-18.
 *
 * @author Ranarrr
 */

public class ChatPreview {
    private boolean groupchat;
    private String groupname;
    private String message;
    private List<Chatter> chatters;
    private long lastmsg;

    // used for dataSnapshot.getValue, needing a default constructor
    public ChatPreview() {
        this("", "", false, 0, new ArrayList<Chatter>());
    }

    public ChatPreview(String msg, String groupnam, boolean isgroup, List<Chatter> list) {
        chatters = list;
        message = msg;
        groupname = groupnam;
        groupchat = isgroup;
        lastmsg = new Date().getTime();
    }

    public ChatPreview(String msg, String groupnam, boolean isgroup, long lastmessage, List<Chatter> chatterList) {
        chatters = chatterList;
        message = msg;
        groupname = groupnam;
        groupchat = isgroup;
        lastmsg = lastmessage;
    }

    public static ChatPreview findPreviewInList(String chatter1, String chatter2, List<ChatPreview> list) {
        for(ChatPreview temp : list) {
            if(temp.getChatters().get(0).getBrukernavn().equals(chatter1) && temp.getChatters().get(1).getBrukernavn().equals(chatter2))
                return temp;
        }

        return null;
    }

    public void addChatter(Chatter chatter) {
        chatters.add(chatter);
    }

    public void removeChatter(Chatter chatter) {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Chatter> getChatters() {
        return chatters;
    }

    public void setChatters(List<Chatter> chatters) {
        this.chatters = chatters;
    }

    public boolean isGroupchat() {
        return groupchat;
    }

    public void setGroupchat(boolean groupchat) {
        this.groupchat = groupchat;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public long getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(long lastmsg) {
        this.lastmsg = lastmsg;
    }
}
