package com.jtmcn.archwiki.viewer

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.ShareActionProvider
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebSettings
import com.jtmcn.archwiki.viewer.data.SearchResult
import com.jtmcn.archwiki.viewer.data.getSearchQuery
import com.jtmcn.archwiki.viewer.data.parseSearchResults
import com.jtmcn.archwiki.viewer.utils.NetworkUtils
import com.jtmcn.archwiki.viewer.utils.getFontSize
import com.jtmcn.archwiki.viewer.utils.shareText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    private var shareActionProvider: ShareActionProvider? = null
    private lateinit var searchView: SearchView
    private lateinit var searchMenuItem: MenuItem
    private var currentSuggestions: List<SearchResult> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        wikiViewer.buildView(progressBar, supportActionBar)

        onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        updateWebSettings()
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent == null) {
            return
        }

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            wikiViewer.passSearch(query)
            hideSearchView()
        } else if (Intent.ACTION_VIEW == intent.action) {
            val url = intent.dataString
            wikiViewer.loadUrl(url)
        }
    }

    /**
     * Update the font size used in the webview.
     */
    fun updateWebSettings() {
        val webSettings = wikiViewer.settings
        val fontSize = getFontSize(this)

        //todo this setting should be changed to a slider, remove deprecated call
        // deprecated method must be used until Android API 14
        // https://developer.android.com/reference/android/webkit/WebSettings.TextSize.html#NORMAL
        webSettings.textSize = when (fontSize) {
            0 -> WebSettings.TextSize.SMALLEST //50%
            1 -> WebSettings.TextSize.SMALLER //75%
            2 -> WebSettings.TextSize.NORMAL //100%
            3 -> WebSettings.TextSize.LARGER //150%
            4 -> WebSettings.TextSize.LARGEST //200%
            else -> WebSettings.TextSize.NORMAL
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchMenuItem = menu.findItem(R.id.menu_search)
        searchView = MenuItemCompat.getActionView(searchMenuItem) as SearchView
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
                    NetworkUtils.fetchURL(searchUrl, object : Callback {
                        override fun onFailure(call: Call?, e: IOException?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onResponse(call: Call?, response: Response?) {
                            runOnUiThread {
                                val searchResults = parseSearchResults(response?.body()?.string()
                                        ?: "")
                                onFinish(searchResults)
                            }
                        }
                    })
                    return true
                }
            }
        })

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val (pageName, pageUrl) = currentSuggestions[position]
                Timber.d("Opening '$pageName' from search suggestion.")
                wikiViewer.loadUrl(pageUrl)
                hideSearchView()
                return true
            }
        })
        return true
    }

    fun hideSearchView() {
        searchMenuItem.collapseActionView()
        wikiViewer.requestFocus() //pass control back to the wikiview
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val share = menu.findItem(R.id.menuShare)
        shareActionProvider = MenuItemCompat.getActionProvider(share) as ShareActionProvider
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuShare -> {
                val wikiPage = wikiViewer.currentWebPage
                if (wikiPage != null) {
                    val intent = shareText(wikiPage.pageTitle!!, wikiPage.pageUrl, this)
                    shareActionProvider!!.setShareIntent(intent)
                }
            }
            R.id.refresh -> wikiViewer.onRefresh()
            R.id.menu_settings -> startActivity(Intent(this, PreferencesActivity::class.java))
            R.id.exit -> finish()
        }
        return true
    }


    fun onFinish(results: List<SearchResult>) {
        currentSuggestions = results
        setCursorAdapter(currentSuggestions)
    }

    private fun setCursorAdapter(currentSuggestions: List<SearchResult>?) {
        searchView.suggestionsAdapter = SearchResultsAdapter.getCursorAdapter(this, currentSuggestions!!)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (wikiViewer.canGoBack()) {
                        wikiViewer.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }

        }
        return super.onKeyDown(keyCode, event)
    }
}