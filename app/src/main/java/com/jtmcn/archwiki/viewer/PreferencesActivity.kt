package com.jtmcn.archwiki.viewer

import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem

/**
 * The [PreferenceActivity] to change settings for the application.
 */
class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        fragmentManager.beginTransaction()
                .replace(R.id.settings_content, ApplicationPreferenceFragment())
                .commit()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setTitle(R.string.menu_settings)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    /**
     * Loads the activities preferences into the fragment.
     */
    class ApplicationPreferenceFragment : PreferenceFragment() {
        override fun onCreate(bundle: Bundle?) {
            super.onCreate(bundle)
            addPreferencesFromResource(R.xml.prefs)
        }
    }

    companion object {
        val KEY_TEXT_SIZE = "textSize"
    }
}
