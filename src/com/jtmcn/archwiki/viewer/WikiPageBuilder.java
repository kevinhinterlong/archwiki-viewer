package com.jtmcn.archwiki.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WikiPageBuilder {

	String myUrl = "";
	String page;
	
	public WikiPageBuilder(String urlString){
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

			int headStart = htmlString.indexOf("<head>");
			int headEnd = htmlString.indexOf("</head>");

			String head = "<link rel='stylesheet' href='file:///android_asset/style.css'></head>";
			page = htmlString.replace(headStart, headEnd, head)
					.toString();

			return page;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public String getHtmlString() {
		return page;
	}
	
	
}