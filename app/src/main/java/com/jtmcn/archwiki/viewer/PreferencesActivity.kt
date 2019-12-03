package com.jtmcn.archwiki.viewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.jtmcn.archwiki.viewer.utils.getTextZoom
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

        getTextZoom(this)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }
    }
}

object Prefs {
    @Deprecated(message = "Should use textZoom", replaceWith = ReplaceWith("KEY_TEXT_ZOOM"))
    const val KEY_TEXT_SIZE = "textSize"
    const val KEY_TEXT_ZOOM = "textZoom"
}