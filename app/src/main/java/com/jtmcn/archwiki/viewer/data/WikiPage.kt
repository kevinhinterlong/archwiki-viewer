package com.jtmcn.archwiki.viewer.data

/**
 * Store the url, title, and html of a page on the wiki.
 *
 * @param pageUrl    the url on the wiki.
 * @param pageTitle  the title of the page on the wiki.
 * @param html the html which should be shown to represent the page.
 */
data class WikiPage(val pageUrl: String, val pageTitle: String?, val html: String) {
    var scrollPosition = 0
}
