package com.jtmcn.archwiki.viewer.tasks;

import android.os.AsyncTask;

import com.jtmcn.archwiki.viewer.data.SearchResult;
import com.jtmcn.archwiki.viewer.data.SearchResultsBuilder;
import com.jtmcn.archwiki.viewer.data.WikiPage;
import com.jtmcn.archwiki.viewer.data.WikiPageBuilder;

import java.util.List;

/**
 * Wrapper for {@link FetchUrl} which gives an easy to use interface
 * for fetching {@link SearchResult} and {@link WikiPage}.
 */
public class Fetch {
	public static final FetchUrl.FetchUrlMapper<List<SearchResult>> SEARCH_RESULTS_MAPPER =
			(url, stringBuilder) -> SearchResultsBuilder.parseSearchResults(stringBuilder.toString());

	public static final FetchUrl.FetchUrlMapper<WikiPage> WIKI_PAGE_MAPPER =
			WikiPageBuilder::buildPage;

	private Fetch() {

	}

	/**
	 * Fetches a {@link List<SearchResult>} from the url.
	 *
	 * @param onFinish The listener called when search results are ready.
	 * @param url      The url to fetch the search results from.
	 * @return the async task fetching the data.
	 */
	public static AsyncTask<String, Void, List<SearchResult>> search(
			FetchUrl.OnFinish<List<SearchResult>> onFinish,
			String url
	) {
		return new FetchUrl<>(onFinish, SEARCH_RESULTS_MAPPER).execute(url);
	}

	/**
	 * Fetches a {@link WikiPage} from the url.
	 *
	 * @param onFinish The listener called when the page is ready.
	 * @param url      The url to fetch the page from.
	 * @return the async task fetching the data.
	 */
	public static AsyncTask<String, Void, WikiPage> page(
			FetchUrl.OnFinish<WikiPage> onFinish,
			String url
	) {
		return new FetchUrl<>(onFinish, WIKI_PAGE_MAPPER).execute(url);
	}

}
