package com.slowna.game;

import android.app.Activity;
import android.widget.Toast;
import com.slowna.game.extra.Notificator;

public class AndroidNotificator implements Notificator {

    private final Activity activity;

    public AndroidNotificator(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void notify(String text) {
        activity.runOnUiThread(() -> Toast.makeText(activity, text, Toast.LENGTH_SHORT).show());

    }
}
