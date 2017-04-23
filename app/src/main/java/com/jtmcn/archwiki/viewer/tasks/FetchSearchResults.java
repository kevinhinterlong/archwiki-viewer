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
	 * Fetches a list of urls and publishes progress on the {@link OnFinish} listener.
	 *
	 * @param onFinish The listener to be called when progress is ready.
	 */
	public FetchSearchResults(OnFinish<List<SearchResult>> onFinish) {
		super(onFinish, mapper);
	}
}
