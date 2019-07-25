package com.jtmcn.archwiki.viewer.data;

/**
 * A page on the wiki which only knows the name and url.
 */
public class SearchResult {
	private final String pageName;
	private final String pageURL;

	/**
	 * Create a search result.
	 *
	 * @param pageName the name of the page as shown on the wiki.
	 * @param pageURL  the string url on the wiki.
	 */
	public SearchResult(String pageName, String pageURL) {
		this.pageName = pageName;
		this.pageURL = pageURL;
	}

	public String getPageName() {
		return pageName;
	}

	public String getPageURL() {
		return pageURL;
	}

	@Override
	public String toString() {
		return "SearchResult{" + "title='" + pageName + '\'' +
				", url='" + pageURL + '\'' + '}';
	}
}
