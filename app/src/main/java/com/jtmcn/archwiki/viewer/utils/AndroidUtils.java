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
