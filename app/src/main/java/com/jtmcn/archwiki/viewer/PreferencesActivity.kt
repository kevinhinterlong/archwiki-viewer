package com.jtmcn.archwiki.viewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.toolbar.*

class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        setSupportActionBar(toolbar)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        if(prefs.contains(Prefs.KEY_TEXT_SIZE)) {
            val textZoom = when(Integer.valueOf(prefs.getString(Prefs.KEY_TEXT_SIZE, "2")!!)) {
                0 -> 50
                1 -> 75
                2 -> 100
                3 -> 150
                4 -> 200
                else -> 100
            }
            prefs.edit()
                    .putInt(Prefs.KEY_TEXT_ZOOM, textZoom)
                    .remove(Prefs.KEY_TEXT_SIZE)
                    .apply();

        }

    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }
    }
}

object Prefs {
    @Deprecated(message = "Should use textZoom")
    const val KEY_TEXT_SIZE = "textSize"
    const val KEY_TEXT_ZOOM = "textZoom"
}