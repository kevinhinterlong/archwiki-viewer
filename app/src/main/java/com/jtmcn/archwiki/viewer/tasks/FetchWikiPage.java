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
	 * Fetches a list of urls and publishes progress on the {@link OnFinish} listener.
	 *
	 * @param onFinish The listener to be called when progress is ready.
	 */
	public FetchWikiPage(OnFinish<WikiPage> onFinish) {
		super(onFinish, wikiMapper);
	}
}
