package com.jtmcn.archwiki.viewer.data;

/**
 * Wrapper for a downloaded wiki page which holds the title and html.
 */
public class WikiPage {
	private final String pageUrl;
	private final String pageTitle;
	private final String htmlString;
	private int scrollPosition = 0;

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

	public int getScrollPosition() {
		return scrollPosition;
	}

	public void setScrollPosition(int scrollPosition) {
		this.scrollPosition = scrollPosition;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WikiPage)) return false;

		WikiPage wikiPage = (WikiPage) o;

		return getPageUrl() != null ? getPageUrl().equals(wikiPage.getPageUrl()) : wikiPage.getPageUrl() == null;

	}

	@Override
	public int hashCode() {
		return getPageUrl() != null ? getPageUrl().hashCode() : 0;
	}
}
