package com.jtmcn.archwiki.viewer.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Utilities class for Android specific actions.
 */
public class AndroidUtils {

	private AndroidUtils() {

	}

	/**
	 * Creates an intent to prompt the user for sharing text.
	 *
	 * @param title   The name of the text being stored.
	 * @param message The message to be shared.
	 * @param context The context needed to start the intent.
	 */
	public static void shareText(String title, String message, Context context) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TITLE, title);
		intent.putExtra(Intent.EXTRA_TEXT, message);
		intent.setType("text/plain");
		context.startActivity(intent);
	}

	/**
	 * Creates an intent to open a link.
	 *
	 * @param url     The url to be opened.
	 * @param context The context needed to start the intent.
	 */
	public static void openLink(String url, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(intent);
	}
}
