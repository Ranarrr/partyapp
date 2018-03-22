package com.partyspottr.appdir.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 20-Mar-18.
 *
 * @author Ranarrr
 */

public class ChatMessage {
    private long userid;
    private String message;
    private boolean isread;
    private boolean isdelivered;
    private List<Chatter> chatters;

    public ChatMessage() {
        this("", false, false, new ArrayList<Chatter>());
    }

    private ChatMessage(String msg, boolean read, boolean delivered, List<Chatter> chatterList) {
        chatters = chatterList;
        message = msg;
        isread = read;
        isdelivered = delivered;
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

    public boolean isIsread() {
        return isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }

    public boolean isIsdelivered() {
        return isdelivered;
    }

    public void setIsdelivered(boolean isdelivered) {
        this.isdelivered = isdelivered;
    }

    public List<Chatter> getChatters() {
        return chatters;
    }

    public void setChatters(List<Chatter> chatters) {
        this.chatters = chatters;
    }
}
