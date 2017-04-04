package com.jtmcn.archwiki.viewer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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
}
