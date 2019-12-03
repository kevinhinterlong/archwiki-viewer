package com.jtmcn.archwiki.viewer

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.jtmcn.archwiki.viewer.data.SearchResult
import com.jtmcn.archwiki.viewer.data.getSearchQuery
import com.jtmcn.archwiki.viewer.tasks.Fetch
import com.jtmcn.archwiki.viewer.utils.getTextZoom
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var searchMenuItem: MenuItem
    private var currentSuggestions: List<SearchResult>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        wikiViewer.buildView(progressBar, supportActionBar)

        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        updateWebSettings()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) {
            return
        }

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            wikiViewer.passSearch(query!!)
            hideSearchView()
        } else if (Intent.ACTION_VIEW == intent.action) {
            val url = intent.dataString
            wikiViewer.wikiClient.shouldOverrideUrlLoading(wikiViewer, url!!)
        }
    }

    /**
     * Update the font size used in the webview.
     */
    private fun updateWebSettings() {
        wikiViewer.settings.textZoom = getTextZoom(this)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchMenuItem = menu.findItem(R.id.menu_search)
        searchView = searchMenuItem.actionView as SearchView
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hideSearchView()
            }
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                wikiViewer.passSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    setCursorAdapter(ArrayList())
                    return true
                } else {
                    val searchUrl = getSearchQuery(newText)
                    Fetch.search({
                        currentSuggestions = it
                        setCursorAdapter(currentSuggestions)
                    }, searchUrl)
                    return true
                }
            }
        })

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val (pageName, pageURL) = currentSuggestions!![position]
                Timber.d("Opening '$pageName' from search suggestion.")
                wikiViewer.wikiClient.shouldOverrideUrlLoading(wikiViewer, pageURL)
                hideSearchView()
                return true
            }
        })
        return true
    }

    private fun hideSearchView() {
        searchMenuItem.collapseActionView()
        wikiViewer.requestFocus() //pass control back to the wikiview
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_share -> {
                val wikiPage = wikiViewer.currentWebPage
                if (wikiPage != null) {
                    val sharingIntent = Intent()
                    sharingIntent.type = TEXT_PLAIN_MIME
                    sharingIntent.action = Intent.ACTION_SEND
                    sharingIntent.putExtra(Intent.EXTRA_TITLE, wikiPage.pageTitle)
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, wikiPage.pageUrl)
                    startActivity(Intent.createChooser(sharingIntent, null))
                }
            }
            R.id.refresh -> wikiViewer.onRefresh()
            R.id.menu_settings -> startActivity(Intent(this, PreferencesActivity::class.java))
            R.id.exit -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setCursorAdapter(currentSuggestions: List<SearchResult>?) {
        searchView.suggestionsAdapter = SearchResultsAdapter.getCursorAdapter(this, currentSuggestions!!)
    }
}