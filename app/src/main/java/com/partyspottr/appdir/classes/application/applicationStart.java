package com.partyspottr.appdir.classes.application;

import android.app.Application;

public class applicationStart extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ApplicationLifecycleMgr());
    }
}