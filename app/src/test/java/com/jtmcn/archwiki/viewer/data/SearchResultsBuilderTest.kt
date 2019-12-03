package com.jtmcn.archwiki.viewer.data

import org.junit.Test
import org.junit.Assert.*

class SearchResultsBuilderTest {
    private val realResult = """["arch", ["Arch-based Distros", "Arch-based distributions", "Arch-chroot", "Arch32", "Arch64 FAQ"],
	["", "", "", "", ""],
	["https://wiki.archlinux.org/index.php/Arch-based_Distros", "https://wiki.archlinux.org/index.php/Arch-based_distributions", "https://wiki.archlinux.org/index.php/Arch-chroot", "https://wiki.archlinux.org/index.php/Arch32", "https://wiki.archlinux.org/index.php/Arch64_FAQ"]
]"""

    @Test
    @Throws(Exception::class)
    fun parseSearchResultsTest() {
        val searchResults = parseSearchResults(realResult)
        assertEquals("Arch-based Distros", searchResults[0].pageName)
        assertEquals("https://wiki.archlinux.org/index.php/Arch-based_Distros", searchResults[0].pageURL)
        assertEquals("Arch-chroot", searchResults[2].pageName)
        assertEquals("https://wiki.archlinux.org/index.php/Arch-chroot", searchResults[2].pageURL)
    }

    @Test
    fun getSearchQuery() {
        val query: String = getSearchQuery("arch")
        assertEquals("https://wiki.archlinux.org/api.php?action=opensearch&format=json&formatversion=2&namespace=0&suggest=true&search=arch&limit=10", query)
        val queryWithLength: String = getSearchQuery("arch", 9)
        assertEquals("https://wiki.archlinux.org/api.php?action=opensearch&format=json&formatversion=2&namespace=0&suggest=true&search=arch&limit=9", queryWithLength)
    }

    @Test
    @Throws(Exception::class)
    fun emptySearchCorrectFormat() {
        val fakeResult = "[\"\", [],\n" +
                "\t[],\n" +
                "\t[]\n" +
                "]"
        val searchResults = parseSearchResults(fakeResult)
        assertEquals(0, searchResults.size)
    }

    @Test
    @Throws(Exception::class)
    fun emptyStringSearch() {
        val fakeResult = ""
        val searchResults = parseSearchResults(fakeResult)
        assertEquals(0, searchResults.size)
    }
}