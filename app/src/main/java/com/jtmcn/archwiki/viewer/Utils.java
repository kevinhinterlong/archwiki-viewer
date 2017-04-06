package com.jtmcn.archwiki.viewer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kevin on 4/3/2017.
 */

public class Utils {
	private Utils() {

	}

	public static void shareText(String title, String message, Context context) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TITLE, title);
		sendIntent.putExtra(Intent.EXTRA_TEXT, message);
		sendIntent.setType("text/plain");
		context.startActivity(sendIntent);
	}

	public static void openLink(String url, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(intent);
	}

	public static StringBuilder fetchURL(String stringUrl) throws IOException {
		StringBuilder sb = new StringBuilder("");
		URL url = new URL(stringUrl);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();

		urlConnection.setReadTimeout(10000 /* milliseconds */);
		urlConnection.setConnectTimeout(15000 /* milliseconds */);
		urlConnection.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(
				urlConnection.getInputStream()), 8);// buffer 8k

		String line = "";
		String lineSeparator = System.getProperty("line.separator");

		while ((line = in.readLine()) != null) {
			sb.append(line).append(lineSeparator);
		}

		urlConnection.disconnect();
		in.close();

		return sb;
	}
}
