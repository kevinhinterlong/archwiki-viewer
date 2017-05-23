package com.jtmcn.archwiki.viewer;

/**
 * Various constants used throughout the program.
 */
public class Constants {
	//format types
	public static final String TEXT_HTML_MIME = "text/html";
	public static final String TEXT_PLAIN_MIME = "text/plain";
	public static final String UTF_8 = "UTF-8";

	//arch wiki urls
	public static final String ARCHWIKI_BASE = "https://wiki.archlinux.org/";
	public static final String ARCHWIKI_MAIN = ARCHWIKI_BASE + "index.php/Main_page";
	public static final String ARCHWIKI_SEARCH_URL = ARCHWIKI_BASE + "index.php?&search=%s";

	//local file paths
	public static final String ASSETS_FOLDER = "file:///android_asset/";
	public static final String LOCAL_CSS = ASSETS_FOLDER + "style.css";
}
