package com.jtmcn.archwiki.viewer.data;

import com.jtmcn.archwiki.viewer.data.SearchResult;
import com.jtmcn.archwiki.viewer.data.SearchResultsBuilder;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class SearchResultsBuilderTest {
	private final String realResult = "[\"arch\", [\"Arch-based Distros\", \"Arch-based distributions\", \"Arch-chroot\", \"Arch32\", \"Arch64 FAQ\"],\n" +
			"\t[\"\", \"\", \"\", \"\", \"\"],\n" +
			"\t[\"https://wiki.archlinux.org/index.php/Arch-based_Distros\", \"https://wiki.archlinux.org/index.php/Arch-based_distributions\", \"https://wiki.archlinux.org/index.php/Arch-chroot\", \"https://wiki.archlinux.org/index.php/Arch32\", \"https://wiki.archlinux.org/index.php/Arch64_FAQ\"]\n" +
			"]";

	@Test
	public void parseSearchResults() throws Exception {
		List<SearchResult> searchResults = SearchResultsBuilder.parseSearchResults(realResult);

		assertEquals("Arch-based Distros",searchResults.get(0).getPageName());
		assertEquals("https://wiki.archlinux.org/index.php/Arch-based_Distros", searchResults.get(0).getPageUrl());

		assertEquals("Arch-chroot",searchResults.get(2).getPageName());
		assertEquals("https://wiki.archlinux.org/index.php/Arch-chroot", searchResults.get(2).getPageUrl());
	}

	@Test
	public void getSearchQuery() {
		String query = SearchResultsBuilder.getSearchQuery("arch");
		assertEquals("https://wiki.archlinux.org/api.php?action=opensearch&format=json&formatversion=2&namespace=0&limit=10&suggest=true&search=arch",query);

		String queryWithLength = SearchResultsBuilder.getSearchQuery("arch",9);
		assertEquals("https://wiki.archlinux.org/api.php?action=opensearch&format=json&formatversion=2&namespace=0&limit=9&suggest=true&search=arch",queryWithLength);
	}

	@Test
	public void emptySearchCorrectFormat() throws Exception {
		String fakeResult = "[\"\", [],\n" +
				"\t[],\n" +
				"\t[]\n" +
				"]";
		List<SearchResult> searchResults = SearchResultsBuilder.parseSearchResults(fakeResult);
		assertEquals(0,searchResults.size());
	}

	@Test
	public void emptyStringSearch() throws Exception {
		String fakeResult = "";
		List<SearchResult> searchResults = SearchResultsBuilder.parseSearchResults(fakeResult);
		assertEquals(0,searchResults.size());
	}
}