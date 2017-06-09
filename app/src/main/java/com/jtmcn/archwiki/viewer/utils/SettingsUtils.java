package com.jtmcn.archwiki.viewer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jtmcn.archwiki.viewer.PreferencesActivity;

/**
 * Created by kevin on 6/7/2017.
 */

public class SettingsUtils {

	public static int getFontSize(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		// https://stackoverflow.com/questions/11346916/listpreference-use-string-array-as-entry-and-integer-array-as-entry-values-does
		// the value of this preference must be parsed as a string
		String fontSizePref = prefs.getString(PreferencesActivity.KEY_TEXT_SIZE, "2");
		return Integer.valueOf(fontSizePref);

	}
}
