package com.jtmcn.archwiki.viewer.tasks;

import com.jtmcn.archwiki.viewer.data.SearchResult;
import com.jtmcn.archwiki.viewer.data.SearchResultsBuilder;
import com.jtmcn.archwiki.viewer.data.WikiPage;
import com.jtmcn.archwiki.viewer.data.WikiPageBuilder;

import java.util.List;

/**
 * Created by kevin on 4/26/2017.
 */

public class Fetch {
	public static final FetchGeneric.FetchGenericMapper<StringBuilder, List<SearchResult>> SEARCH_RESULTS_MAPPER =
			new FetchGeneric.FetchGenericMapper<StringBuilder, List<SearchResult>>() {
				@Override
				public List<SearchResult> mapTo(String url, StringBuilder stringBuilder) {
					return SearchResultsBuilder.parseSearchResults(stringBuilder.toString());
				}
			};

	public static final FetchGeneric.FetchGenericMapper<StringBuilder, WikiPage> WIKIPAGE_MAPPER =
			new FetchGeneric.FetchGenericMapper<StringBuilder, WikiPage>() {
				@Override
				public WikiPage mapTo(String url, StringBuilder sb) {
					return WikiPageBuilder.buildPage(url, sb);
				}
			};

	private Fetch() {

	}

	public static FetchGeneric<List<SearchResult>> search(FetchGeneric.OnFinish<List<SearchResult>> onFinish) {
		return new FetchGeneric<>(onFinish, SEARCH_RESULTS_MAPPER);
	}

	public static FetchGeneric<WikiPage> page(FetchGeneric.OnFinish<WikiPage> onFinish) {
		return new FetchGeneric<>(onFinish, WIKIPAGE_MAPPER);
	}

}
