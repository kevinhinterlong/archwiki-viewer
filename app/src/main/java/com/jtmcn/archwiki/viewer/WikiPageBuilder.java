package com.jtmcn.archwiki.viewer;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WikiPageBuilder {
	public static final String TAG = WikiPageBuilder.class.getSimpleName();
	private static final int PAGE_RETRIES = 0;
	public static final String LOCAL_CSS = "file:///android_asset/style.css";
	public static final String HTML_HEAD_OPEN = "<head>";
	public static final String HTML_HEAD_CLOSE = "</head>";
	public static final String HTML_TITLE_OPEN = "<title>";
	public static final String HTML_TITLE_CLOSE = "</title>";
	public static final String DEFAULT_TITLE = " - ArchWiki";

	private WikiPageBuilder() {

	}

	public static WikiPage getWikiPage(String stringUrl) {
		return buildPage(stringUrl, PAGE_RETRIES);
	}

	/**
	 * Fetches a page from the wiki, extracts the title, and injects local css.
	 * @param stringUrl url to download.
	 * @param pageRetries number of times to retry while fetching page.
	 * @return {@link WikiPage} containing downloaded page.
	 */
	private static WikiPage buildPage(String stringUrl, int pageRetries) {
		StringBuilder stringBuilder = fetchUrl(stringUrl);

		while (stringBuilder.length() != 0 && pageRetries > 0) {
			Log.d(TAG, "Page (" + stringUrl + ") was empty. Trying again.");
			stringBuilder = fetchUrl(stringUrl);
			pageRetries--;
		}

		// System.out.println(htmlString.substring(0, 100));
		String pageTitle = getPageTitle(stringBuilder);

		injectLocalCSS(stringBuilder, LOCAL_CSS);
		String page = stringBuilder.toString();

		return new WikiPage(pageTitle, page);

	}

	private static String getPageTitle(StringBuilder htmlString) {
		// start after <title>
		int titleStart = (htmlString.indexOf(HTML_TITLE_OPEN) + HTML_TITLE_OPEN.length());
		// drop DEFAULT_TITLE from page title
		int titleEnd = htmlString.indexOf(HTML_TITLE_CLOSE, titleStart);
		try {
			if(htmlString.indexOf(DEFAULT_TITLE) > 0) { //If it's a normal title "Something - ArchWiki"
				return htmlString.substring(titleStart, titleEnd - DEFAULT_TITLE.length());
			} else { //else return "Something"
				return htmlString.substring(titleStart,titleEnd);
			}
		} catch (StringIndexOutOfBoundsException e) {
			Log.d(TAG, "Failed to parse page title.", e);
		}
		return "No title found";
	}

	private static void injectLocalCSS(StringBuilder htmlString, String localCSSFilePath) {
		try {
			int headStart = htmlString.indexOf(HTML_HEAD_OPEN) + HTML_HEAD_OPEN.length();
			int headEnd = htmlString.indexOf(HTML_HEAD_CLOSE);

			String head = "<link rel='stylesheet' href='" + localCSSFilePath + "'>"
					+ "<meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=no'>";
			htmlString.replace(headStart, headEnd, head);
		} catch (StringIndexOutOfBoundsException e) {
			Log.d(TAG, "Failed to inject local CSS.", e);
		}
	}

	public static StringBuilder fetchUrl(String stringUrl) {
		StringBuilder sb = new StringBuilder("");
		try {
			sb = Utils.fetchURL(stringUrl);
		} catch (IOException e) {
			Log.d(TAG, "Failed while fetching (" + stringUrl + ") - ", e);
		}
		return sb;
	}
}