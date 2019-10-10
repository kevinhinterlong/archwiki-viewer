package com.jtmcn.archwiki.viewer.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.jtmcn.archwiki.viewer.Prefs

fun getFontSize(context: Context): Int {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    // https://stackoverflow.com/questions/11346916/listpreference-use-string-array-as-entry-and-integer-array-as-entry-values-does
    // the value of this preference must be parsed as a string
    val fontSizePref = prefs.getString(Prefs.KEY_TEXT_SIZE, "2")!!
    return Integer.valueOf(fontSizePref)

}

fun getFontZoom(context: Context): Int {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    return prefs.getInt(Prefs.KEY_TEXT_ZOOM, 100)
}
