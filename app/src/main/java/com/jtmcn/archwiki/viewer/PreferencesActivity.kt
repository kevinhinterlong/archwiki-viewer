package com.jtmcn.archwiki.viewer

import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.toolbar.*


/**
 * The [PreferenceActivity] to change settings for the application.
 */
class PreferencesActivity : AppCompatActivity() {
    companion object {
        const val KEY_TEXT_SIZE = "textSize"
    }

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

        setSupportActionBar(toolbar)
        setTitle(R.string.menu_settings)

        with(supportActionBar) {
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setHomeButtonEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> onBackPressed()
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
}
