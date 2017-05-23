package com.jtmcn.archwiki.viewer;

import android.app.ActionBar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.ProgressBar;

public class WikiChromeClient extends WebChromeClient {
	//// TODO: 4/3/2017 Fix memory leak, maybe remove this class?
	private static ProgressBar progressBar;
	private static ActionBar actionBar;

	public WikiChromeClient(ProgressBar progressBar, ActionBar actionBar) {
		WikiChromeClient.progressBar = progressBar;
		WikiChromeClient.actionBar = actionBar;
	}

	public static void showProgress() {
		if(progressBar != null) {
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	public static void hideProgress() {
		if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
		}
	}

	public static void setSubtitle(String title) {
		if (actionBar != null) {
			actionBar.setSubtitle(title);
		}
	}

}
