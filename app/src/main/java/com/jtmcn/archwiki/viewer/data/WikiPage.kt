package com.jtmcn.archwiki.viewer.data

/**
 * Wrapper for a downloaded wiki page which holds the title and html.
 */
data class WikiPage(val pageUrl: String, val pageTitle: String?, val htmlString: String) {
    var scrollPosition = 0
}
