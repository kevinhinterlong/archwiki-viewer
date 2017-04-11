package com.jtmcn.archwiki.viewer;

/**
 * Wrapper for a downloaded wiki page which holds the title and html.
 */
public class WikiPage {
	private final String pageUrl;
	private final String pageTitle;
	private final String htmlString;

	public WikiPage(String pageUrl, String pageTitle, String htmlString) {
		this.pageUrl = pageUrl;
		this.pageTitle = pageTitle;
		this.htmlString = htmlString;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public String getHtmlString() {
		return htmlString;
	}
}
