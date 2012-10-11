package com.jtmcn.archwiki.viewer;

import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.ProgressBar;

public class WikiChromeClient extends WebChromeClient {

	static ProgressBar progressBar;

	public WikiChromeClient(ProgressBar progressBar) {
		WikiChromeClient.progressBar = progressBar;
	}

	public static void showProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}

	public static void hideProgress() {
		progressBar.setVisibility(View.GONE);
	}

}
