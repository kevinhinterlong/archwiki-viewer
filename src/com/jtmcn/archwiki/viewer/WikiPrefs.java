package com.jtmcn.archwiki.viewer;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class WikiPrefs extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// this is deprecated, should be using a fragment
		addPreferencesFromResource(R.xml.settings);

	}

}
