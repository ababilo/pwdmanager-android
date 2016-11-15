package com.ababilo.pwd.pwdmanager.util;

import android.content.Context;
import android.view.MenuItem;

import com.ababilo.pwd.pwdmanager.R;

/**
 * Created by ababilo on 14.11.16.
 */

public class LedController {

    private Context context;
    private MenuItem led;

    public enum State {
        DISCONNECTED(R.drawable.ic_little_white),
        CONNECTED(R.drawable.ic_little_green),
        WAITING(R.drawable.ic_little_yellow),
        ERROR(R.drawable.ic_little_red);

        private int resource;

        State(int resource) {
            this.resource = resource;
        }
    }

    public LedController(Context context, MenuItem led) {
        this.context = context;
        this.led = led;
    }

    public void setState(State state) {
        if (null != led) {
            led.setIcon(context.getResources().getDrawable(state.resource, context.getTheme()));
        }
    }
}
