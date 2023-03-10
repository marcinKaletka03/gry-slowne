package com.slowna.game;

import android.content.Context;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseManager {

    private final FirebaseAnalytics firebaseAnalytics;

    public FirebaseManager(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        logJoinEvent();
    }

    public void logJoinEvent() {
        Bundle bundle = new Bundle();
        bundle.putString("log", "log");
        firebaseAnalytics.logEvent("login", bundle);
    }
}
