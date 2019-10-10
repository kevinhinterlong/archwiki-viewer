package com.jtmcn.archwiki.viewer.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.jtmcn.archwiki.viewer.PreferencesActivity

fun getFontSize(context: Context): Int {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    // https://stackoverflow.com/questions/11346916/listpreference-use-string-array-as-entry-and-integer-array-as-entry-values-does
    // the value of this preference must be parsed as a string
    val fontSizePref = prefs.getString(PreferencesActivity.KEY_TEXT_SIZE, "2")
    return Integer.valueOf(fontSizePref!!)

}
