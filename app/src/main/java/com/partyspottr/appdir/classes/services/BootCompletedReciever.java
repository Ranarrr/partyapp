package com.partyspottr.appdir.classes.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent startService = new Intent(context, NotificationService.class);
            context.startService(startService);
        }
    }
}
