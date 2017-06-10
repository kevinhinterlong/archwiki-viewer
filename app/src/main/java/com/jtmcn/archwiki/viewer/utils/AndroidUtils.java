package com.jtmcn.archwiki.viewer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;

import com.jtmcn.archwiki.viewer.R;

import static com.jtmcn.archwiki.viewer.Constants.TEXT_PLAIN_MIME;

/**
 * Utilities class for Android specific actions.
 */
public class AndroidUtils {

	private AndroidUtils() {

	}

	/**
	 * Creates an intent to prompt the user for sharing text.
	 *
	 * @param title    The name of the text being stored.
	 * @param url      The url to be shared.
	 * @param activity The current activity.
	 */
	public static Intent shareText(String title, String url, Activity activity) {
		return ShareCompat.IntentBuilder.from(activity)
				.setSubject(title)
				.setText(url)
				.setStream(Uri.parse(url))
				.setType(TEXT_PLAIN_MIME)
				.setChooserTitle(R.string.share)
				.getIntent();
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
