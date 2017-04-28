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
	public static final FetchUrl.FetchGenericMapper<List<SearchResult>> SEARCH_RESULTS_MAPPER =
			new FetchUrl.FetchGenericMapper<List<SearchResult>>() {
				@Override
				public List<SearchResult> mapTo(String url, StringBuilder stringBuilder) {
					return SearchResultsBuilder.parseSearchResults(stringBuilder.toString());
				}
			};

	public static final FetchUrl.FetchGenericMapper<WikiPage> WIKIPAGE_MAPPER =
			new FetchUrl.FetchGenericMapper<WikiPage>() {
				@Override
				public WikiPage mapTo(String url, StringBuilder sb) {
					return WikiPageBuilder.buildPage(url, sb);
				}
			};

	private Fetch() {

	}

	public static FetchUrl<List<SearchResult>> search(FetchUrl.OnFinish<List<SearchResult>> onFinish) {
		return new FetchUrl<>(onFinish, SEARCH_RESULTS_MAPPER);
	}

	public static FetchUrl<WikiPage> page(FetchUrl.OnFinish<WikiPage> onFinish) {
		return new FetchUrl<>(onFinish, WIKIPAGE_MAPPER);
	}

}
