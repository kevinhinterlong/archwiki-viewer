package com.jtmcn.archwiki.viewer;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jtmcn.archwiki.viewer.data.WikiPage;

import static com.jtmcn.archwiki.viewer.Constants.ARCHWIKI_MAIN;
import static com.jtmcn.archwiki.viewer.Constants.START_PAGE_FILE;

public class WikiView extends WebView {
	public static final String TAG = WikiView.class.getSimpleName();
	WikiClient wikiClient;
	private final Context context;

	public WikiView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !isInEditMode()) {
			//this allows the webview to inject the css (otherwise it blocks it for security reasons)
			getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
	}

	public void buildView() {
		wikiClient = new WikiClient(this);
		setWebViewClient(wikiClient);

		wikiClient.shouldOverrideUrlLoading(this, ARCHWIKI_MAIN);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button
		if (keyCode == KeyEvent.KEYCODE_BACK && wikiClient.histStackSize() > 1) {
			Log.d(TAG, "Handling back button press");
			loadLastWebPage();
			return true;
		} else {
			// if there are zero entries exit application
			Log.d(TAG, "passing up back button press");
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * Load the last webpage used on the for the wikiclient
	 */
	private void loadLastWebPage() {
		if (wikiClient.histStackSize() > 1) {
			wikiClient.goBackHistory();
		}
	}

	public void passSearch(String searchUrl) {
		wikiClient.searchWiki(searchUrl);
	}

	public WikiPage getCurrentWebPage() {
		return wikiClient.getCurrentWebPage();
	}
}
