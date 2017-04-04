package com.jtmcn.archwiki.viewer;

import android.app.ActionBar;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WikiChromeClient extends WebChromeClient {
	//// TODO: 4/3/2017 Fix memory leak 
	private static ProgressBar progressBar;
	private static TextView tvTitle;
	private static ActionBar actionBar;

	public WikiChromeClient(ProgressBar progressBar, TextView tvTitle, ActionBar actionBar) {
		WikiChromeClient.progressBar = progressBar;
		WikiChromeClient.tvTitle = tvTitle;
		WikiChromeClient.actionBar = actionBar;
	}

	public static void showProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}

	public static void hideProgress() {
		progressBar.setVisibility(View.GONE);
	}

	public static void setTvTitle(String title) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			actionBar.setSubtitle(title);
		} else {
			tvTitle.setText(title);
		}
	}

}
