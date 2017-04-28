package com.jtmcn.archwiki.viewer.data;

/**
 * Created by kevin on 4/4/2017.
 */

public class SearchResult {
	private final String pageName;
	private final String pageUrl;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SearchResult that = (SearchResult) o;

		if (getPageName() == null || getPageUrl() == null) {
			return false;
		}

		return getPageName().equals(that.getPageName()) &&
				getPageUrl().equals(that.getPageUrl());

	}

	@Override
	public int hashCode() {
		int result = getPageName().hashCode();
		result = 31 * result + getPageUrl().hashCode();
		return result;
	}
}
