package com.jtmcn.archwiki.viewer;

/**
 * Created by kevin on 4/3/2017.
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
