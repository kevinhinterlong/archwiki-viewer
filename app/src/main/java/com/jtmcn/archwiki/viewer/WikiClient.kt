package com.jtmcn.archwiki.viewer

import android.os.Handler
import android.support.v7.app.ActionBar
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.jtmcn.archwiki.viewer.data.WikiPage
import com.jtmcn.archwiki.viewer.tasks.Fetch
import com.jtmcn.archwiki.viewer.tasks.OnFinish
import com.jtmcn.archwiki.viewer.utils.openLink
import timber.log.Timber
import java.util.*

class WikiClient(private val progressBar: ProgressBar, private val actionBar: ActionBar?, private val webView: WebView) : WebViewClient() {
    private val webpageStack = Stack<WikiPage>()
    private val loadedUrls = HashSet<String>() // this is used to see if we should restore the scroll position
    private var lastLoadedUrl: String? = null //https://stackoverflow.com/questions/11601134/android-webview-function-onpagefinished-is-called-twice

    /**
     * Get the number of pages that are in the history.
     *
     * @return number of pages on the stack.
     */
    val historyStackSize: Int
        get() = webpageStack.size

    /**
     * Returns null or the current page.
     *
     * @return The current page
     */
    val currentWebPage: WikiPage?
        get() = if (webpageStack.size == 0) null else webpageStack.peek()

    /*
	 * Manage page history
	 */
    fun addHistory(wikiPage: WikiPage) {
        if (webpageStack.size > 0) {
            Timber.d("Saving " + currentWebPage!!.pageTitle + " at " + webView.scrollY)
            currentWebPage!!.scrollPosition = webView.scrollY
        }
        webpageStack.push(wikiPage)
        Timber.i("Adding page " + wikiPage.pageTitle + ". Stack size= " + webpageStack.size)
    }

    /**
     * Loads the html from a [WikiPage] into the webview.
     *
     * @param wikiPage the page to be loaded.
     */
    fun loadWikiHtml(wikiPage: WikiPage?) {
        webView.loadDataWithBaseURL(
                wikiPage!!.pageUrl,
                wikiPage.htmlString,
                TEXT_HTML_MIME,
                UTF_8, null
        )

        val sub = wikiPage.pageTitle ?: "No Title Found"
        setSubtitle(sub)
    }

    /**
     * Intercept url when clicked. If it's part of the wiki load it here.
     * If not, open the device's default browser.
     *
     * @param view webview being loaded into
     * @param url  url being loaded
     * @return true if should override url loading
     */
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        // deprecated until min api 21 is used
        if (url.startsWith(ARCHWIKI_BASE)) {
            webView.stopLoading()
            Fetch.page(this::onFinish, url)
            showProgress()

            return false
        } else {
            openLink(url, view.context)
            return true
        }
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        val currentWebPage = currentWebPage
        Timber.d("Calling onPageFinished(view, " + currentWebPage!!.pageTitle + ")")
        // make sure we're loading the current page and that
        // this page's url doesn't have an anchor (only on first page load)
        if (url == currentWebPage.pageUrl && url != lastLoadedUrl) {
            if (!isFirstLoad(currentWebPage)) {
                Handler().postDelayed({
                    val scrollY = currentWebPage.scrollPosition
                    Timber.d("Restoring " + currentWebPage.pageTitle + " at " + scrollY)
                    webView.scrollY = scrollY
                }, 25)
            }

            lastLoadedUrl = url
            hideProgress()
        }
    }

    private fun isFirstLoad(currentWebPage: WikiPage): Boolean {
        if (loadedUrls.contains(currentWebPage.pageUrl)) {
            return false
        } else {
            loadedUrls.add(currentWebPage.pageUrl)
            return true
        }
    }

    fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    fun setSubtitle(title: String) {
        actionBar?.setSubtitle(title)
    }

    /**
     * Go back to the last loaded page.
     */
    fun goBackHistory() {
        val (pageUrl, pageTitle) = webpageStack.pop()
        loadedUrls.remove(pageUrl)
        Timber.i("Removing $pageTitle from stack")
        val newPage = webpageStack.peek()
        loadWikiHtml(newPage)
    }

    fun onFinish(results: WikiPage) {
        addHistory(results)
        loadWikiHtml(currentWebPage)
    }

    fun refreshPage() {
        lastLoadedUrl = null // set to null if page should restore position, otherwise start at top of page
        val currentWebPage = currentWebPage
        if (currentWebPage != null) {
            val scrollPosition = currentWebPage.scrollPosition

            val url = currentWebPage.pageUrl
            showProgress()
            Fetch.page(object : OnFinish<WikiPage> {
                override fun invoke(wikiPage: WikiPage) {
                    webpageStack.pop()
                    webpageStack.push(wikiPage)
                    wikiPage.scrollPosition = scrollPosition
                    loadWikiHtml(wikiPage)
                }
            }, url)
        }
    }
}