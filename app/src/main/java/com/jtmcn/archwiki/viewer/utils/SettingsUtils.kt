package com.jtmcn.archwiki.viewer.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.jtmcn.archwiki.viewer.Prefs

@Deprecated("Use getTextZoom", replaceWith = ReplaceWith("getTextZoom(this)"))
fun getTextSize(prefs: SharedPreferences): Int? {
    if(!prefs.contains(Prefs.KEY_TEXT_SIZE)) {
        return null
    }

    // https://stackoverflow.com/questions/11346916/listpreference-use-string-array-as-entry-and-integer-array-as-entry-values-does
    // the value of this preference must be parsed as a string
    val fontSizePref = prefs.getString(Prefs.KEY_TEXT_SIZE, "2")!!
    return Integer.valueOf(fontSizePref)

}

@Deprecated("Use getTextZoom")
fun textSizeToTextZoom(fontSize: Int) = when(fontSize) {
    0 -> 50
    1 -> 75
    2 -> 100
    3 -> 150
    4 -> 200
    else -> 100
}

/**
 * gets the [Prefs.KEY_TEXT_ZOOM] preference, and if needed migrates from [getTextSize=]
 */
fun getTextZoom(context: Context): Int {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val textSize = getTextSize(prefs)
    if(textSize != null) {
        val textZoom = textSizeToTextZoom(textSize)
        prefs.edit()
            .putInt(Prefs.KEY_TEXT_ZOOM, textZoom)
            .remove(Prefs.KEY_TEXT_SIZE)
            .apply()

        return textZoom
    }

    return prefs.getInt(Prefs.KEY_TEXT_ZOOM, 100)
}
