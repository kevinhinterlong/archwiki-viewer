package com.jtmcn.archwiki.viewer;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WikiView extends WebView {
	WikiClient wikiClient;

	public WikiView(Context context, AttributeSet attrs) {
		super(context, attrs);
		buildView((Activity) context);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			//this allows the webview to inject the css (otherwise it blocks it for security reasons)
			getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
	}

	public void buildView(Activity act) {
		wikiClient = new WikiClient(this);
		setWebViewClient(wikiClient);
		loadUrl("file:///android_asset/startPage.html");
	}

	public void resetApplication() {
		wikiClient.resetStackSize();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history
		// history needs to be managed manually
		if ((keyCode == KeyEvent.KEYCODE_BACK)
				&& (wikiClient.histStackSize() == 1)) {
			// if there's only one entry load local html
			wikiClient.resetStackSize();
			loadUrl("file:///android_asset/startPage.html");
			return true;
		} else if ((keyCode == KeyEvent.KEYCODE_BACK)
				&& (wikiClient.histStackSize() > 1)) {
			wikiClient.goBackHistory();
			return true;
		} else {
			// if there are zero entries exit application
			return super.onKeyDown(keyCode, event);
		}
	}

	public void passSearch(String searchUrl) {
		wikiClient.searchWiki(searchUrl);
	}

}
