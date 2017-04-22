package com.jtmcn.archwiki.viewer.tasks;

import com.jtmcn.archwiki.viewer.data.WikiPage;
import com.jtmcn.archwiki.viewer.data.WikiPageBuilder;

/**
 * Background thread to download and manipulate page data.
 */
public class FetchWikiPage extends FetchGeneric<WikiPage> {
	private static final FetchGenericMapper<StringBuilder, WikiPage> wikiMapper = new FetchGenericMapper<StringBuilder, WikiPage>() {
		@Override
		public WikiPage mapTo(String url, StringBuilder sb) {
			return WikiPageBuilder.buildPage(url, sb);
		}
	};

	/**
	 * Fetches a list of urls and publishes progress on the {@link OnProgressChange} listener.
	 *
	 * @param onProgressChange The listener to be called when progress is ready.
	 * @param blocking         Whether or not it should force all connections to be finished.
	 */
	public FetchWikiPage(OnProgressChange<WikiPage> onProgressChange, boolean blocking) {
		super(onProgressChange, wikiMapper, blocking);
	}

	public FetchWikiPage(OnProgressChange<WikiPage> onProgressChange) {
		super(onProgressChange, wikiMapper, true);
	}
}
