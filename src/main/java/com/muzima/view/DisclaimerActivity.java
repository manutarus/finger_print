/*
 * Copyright (c) 2014. The Trustees of Indiana University.
 *
 * This version of the code is licensed under the MPL 2.0 Open Source license with additional
 * healthcare disclaimer. If the user is an entity intending to commercialize any application
 * that uses this code in a for-profit venture, please contact the copyright holder.
 */

package com.muzima.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import com.muzima.R;
import com.muzima.view.custom.ScrollViewWithDetection;
import com.muzima.view.login.LoginActivity;

/**
 * TODO: Write brief description about the class here.
 */
public class DisclaimerActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        final Button nextButton = (Button) findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                navigateToNextActivity();
            }
        });

        Button prevButton = (Button) findViewById(R.id.previous);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });

        ScrollViewWithDetection scrollViewWithDetection = (ScrollViewWithDetection) findViewById(R.id.disclaimer_scroller);
        scrollViewWithDetection.setOnBottomReachedListener(new ScrollViewWithDetection.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                nextButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void navigateToNextActivity() {
        // write the setting to mark the user have accepted the disclaimer
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String disclaimerKey = getResources().getString(R.string.preference_disclaimer);
        settings.edit().putBoolean(disclaimerKey, true).commit();
        // transition to the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.isFirstLaunch, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}