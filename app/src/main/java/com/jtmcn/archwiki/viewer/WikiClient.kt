package com.jtmcn.archwiki.viewer

import android.os.Handler
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import com.jtmcn.archwiki.viewer.data.WikiPage
import com.jtmcn.archwiki.viewer.tasks.Fetch
import com.jtmcn.archwiki.viewer.utils.openLink
import timber.log.Timber
import java.util.*


class WikiClient(private val progressBar: ProgressBar, private val actionBar: ActionBar?, private val webView: WebView) : WebViewClient() {
    private val webPageStack = Stack<WikiPage>()
    private val loadedUrls = HashSet<String>() // this is used to see if we should restore the scroll position
    private var lastLoadedUrl: String? = null //https://stackoverflow.com/questions/11601134/android-webview-function-onpagefinished-is-called-twice

    /**
     * Get the number of pages that are in the history.
     *
     * @return number of pages on the stack.
     */
    val historyStackSize: Int
        get() = webPageStack.size

    /**
     * Returns null or the current page.
     *
     * @return The current page
     */
    val currentWebPage: WikiPage?
        get() = if (webPageStack.size == 0) null else webPageStack.peek()

    /*
	 * Manage page history
	 */
    private fun addHistory(wikiPage: WikiPage) {
        if (webPageStack.size > 0) {
            Timber.d("Saving ${currentWebPage?.pageTitle} at ${webView.scrollY}")
            currentWebPage!!.scrollPosition = webView.scrollY
        }
        webPageStack.push(wikiPage)
        Timber.i("Adding page ${wikiPage.pageTitle}. Stack size= ${webPageStack.size}")
    }

    /**
     * Loads the html from a [WikiPage] into the webview.
     *
     * @param wikiPage the page to be loaded.
     */
    fun loadWikiHtml(wikiPage: WikiPage) {
        webView.loadDataWithBaseURL(
                wikiPage.pageUrl,
                wikiPage.htmlString,
                TEXT_HTML_MIME,
                UTF_8,
                null
        )

        setSubtitle(wikiPage.pageTitle)
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
            Fetch.page({
                addHistory(it)
                loadWikiHtml(currentWebPage!!)
            }, url)
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
        Timber.d("Calling onPageFinished(view, ${currentWebPage?.pageTitle})")
        // make sure we're loading the current page and that
        // this page's url doesn't have an anchor (only on first page load)
        if (url == currentWebPage?.pageUrl && url != lastLoadedUrl) {
            if (!isFirstLoad(currentWebPage)) {
                Handler().postDelayed({
                    val scrollY = currentWebPage.scrollPosition
                    Timber.d("Restoring ${currentWebPage.pageTitle} at $scrollY")
                    webView.scrollY = scrollY
                }, 25)
            }

            lastLoadedUrl = url
            hideProgress()
        }
    }

    private fun isFirstLoad(currentWebPage: WikiPage): Boolean {
        return if (loadedUrls.contains(currentWebPage.pageUrl)) {
            false
        } else {
            loadedUrls.add(currentWebPage.pageUrl)
            true
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun setSubtitle(title: String?) {
        if (actionBar != null) {
            actionBar.subtitle = title
        }
    }

    /**
     * Go back to the last loaded page.
     */
    fun goBackHistory() {
        val (pageUrl, pageTitle) = webPageStack.pop()
        loadedUrls.remove(pageUrl)
        Timber.i("Removing $pageTitle from stack")
        val newPage = webPageStack.peek()
        loadWikiHtml(newPage)
    }

    fun refreshPage() {
        lastLoadedUrl = null // set to null if page should restore position, otherwise start at top of page
        val currentWebPage = currentWebPage
        if (currentWebPage != null) {
            val scrollPosition = currentWebPage.scrollPosition

            val url = currentWebPage.pageUrl
            showProgress()
            Fetch.page({ wikiPage ->
                webPageStack.pop()
                webPageStack.push(wikiPage)
                wikiPage.scrollPosition = scrollPosition
                loadWikiHtml(wikiPage)
            }, url)
        }
    }
}