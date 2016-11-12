package com.ababilo.pwd.pwdmanager.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.IntentCompat;
import android.view.View;

import com.ababilo.pwd.pwdmanager.ui.MainActivity;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by ababilo on 12.11.16.
 */

public class ActivityUtil {

    public static void loadActivity(Context base, Class<? extends Activity> load) {
        Intent intent = new Intent(base, load);
        base.startActivity(intent);
    }

    public static void loadActivity(Activity base, Class<? extends Activity> load, int animIn, int animOut) {
        loadActivity(base, load);
        base.overridePendingTransition(animIn, animOut);
    }

    public static void loadActivity(Activity base, Class<? extends Activity> load, Map<String, ? extends Serializable> params, int animIn, int animOut) {
        loadActivity(base, load, params);
        base.overridePendingTransition(animIn, animOut);
    }

    public static void loadMainActivity(Context base) {
        loadRootActivity(base, MainActivity.class);
    }

    public static void loadRootActivity(Context base, Class<? extends Activity> load) {
        Intent intent = new Intent(base, load);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        base.startActivity(intent);
    }

    public static void loadActivity(Context base, Class<? extends Activity> load, Map<String, ? extends Serializable> params) {
        Intent intent = new Intent(base, load);
        for (Map.Entry<String, ? extends Serializable> param : params.entrySet()) {
            intent.putExtra(param.getKey(), param.getValue());
        }
        base.startActivity(intent);
    }

    public static void showProgress(final Activity base, final View progress, final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = base.getResources().getInteger(android.R.integer.config_shortAnimTime);
            progress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public static void showFullScreenProgress(final Activity base, final View form, final View progress, final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = base.getResources().getInteger(android.R.integer.config_shortAnimTime);
            form.setVisibility(show ? View.GONE : View.VISIBLE);
            form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    form.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            progress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int target) {
        fragmentManager.beginTransaction()
                .replace(target, fragment)
                .commit();
    }
}
