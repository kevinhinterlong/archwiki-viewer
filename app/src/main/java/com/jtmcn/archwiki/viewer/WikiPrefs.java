package com.jtmcn.archwiki.viewer;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * The {@link PreferenceActivity} to change settings for the application.
 */
public class WikiPrefs extends PreferenceActivity {
	public static final String KEY_TEXT_SIZE = "textSize";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new ApplicationPreferenceFragment())
				.commit();
	}


	/**
	 * Loads the activities preferences into the fragment.
	 */
	public static class ApplicationPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle bundle) {
			super.onCreate(bundle);

			//sets default values if they haven't been set before
			PreferenceManager.setDefaultValues(getActivity(), R.xml.prefs, false);

			addPreferencesFromResource(R.xml.prefs);

		}
	}

}
