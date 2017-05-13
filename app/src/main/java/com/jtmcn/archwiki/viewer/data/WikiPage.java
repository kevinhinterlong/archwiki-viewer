package com.jtmcn.archwiki.viewer.data;

/**
 * Wrapper for a downloaded wiki page which holds the title and html.
 */
public class WikiPage {
	private final String pageUrl;
	private final String pageTitle;
	private final String htmlString;

	/**
	 * Store the url, title, and html of a page on the wiki.
	 *
	 * @param pageUrl    the string url on the wiki.
	 * @param pageTitle  the title of the page on the wiki.
	 * @param htmlString the html which should be shown to represent the page.
	 */
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("WikiPage{");
		sb.append("title='").append(pageTitle).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
