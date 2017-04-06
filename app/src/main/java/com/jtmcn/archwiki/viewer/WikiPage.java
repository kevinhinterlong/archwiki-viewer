package com.jtmcn.archwiki.viewer;

/**
 * Wrapper for a downloaded wiki page which holds the title and html.
 */
public class WikiPage {
	private final String pageTitle;
	private final String htmlString;

	public WikiPage(String pageTitle, String htmlString) {
		this.pageTitle = pageTitle;
		this.htmlString = htmlString;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public String getHtmlString() {
		return htmlString;
	}
}
