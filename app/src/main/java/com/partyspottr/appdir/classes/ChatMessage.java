package com.partyspottr.appdir.classes;

import java.util.Date;

/**
 * Created by Ranarrr on 27-Mar-18.
 *
 * @author Ranarrr
 */

public class ChatMessage {
    private String message;
    private String bruker;
    private long time;

    public ChatMessage() {
        message = "";
        bruker = "";
        time = new Date().getTime();
    }

    public ChatMessage(String msg, String user) {
        message = msg;
        bruker = user;

        time = new Date().getTime();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getBruker() {
        return bruker;
    }

    public void setBruker(String bruker) {
        this.bruker = bruker;
    }
}