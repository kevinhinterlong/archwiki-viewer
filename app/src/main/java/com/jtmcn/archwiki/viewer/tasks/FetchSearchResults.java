package com.jtmcn.archwiki.viewer.tasks;

import com.jtmcn.archwiki.viewer.data.SearchResult;
import com.jtmcn.archwiki.viewer.data.SearchResultsBuilder;

import java.util.List;

/**
 * Created by kevin on 4/22/2017.
 */

public class FetchSearchResults extends FetchGeneric<List<SearchResult>> {
	private static final FetchGenericMapper<StringBuilder, List<SearchResult>> mapper = new FetchGenericMapper<StringBuilder, List<SearchResult>>() {
		@Override
		public List<SearchResult> mapTo(String url, StringBuilder stringBuilder) {
			return SearchResultsBuilder.parseSearchResults(stringBuilder.toString());
		}
	};

	/**
	 * Fetches a list of urls and publishes progress on the {@link OnProgressChange} listener.
	 *
	 * @param onProgressChange The listener to be called when progress is ready.
	 * @param blocking         Whether or not it should force all connections to be finished.
	 */
	public FetchSearchResults(OnProgressChange<List<SearchResult>> onProgressChange, boolean blocking) {
		super(onProgressChange, mapper, blocking);
	}

	/**
	 * Fetches a list of urls and publishes progress on the {@link OnProgressChange} listener.
	 *
	 * @param onProgressChange The listener to be called when progress is ready.
	 */
	public FetchSearchResults(OnProgressChange<List<SearchResult>> onProgressChange) {
		super(onProgressChange, mapper, true);
	}
}
