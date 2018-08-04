package com.jtmcn.archwiki.viewer.data

/**
 * Store the url, title, and html of a page on the wiki.
 *
 * @param pageUrl    the string url on the wiki.
 * @param pageTitle  the title of the page on the wiki.
 * @param htmlString the html which should be shown to represent the page.
 */
data class WikiPage(val pageUrl: String, val pageTitle: String?, val htmlString: String) {
    var scrollPosition = 0
}
