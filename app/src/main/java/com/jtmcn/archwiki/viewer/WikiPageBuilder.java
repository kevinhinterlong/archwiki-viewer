package com.jtmcn.archwiki.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WikiPageBuilder {
	private static final int PAGE_RETRIES = 2;

	private WikiPageBuilder() {

	}

	public static WikiPage getWikiPage(String stringUrl) {
		return buildPage(stringUrl, PAGE_RETRIES);
	}

	/*
	 * buildPage will rerun twice after failure
	 */
	private static WikiPage buildPage(String stringUrl, int pageRetries) {

		String pageString = fetchUrl(stringUrl);

		while(pageString.isEmpty() && pageRetries > 0) {
			pageString = fetchUrl(stringUrl);
			pageRetries--;
		}

		StringBuilder htmlString = new StringBuilder();
		htmlString.append(pageString);

		// System.out.println(htmlString.substring(0, 100));

		// start after <title>
		int titleStart = (htmlString.indexOf("<title>") + 7);
		// drop " - ArchWiki"
		int titleEnd = (htmlString.indexOf("</title>", titleStart) - 11);



		String pageTitle = htmlString.substring(titleStart, titleEnd);

		String page = injectLocalCSS(htmlString);

		return new WikiPage(pageTitle,page);

	}

	private static String injectLocalCSS(StringBuilder htmlString) {
		int headStart = htmlString.indexOf("<head>");
		int headEnd = htmlString.indexOf("</head>");

		String head = "<head>" + "<link rel='stylesheet' href='file:///android_asset/style.css'>"
				+ "<meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=no'>";
		return htmlString.replace(headStart, headEnd, head).toString();
	}

	public static String fetchUrl(String stringUrl) {
		StringBuilder sb = new StringBuilder("");
		try {
			URL url = new URL(stringUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			urlConnection.setReadTimeout(10000 /* milliseconds */);
			urlConnection.setConnectTimeout(15000 /* milliseconds */);
			urlConnection.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()), 8);// buffer 8k

			String l = "";
			String nl = System.getProperty("line.separator");

			while ((l = in.readLine()) != null) {
				sb.append(l).append(nl);
			}

			urlConnection.disconnect();
			in.close();

		} catch (IOException e) {

		}
		return sb.toString();
	}
}