package com.partyspottr.appdir.classes.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class ApplicationLifecycleMgr implements Application.ActivityLifecycleCallbacks {
    private static int visibleActivityCount = 0;
    private static int foregroundActivityCount = 0;

    public static boolean isAppInForeground() {
        return foregroundActivityCount > 0;
    }

    public static boolean isAppVisible() {
        return visibleActivityCount > 1;
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
        foregroundActivityCount++;
    }

    public void onActivityPaused(Activity activity) {
        foregroundActivityCount--;
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityStarted(Activity activity) {
        visibleActivityCount++;
    }

    public void onActivityStopped(Activity activity) {
        visibleActivityCount--;
    }
}