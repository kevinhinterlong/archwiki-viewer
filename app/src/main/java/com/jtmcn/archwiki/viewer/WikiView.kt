package com.jtmcn.archwiki.viewer

import android.content.Context
import android.os.Build
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBar
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebSettings
import android.widget.ProgressBar
import com.github.takahirom.webview_in_coodinator_layout.NestedWebView
import com.jtmcn.archwiki.viewer.data.WikiPage

class WikiView(context: Context, attrs: AttributeSet) : NestedWebView(context, attrs), SwipeRefreshLayout.OnRefreshListener {
    val TAG = WikiView::class.java.simpleName
    internal lateinit var wikiClient: WikiClient

    /**
     * Returns the current [WikiPage] being shown or null.
     *
     * @return current wiki page being shown.
     */
    val currentWebPage: WikiPage?
        get() = wikiClient.currentWebPage

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !isInEditMode) {
            //this allows the webview to inject the css (otherwise it blocks it for security reasons)
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
            Log.i(TAG, "Loading previous page.")
            Log.d(TAG, "Position on page currently at $scrollY")
            wikiClient.goBackHistory()
            return true
        } else {
            Log.d(TAG, "Passing up button press.")
            return super.onKeyDown(keyCode, event)
        }
    }

    /**
     * Performs a search against the wiki.
     *
     * @param query the text to search for.
     */
    fun passSearch(query: String) {
        Log.d(TAG, "Searching for $query")
        val searchUrl = String.format(ARCHWIKI_SEARCH_URL, query)
        wikiClient.shouldOverrideUrlLoading(this, searchUrl)
    }

    override fun onRefresh() {
        wikiClient.refreshPage()
        stopLoading()
    }
}
