@file:JvmName("WikiPageBuilder")
package com.jtmcn.archwiki.viewer.data

import com.jtmcn.archwiki.viewer.LOCAL_CSS

/**
 * Helps with creating a [WikiPage] by extracting content from the
 * html fetched from the ArchWiki.
 */

//NOTE: spaces are allowed in "<head>"/etc, but parsing this way should be fine
const val HTML_HEAD_OPEN = "<head>"
const val HTML_HEAD_CLOSE = "</head>"
const val HTML_TITLE_OPEN = "<title>"
const val HTML_TITLE_CLOSE = "</title>"
private const val HEAD_TO_INJECT = "<link rel='stylesheet' href='%s' />" + "<meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=no' />"
private const val DEFAULT_TITLE = " - ArchWiki"

/**
 * Builds a page containing the title, url, and injects local css.
 *
 * @param stringUrl url to download.
 * @param html      stringbuilder containing the html of the wikipage
 * @return [WikiPage] containing downloaded page.
 */
fun buildPage(stringUrl: String, html: StringBuilder): WikiPage {
    val pageTitle = getPageTitle(html)
    injectLocalCSS(html, LOCAL_CSS)
    return WikiPage(stringUrl, pageTitle, html.toString())
}

/**
 * Finds the name of the page within the title block of the html.
 * The returned string removes the " - ArchWiki" if found.
 *
 * @param htmlString The html of the page as a string.
 * @return the extracted title from the page.
 */
fun getPageTitle(htmlString: StringBuilder): String? {
    val titleStart = htmlString.indexOf(HTML_TITLE_OPEN) + HTML_TITLE_OPEN.length
    val titleEnd = htmlString.indexOf(HTML_TITLE_CLOSE, titleStart)
    if (titleStart in 1 until titleEnd) { // if there is an html title block
        val title = htmlString.substring(titleStart, titleEnd)
        return title.replace(DEFAULT_TITLE, "") // drop DEFAULT_TITLE from page title
    }
    return null
}

/**
 * Removes the contents within the head block of the html
 * and replaces it with the a reference to a local css file.
 *
 * @param htmlString       The html of the page as a string.
 * @param localCSSFilePath The path of the css file to inject.
 * @return true if the block was successfully replaced.
 */
fun injectLocalCSS(htmlString: StringBuilder, localCSSFilePath: String): Boolean {
    val headStart = htmlString.indexOf(HTML_HEAD_OPEN) + HTML_HEAD_OPEN.length
    val headEnd = htmlString.indexOf(HTML_HEAD_CLOSE, headStart)

    if (headStart in 1..headEnd) {
        val injectedHeadHtml = String.format(HEAD_TO_INJECT, localCSSFilePath)
        htmlString.replace(headStart, headEnd, injectedHeadHtml)
        return true
    }

    return false
}