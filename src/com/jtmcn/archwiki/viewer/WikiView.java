package com.jtmcn.archwiki.viewer;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebView;

public class WikiView extends WebView {
	WikiClient myClient;

	public WikiView(Context context, AttributeSet attrs) {
		super(context, attrs);
		buildView((Activity) context);
	}

	public void buildView(Activity act) {
		myClient = new WikiClient(this);
		setWebViewClient(myClient);

		loadUrl("file:///android_asset/startPage.html");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history
		// history needs to be managed manually
		if ((keyCode == KeyEvent.KEYCODE_BACK)
				&& (myClient.histStackSize() == 1)) {
			// if there's only one entry load local html
			myClient.resetStackSize();
			loadUrl("file:///android_asset/startPage.html");
			WikiChromeClient.setTvTitle("ArchWiki Viewer");

			return true;
		} else if ((keyCode == KeyEvent.KEYCODE_BACK)
				&& (myClient.histStackSize() > 1)) {
			myClient.goBackHistory();
			return true;
		} else {
			// if there are zero entries exit application
			return super.onKeyDown(keyCode, event);
		}
	}

	public void passSearch(String searchUrl) {
		myClient.searchWiki(searchUrl);
	}

}
