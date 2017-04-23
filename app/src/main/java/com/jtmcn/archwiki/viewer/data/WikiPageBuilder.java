package com.jtmcn.archwiki.viewer.data;

import android.util.Log;

import static com.jtmcn.archwiki.viewer.Constants.LOCAL_CSS;

public class WikiPageBuilder {
	public static final String TAG = WikiPageBuilder.class.getSimpleName();
	public static final String HTML_HEAD_OPEN = "<head>";
	public static final String HTML_HEAD_CLOSE = "</head>";
	public static final String HTML_TITLE_OPEN = "<title>";
	public static final String HTML_TITLE_CLOSE = "</title>";
	public static final String DEFAULT_TITLE = " - ArchWiki";

	private WikiPageBuilder() {

	}

	/**
	 * Fetches a page from the wiki, extracts the title, and injects local css.
	 *
	 * @param stringUrl url to download.
	 * @param html stringbuilder containing the html of the wikipage
	 * @return {@link WikiPage} containing downloaded page.
	 */
	public static WikiPage buildPage(String stringUrl, StringBuilder html) {
		String pageTitle = getPageTitle(html);
		injectLocalCSS(html, LOCAL_CSS);
		String page = html.toString();

		return new WikiPage(stringUrl, pageTitle, page);

	}

	public static String getPageTitle(StringBuilder htmlString) {
		// start after <title>
		int titleStart = (htmlString.indexOf(HTML_TITLE_OPEN) + HTML_TITLE_OPEN.length());
		int titleEnd = htmlString.indexOf(HTML_TITLE_CLOSE, titleStart);
		if (titleStart > 0 && titleEnd > titleStart) { // drop DEFAULT_TITLE from page title
			String title = htmlString.substring(titleStart, titleEnd);
			return title.replace(DEFAULT_TITLE, "");
		}
		return "No title found";
	}

	public static void injectLocalCSS(StringBuilder htmlString, String localCSSFilePath) {
		try {
			int headStart = htmlString.indexOf(HTML_HEAD_OPEN) + HTML_HEAD_OPEN.length();
			int headEnd = htmlString.indexOf(HTML_HEAD_CLOSE);

			String injectedHeadHtml = "<link rel='stylesheet' href='" + localCSSFilePath + "' />"
					+ "<meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=no' />";
			htmlString.replace(headStart, headEnd, injectedHeadHtml);
		} catch (StringIndexOutOfBoundsException e) {
			Log.d(TAG, "Failed to inject local CSS.", e);
		}
	}
}