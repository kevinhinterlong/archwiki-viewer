package com.jtmcn.archwiki.viewer.data;

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("WikiPage{");
		sb.append("title='").append(pageTitle).append('\'');
		sb.append('}');
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WikiPage)) return false;

		WikiPage wikiPage = (WikiPage) o;

		if(getPageTitle() == null || getHtmlString() == null) {
			return false;
		}

		return getPageTitle().equals(wikiPage.getPageTitle()) &&
				getHtmlString().equals(wikiPage.getHtmlString());

	}

	@Override
	public int hashCode() {
		int result = getPageTitle() != null ? getPageTitle().hashCode() : 0;
		result = 31 * result + (getHtmlString() != null ? getHtmlString().hashCode() : 0);
		return result;
	}
}
