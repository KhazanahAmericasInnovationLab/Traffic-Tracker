package com.example.wmmc88.traffictracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

//TODO Permission Requests

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mCurrentSettingsTextView;

    private int mLaunchMode;
    private static final int VIDEO_LAUNCH = 0;
    private static final int CAMERA_LAUNCH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mCurrentSettingsTextView = findViewById(R.id.tv_current_settings);

        Button mLaunchButton = findViewById(R.id.b_launch);
        Button mConfigureSettingsButton = findViewById(R.id.b_configure_settings);

        mLaunchButton.setOnClickListener(this);
        mConfigureSettingsButton.setOnClickListener(this);
        setupSettingsTextView();
        mLaunchMode = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_key_camera_selection), null));
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick");
        int clickViewId = view.getId();
        switch (clickViewId) {
            case R.id.b_launch:
                Log.v(TAG, "b_launch");

                switch (mLaunchMode) {
                    case VIDEO_LAUNCH:
                        Intent intentToStartVideoMode = new Intent(MainActivity.this, VideoActivity.class);
                        startActivity(intentToStartVideoMode);
                        break;

                    case CAMERA_LAUNCH:
                        Intent intentToStartCameraMode = new Intent(MainActivity.this, CameraActivity.class);
                        startActivity(intentToStartCameraMode);
                        break;

                    default:
                        Log.e(TAG, "INVALID LAUNCH MODE");
                        break;
                }
                break;

            case R.id.b_configure_settings:
                Log.v(TAG, "b_configure_settings");
                Intent intentToOpenSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentToOpenSettings);
                break;
        }
    }

    private void setupSettingsTextView() {
        Log.d(TAG, "setupSettingsTextView");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        updateSettingsTextView(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(MainActivity.this);
    }

    private void updateSettingsTextView(SharedPreferences sharedPreferences) {
        Log.d(TAG, "updateSettingsTextView");

        Map<String, ?> settings = sharedPreferences.getAll();

        String settingsString = "";
        for (Map.Entry<String, ?> setting : settings.entrySet()) {
            settingsString += setting.getKey() + ": \t" + setting.getValue() + '\n';
        }
        mCurrentSettingsTextView.setText(settingsString);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(TAG, "onSharedPreferenceChanged");
        updateSettingsTextView(sharedPreferences);
        mLaunchMode = Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_key_camera_selection), null));
    }
}
