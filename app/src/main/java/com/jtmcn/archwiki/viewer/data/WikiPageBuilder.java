package com.jtmcn.archwiki.viewer.data;

import static com.jtmcn.archwiki.viewer.Constants.LOCAL_CSS;

/**
 * Helps with creating a {@link WikiPage} by extracting content from the
 * html fetched from the ArchWiki.
 */
public class WikiPageBuilder {
	//NOTE: spaces are allowed in "<head>"/etc, but parsing this way should be fine
	public static final String HTML_HEAD_OPEN = "<head>";
	public static final String HTML_HEAD_CLOSE = "</head>";
	public static final String HTML_TITLE_OPEN = "<title>";
	public static final String HTML_TITLE_CLOSE = "</title>";
	public static final String HEAD_TO_INJECT = "<link rel='stylesheet' href='%s' />"
			+ "<meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=no' />";
	public static final String DEFAULT_TITLE = " - ArchWiki";

	private WikiPageBuilder() {

	}

	/**
	 * Builds a page containing the title, url, and injects local css.
	 *
	 * @param stringUrl url to download.
	 * @param html      stringbuilder containing the html of the wikipage
	 * @return {@link WikiPage} containing downloaded page.
	 */
	public static WikiPage buildPage(String stringUrl, StringBuilder html) {
		String pageTitle = getPageTitle(html);
		injectLocalCSS(html, LOCAL_CSS);
		return new WikiPage(stringUrl, pageTitle, html.toString());
	}

	/**
	 * Finds the name of the page within the title block of the html.
	 * The returned string removes the " - ArchWiki" if found.
	 *
	 * @param htmlString The html of the page as a string.
	 * @return the extracted title from the page.
	 */
	public static String getPageTitle(StringBuilder htmlString) {
		int titleStart = (htmlString.indexOf(HTML_TITLE_OPEN) + HTML_TITLE_OPEN.length());
		int titleEnd = htmlString.indexOf(HTML_TITLE_CLOSE, titleStart);
		if (titleStart > 0 && titleEnd > titleStart) { // if there is an html title block
			String title = htmlString.substring(titleStart, titleEnd);
			return title.replace(DEFAULT_TITLE, ""); // drop DEFAULT_TITLE from page title
		}
		//todo should be handled somewhere else when no title is found
		return "No title found";
	}

	/**
	 * Removes the contents within the head block of the html
	 * and replaces it with the a reference to a local css file.
	 *
	 * @param htmlString       The html of the page as a string.
	 * @param localCSSFilePath The path of the css file to inject.
	 * @return true if the block was successfully replaced.
	 */
	public static boolean injectLocalCSS(StringBuilder htmlString, String localCSSFilePath) {
		int headStart = htmlString.indexOf(HTML_HEAD_OPEN) + HTML_HEAD_OPEN.length();
		int headEnd = htmlString.indexOf(HTML_HEAD_CLOSE, headStart);

		if (headStart > 0 && headEnd >= headStart) {
			String injectedHeadHtml = String.format(HEAD_TO_INJECT, localCSSFilePath);
			htmlString.replace(headStart, headEnd, injectedHeadHtml);
			return true;
		}

		return false;
	}
}