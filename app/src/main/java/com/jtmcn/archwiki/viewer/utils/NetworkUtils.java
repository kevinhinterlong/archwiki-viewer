package com.jtmcn.archwiki.viewer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for performing basic network tasks.
 */
public class NetworkUtils {
	private static final Map<URL, StringBuilder> downloadCache = new HashMap<>();

	private NetworkUtils() {

	}

	/**
	 * Fetches a url with caching.
	 *
	 * @param stringUrl url to be fetched.
	 * @return the string that was fetched.
	 * @throws IOException on network failure.
	 */
	public static StringBuilder fetchURL(String stringUrl) throws IOException {
		return fetchURL(stringUrl, true);
	}

	/**
	 * Fetches a url with optional caching.
	 *
	 * @param stringUrl url to be fetched.
	 * @param useCache  whether or not it should return a cached value.
	 * @return the string that was fetched.
	 * @throws IOException on network failure.
	 */
	public static StringBuilder fetchURL(String stringUrl, boolean useCache) throws IOException {
		StringBuilder sb = new StringBuilder("");
		URL url = new URL(stringUrl);
		if (useCache && downloadCache.containsKey(url)) {
			return new StringBuilder(downloadCache.get(url));
		}
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();

		urlConnection.setReadTimeout(10000); // milliseconds
		urlConnection.setConnectTimeout(15000); // milliseconds
		urlConnection.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(
				urlConnection.getInputStream()), 8);// buffer 8k

		String line;
		String lineSeparator = System.getProperty("line.separator");

		while ((line = in.readLine()) != null) {
			sb.append(line).append(lineSeparator);
		}

		urlConnection.disconnect();
		in.close();

		downloadCache.put(url, new StringBuilder(sb));
		return sb;
	}
}
