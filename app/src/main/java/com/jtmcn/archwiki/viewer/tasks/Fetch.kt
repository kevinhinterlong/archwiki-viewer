package com.jtmcn.archwiki.viewer.tasks

import android.os.AsyncTask

import com.jtmcn.archwiki.viewer.data.SearchResult
import com.jtmcn.archwiki.viewer.data.*
import com.jtmcn.archwiki.viewer.data.WikiPage

/**
 * Wrapper for [FetchUrl] which gives an easy to use interface
 * for fetching [SearchResult] and [WikiPage].
 */
object Fetch {
    private val SEARCH_RESULTS_MAPPER = { _: String, html: StringBuilder -> parseSearchResults(html.toString()) }

    private val WIKI_PAGE_MAPPER = { url: String, html: StringBuilder -> buildPage(url, html) }

    /**
     * Fetches a List<SearchResults> from the url.
     *
     * @param onFinish The listener called when search results are ready.
     * @param url      The url to fetch the search results from.
     * @return the async task fetching the data.
     */
    fun search(onFinish: (List<SearchResult>) -> Unit, url: String): AsyncTask<String, Void, List<SearchResult>> {
        return FetchUrl(onFinish, SEARCH_RESULTS_MAPPER).execute(url)
    }

    /**
     * Fetches a [WikiPage] from the url.
     *
     * @param onFinish The listener called when the page is ready.
     * @param url      The url to fetch the page from.
     * @return the async task fetching the data.
     */
    fun page(onFinish: (WikiPage) -> Unit, url: String): AsyncTask<String, Void, WikiPage> {
        return FetchUrl(onFinish, WIKI_PAGE_MAPPER).execute(url)
    }
}
