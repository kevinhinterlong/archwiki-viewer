package com.jtmcn.archwiki.viewer

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBar
import android.util.AttributeSet
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.github.takahirom.webview_in_coodinator_layout.NestedWebView
import com.jtmcn.archwiki.viewer.data.WikiPage
import com.jtmcn.archwiki.viewer.data.buildPage
import com.jtmcn.archwiki.viewer.utils.NetworkUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import timber.log.Timber
import java.io.IOException


class WikiView(context: Context, attrs: AttributeSet) : NestedWebView(context, attrs), SwipeRefreshLayout.OnRefreshListener {
    /**
     * Returns the current [WikiPage] being shown or null.
     *
     * @return current wiki page being shown.
     */
    val currentWebPage: WikiPage?
        get() = null // wikiClient.currentWebPage

    var actionBar: ActionBar? = null

    /**
     * Initializes the wiki client and loads the main page.
     */
    fun buildView(progressBar: ProgressBar, actionBar: ActionBar?) {
        this.actionBar = actionBar
        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url
                var urlString = url?.toString()
                if (urlString == null || !isArchWikiDomain(urlString) && !urlString.startsWith("file://")) {
                    return super.shouldOverrideUrlLoading(view, request)
                }

                if (urlString.startsWith("file://")) {
                    urlString = ARCHWIKI_BASE + "index.php/${url?.lastPathSegment?.toString()}"
                }
                loadArchWikiPage(urlString)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE

                actionBar?.subtitle = view?.title
            }
        }

        loadArchWikiPage(ARCHWIKI_MAIN)
    }

    fun isArchWikiDomain(url: String? = getUrl()): Boolean {
        return url?.startsWith(ARCHWIKI_BASE) == true
    }

    private fun loadArchWikiPage(url: String) {
        Timber.d("Loading new wikipage $url")
        fetchPage(url) {
            Timber.d("page url is ${it.pageUrl}")
            loadDataWithBaseURL(
                    "file:///android_asset/",
                    it.html,
                    TEXT_HTML_MIME,
                    UTF_8,
                    it.pageUrl
            )
            actionBar?.subtitle = it.pageTitle
        }
    }


    private fun fetchPage(url: String, onFinish: (WikiPage) -> Unit = {}) {
        NetworkUtils.fetchURL(url, object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                TODO("not implemented")
            }

            override fun onResponse(call: Call?, response: Response?) {
                Handler(Looper.getMainLooper()).post {
                    val html = StringBuilder(response?.body()?.string())
                    response?.body()?.close()

                    val wikiPage = buildPage(url, html)
                    onFinish(wikiPage)
                }
            }
        })
    }

    fun passSearch(query: String) {
        Timber.d("Searching for $query")
        val searchUrl = String.format(ARCHWIKI_SEARCH_URL, query)
        loadUrl(searchUrl)
    }

    override fun onRefresh() {
        reload()
    }
}
