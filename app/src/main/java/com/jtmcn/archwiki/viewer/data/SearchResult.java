package com.jtmcn.archwiki.viewer.data;

/**
 * A page on the wiki which only knows the name and url.
 */
public class SearchResult {
	private final String pageName;
	private final String pageUrl;

	/**
	 * Create a search result.
	 *
	 * @param pageName the name of the page as shown on the wiki.
	 * @param pageUrl  the string url on the wiki.
	 */
	public SearchResult(String pageName, String pageUrl) {
		this.pageName = pageName;
		this.pageUrl = pageUrl;
	}

	public String getPageName() {
		return pageName;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("SearchResult{");
		sb.append("title='").append(pageName).append('\'');
		sb.append(", url='").append(pageUrl).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
