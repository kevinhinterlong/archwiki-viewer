package com.jtmcn.archwiki.viewer.data

import junit.framework.Assert.assertEquals
import org.junit.Test

class SearchResultsBuilderTest {
    private val realResult = """["arch", ["Arch-based Distros", "Arch-based distributions", "Arch-chroot", "Arch32", "Arch64 FAQ"],
	["", "", "", "", ""],
	["https://wiki.archlinux.org/index.php/Arch-based_Distros", "https://wiki.archlinux.org/index.php/Arch-based_distributions", "https://wiki.archlinux.org/index.php/Arch-chroot", "https://wiki.archlinux.org/index.php/Arch32", "https://wiki.archlinux.org/index.php/Arch64_FAQ"]
]"""

    @Test
    @Throws(Exception::class)
    fun parseSearchResults() {
        val searchResults = parseSearchResults(realResult)

        assertEquals("Arch-based Distros", searchResults[0].pageName)
        assertEquals("https://wiki.archlinux.org/index.php/Arch-based_Distros", searchResults[0].pageUrl)

        assertEquals("Arch-chroot", searchResults[2].pageName)
        assertEquals("https://wiki.archlinux.org/index.php/Arch-chroot", searchResults[2].pageUrl)
    }

    @Test
    fun getSearchQuery() {
        val query = getSearchQuery("arch")
        assertEquals("https://wiki.archlinux.org/api.php?action=opensearch&format=json&formatversion=2&namespace=0&suggest=true&search=arch&limit=10", query)

        val queryWithLength = getSearchQuery("arch", 9)
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