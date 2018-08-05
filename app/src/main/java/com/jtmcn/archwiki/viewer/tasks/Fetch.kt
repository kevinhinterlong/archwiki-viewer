package com.jtmcn.archwiki.viewer.tasks

import android.os.AsyncTask
import com.jtmcn.archwiki.viewer.data.SearchResult
import com.jtmcn.archwiki.viewer.data.WikiPage
import com.jtmcn.archwiki.viewer.data.buildPage
import com.jtmcn.archwiki.viewer.data.parseSearchResults

/**
 * Wrapper for [FetchUrl] which gives an easy to use interface
 * for fetching [SearchResult] and [WikiPage].
 */
object Fetch {
    val SEARCH_RESULTS_MAPPER: FetchUrlMapper<List<SearchResult>> = { _, sb -> parseSearchResults(sb.toString()) }

    val WIKIPAGE_MAPPER: FetchUrlMapper<WikiPage> = { url, sb -> buildPage(url, sb) }

    /**
     * Fetches a [<] from the url.
     *
     * @param onFinish The listener called when search results are ready.
     * @param url      The url to fetch the search results from.
     * @return the async task fetching the data.
     */
    fun search(
            onFinish: OnFinish<List<SearchResult>>,
            url: String
    ): AsyncTask<String, Void, List<SearchResult>> {
        return FetchUrl(onFinish, SEARCH_RESULTS_MAPPER).execute(url)
    }

    /**
     * Fetches a [WikiPage] from the url.
     *
     * @param onFinish The listener called when the page is ready.
     * @param url      The url to fetch the page from.
     * @return the async task fetching the data.
     */
    fun page(onFinish: OnFinish<WikiPage>, url: String): AsyncTask<String, Void, WikiPage> {
        return FetchUrl(onFinish, WIKIPAGE_MAPPER).execute(url)
    }

}
