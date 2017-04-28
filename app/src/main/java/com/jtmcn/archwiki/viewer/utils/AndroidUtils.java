package com.jtmcn.archwiki.viewer.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by kevin on 4/3/2017.
 */

public class AndroidUtils {
	public static final String TAG = AndroidUtils.class.getSimpleName();

	private AndroidUtils() {

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

	/**
	 * https://stackoverflow.com/questions/9544737/read-file-from-assets
	 * @param filename
	 * @param context
	 * @return
	 */
	public static StringBuilder readFileFromAssets(String filename, Context context) {
		BufferedReader reader = null;
		StringBuilder fileString = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(
					context.getAssets().open(filename)
			));

			String mLine;
			while ((mLine = reader.readLine()) != null) {
				fileString.append(mLine);
			}
		} catch (IOException e) {
			Log.d(TAG, "Failed while reading " + filename, e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.d(TAG, "Failed while closing " + filename, e);
				}
			}
		}
		return fileString;
	}
}
