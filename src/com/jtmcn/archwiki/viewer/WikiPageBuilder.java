package com.jtmcn.archwiki.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.widget.Toast;

public class WikiPageBuilder {

	String myUrl = "";
	String page = null;
	String pageTitle;

	public WikiPageBuilder(String urlString) {
		myUrl = urlString;
		buildPage();
	}

	private String buildPage() {

		try {
			URL url = new URL(myUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			urlConnection.setReadTimeout(10000 /* milliseconds */);
			urlConnection.setConnectTimeout(15000 /* milliseconds */);
			urlConnection.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));

			StringBuffer sb = new StringBuffer("");
			String l = "";
			String nl = System.getProperty("line.separator");

			while ((l = in.readLine()) != null) {
				sb.append(l + nl);
			}

			urlConnection.disconnect();
			in.close();

			String pageString = sb.toString();

			StringBuilder htmlString = new StringBuilder();
			htmlString.append(pageString);

			// start after <title>
			int titleStart = (htmlString.indexOf("<title>") + 7);
			// drop " - ArchWiki"
			int titleEnd = (htmlString.indexOf("</title>") - 11);

			pageTitle = htmlString.substring(titleStart, titleEnd);

			int headStart = htmlString.indexOf("<head>");
			int headEnd = htmlString.indexOf("</head>");

			String head = "<link rel='stylesheet' href='file:///android_asset/style.css'></head>";
			page = htmlString.replace(headStart, headEnd, head).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return page;

	}

	public String getPageTitle() {
		return pageTitle;
	}

	public String getHtmlString() {
		return page;
	}

}