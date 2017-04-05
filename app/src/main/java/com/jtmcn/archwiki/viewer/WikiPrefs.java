package com.jtmcn.archwiki.viewer;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
//import android.webkit.WebSettings;
//// TODO: 4/4/2017 change to new preference setup and remove deprecated calls
public class WikiPrefs extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	public static final String KEY_LIST_PREFERENCE = "listPref";
	ListPreference listPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// this is deprecated, should be using a fragment
		addPreferencesFromResource(R.xml.prefs);

		PreferenceManager.setDefaultValues(getApplicationContext(),
				R.xml.prefs, false);

		listPreference = (ListPreference) getPreferenceScreen().findPreference(
				KEY_LIST_PREFERENCE);

		listPreference.setSummary(listPreference.getEntry());
	}

	@Override
	protected void onResume() {
		super.onResume();

		listPreference.setSummary(listPreference.getEntry());

		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		PreferenceManager.getDefaultSharedPreferences(this)
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
										  String key) {

		listPreference.setSummary(listPreference.getEntry());

	}
}
