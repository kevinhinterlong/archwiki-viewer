package com.jtmcn.archwiki.viewer;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.jtmcn.archwiki.viewer.data.WikiPage;

import static com.jtmcn.archwiki.viewer.Constants.ARCHWIKI_MAIN;
import static com.jtmcn.archwiki.viewer.Constants.ARCHWIKI_SEARCH_URL;

public class WikiView extends WebView {
	public static final String TAG = WikiView.class.getSimpleName();
	WikiClient wikiClient;

	public WikiView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !isInEditMode()) {
			//this allows the webview to inject the css (otherwise it blocks it for security reasons)
			getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
	}

	/**
	 * Initializes the wiki client and loads the main page.
	 */
	public void buildView(ProgressBar progressBar, ActionBar actionBar) {
		wikiClient = new WikiClient(progressBar, actionBar, this);
		setWebViewClient(wikiClient);
		wikiClient.shouldOverrideUrlLoading(this, ARCHWIKI_MAIN);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && wikiClient.getHistoryStackSize() > 1) {
			Log.d(TAG, "Loading previous page.");
			wikiClient.goBackHistory();
			return true;
		} else {
			Log.d(TAG, "Passing up button press.");
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * Performs a search against the wiki.
	 *
	 * @param query the text to search for.
	 */
	public void passSearch(String query) {
		Log.d(TAG, "Searching for " + query);
		String searchUrl = String.format(ARCHWIKI_SEARCH_URL, query);
		wikiClient.shouldOverrideUrlLoading(this, searchUrl);
	}

	/**
	 * Returns the current {@link WikiPage} being shown or null.
	 *
	 * @return current wiki page being shown.
	 */
	public WikiPage getCurrentWebPage() {
		return wikiClient.getCurrentWebPage();
	}

	public void refreshPage() {
		wikiClient.refreshPage();
	}
}
