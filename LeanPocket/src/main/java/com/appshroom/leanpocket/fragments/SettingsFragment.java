package com.appshroom.leanpocket.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.helpers.Consts;

/**
 * Created by jpetrakovich on 9/14/13.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        setCardPositionSummary();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(Consts.SHARED_PREFS_CARD_POSITION)) {

            setCardPositionSummary();
        }
    }

    private void setCardPositionSummary() {

        Preference pref = findPreference(Consts.SHARED_PREFS_CARD_POSITION);
        String pos = getPreferenceScreen().getSharedPreferences().getString(Consts.SHARED_PREFS_CARD_POSITION, getString(R.string.top));

        if (pos.equals(getString(R.string.top))) {

            pref.setSummary(R.string.top_of_lane);

        } else {

            pref.setSummary(R.string.bottom_of_lane);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
