package com.jtmcn.archwiki.viewer

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.KeyEvent
import android.webkit.WebSettings
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.takahirom.webview_in_coodinator_layout.NestedWebView
import com.jtmcn.archwiki.viewer.data.WikiPage
import timber.log.Timber

class WikiView(context: Context, attrs: AttributeSet) : NestedWebView(context, attrs), SwipeRefreshLayout.OnRefreshListener {
    lateinit var wikiClient: WikiClient

    /**
     * Returns the current [WikiPage] being shown or null.
     *
     * @return current wiki page being shown.
     */
    val currentWebPage: WikiPage?
        get() = wikiClient.currentWebPage

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !isInEditMode) {
            // This allows the webview to inject the css (otherwise it blocks it for security reasons)
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }

    /**
     * Initializes the wiki client and loads the main page.
     */
    fun buildView(progressBar: ProgressBar, actionBar: ActionBar?) {
        wikiClient = WikiClient(progressBar, actionBar, this)
        webViewClient = wikiClient
        wikiClient.shouldOverrideUrlLoading(this, ARCHWIKI_MAIN)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && wikiClient.historyStackSize > 1) {
            Timber.i("Loading previous page.")
            Timber.d("Position on page currently at $scrollY")
            wikiClient.goBackHistory()
            return true
        } else {
            Timber.d("Passing up button press.")
            return super.onKeyDown(keyCode, event)
        }
    }

    /**
     * Performs a search against the wiki.
     *
     * @param query the text to search for.
     */
    fun passSearch(query: String) {
        Timber.d("Searching for $query")
        val searchUrl = String.format(ARCHWIKI_SEARCH_URL, query)
        wikiClient.shouldOverrideUrlLoading(this, searchUrl)
    }

    override fun onRefresh() {
        wikiClient.refreshPage()
        stopLoading()
    }
}
