package com.jtmcn.archwiki.viewer.data;

import android.util.Log;

import com.jtmcn.archwiki.viewer.utils.NetworkUtils;

import java.io.IOException;

public class WikiPageBuilder {
	public static final String TAG = WikiPageBuilder.class.getSimpleName();
	public static final String LOCAL_CSS = "file:///android_asset/style.css";
	public static final String HTML_HEAD_OPEN = "<head>";
	public static final String HTML_HEAD_CLOSE = "</head>";
	public static final String HTML_TITLE_OPEN = "<title>";
	public static final String HTML_TITLE_CLOSE = "</title>";
	public static final String DEFAULT_TITLE = " - ArchWiki";

	private WikiPageBuilder() {

	}

	public static WikiPage getWikiPage(String stringUrl) {
		return buildPage(stringUrl);
	}

	/**
	 * Fetches a page from the wiki, extracts the title, and injects local css.
	 *
	 * @param stringUrl url to download.
	 * @return {@link WikiPage} containing downloaded page.
	 */
	private static WikiPage buildPage(String stringUrl) {
		StringBuilder stringBuilder = fetchUrl(stringUrl);

		// System.out.println(htmlString.substring(0, 100));
		String pageTitle = getPageTitle(stringBuilder);

		injectLocalCSS(stringBuilder, LOCAL_CSS);
		String page = stringBuilder.toString();

		return new WikiPage(stringUrl, pageTitle, page);

	}

	private static String getPageTitle(StringBuilder htmlString) {
		// start after <title>
		int titleStart = (htmlString.indexOf(HTML_TITLE_OPEN) + HTML_TITLE_OPEN.length());
		int titleEnd = htmlString.indexOf(HTML_TITLE_CLOSE, titleStart);
		if (titleStart > 0) { // drop DEFAULT_TITLE from page title
			String title = htmlString.substring(titleStart, titleEnd);
			return title.replace(DEFAULT_TITLE, "");
		}
		return "No title found";
	}

	private static void injectLocalCSS(StringBuilder htmlString, String localCSSFilePath) {
		try {
			int headStart = htmlString.indexOf(HTML_HEAD_OPEN) + HTML_HEAD_OPEN.length();
			int headEnd = htmlString.indexOf(HTML_HEAD_CLOSE);

			String injectedHeadHtml = "<link rel='stylesheet' href='" + localCSSFilePath + "'>"
					+ "<meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=no'>";
			htmlString.replace(headStart, headEnd, injectedHeadHtml);
		} catch (StringIndexOutOfBoundsException e) {
			Log.d(TAG, "Failed to inject local CSS.", e);
		}
	}

	public static StringBuilder fetchUrl(String stringUrl) {
		StringBuilder sb = new StringBuilder("");
		try {
			sb = NetworkUtils.fetchURL(stringUrl);
		} catch (IOException e) {
			Log.d(TAG, "Failed while fetching (" + stringUrl + ") - ", e);
		}
		return sb;
	}
}