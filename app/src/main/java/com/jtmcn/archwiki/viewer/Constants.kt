@file:JvmName("Constants")

package com.jtmcn.archwiki.viewer

/**
 * Various constants used throughout the program.
 */

// format types
const val TEXT_HTML_MIME = "text/html"
const val TEXT_PLAIN_MIME = "text/plain"
const val UTF_8 = "UTF-8"

//arch wiki urls
const val ARCHWIKI_BASE = "https://wiki.archlinux.org/"
const val ARCHWIKI_MAIN = ARCHWIKI_BASE + "index.php/Main_page"
const val ARCHWIKI_SEARCH_URL = ARCHWIKI_BASE + "index.php?&search=%s"

//local file paths
const val ASSETS_FOLDER = "file:///android_asset/"
const val LOCAL_CSS = ASSETS_FOLDER + "style.css"
