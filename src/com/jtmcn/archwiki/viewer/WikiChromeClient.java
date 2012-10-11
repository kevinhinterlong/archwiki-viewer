package com.jtmcn.archwiki.viewer;

import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WikiChromeClient extends WebChromeClient {

	static ProgressBar progressBar;
	static TextView tvTitle;

	public WikiChromeClient(ProgressBar progressBar, TextView tvTitle) {
		WikiChromeClient.progressBar = progressBar;
		WikiChromeClient.tvTitle = tvTitle;
	}

	public static void showProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}

	public static void hideProgress() {
		progressBar.setVisibility(View.GONE);
	}

	public static void setTvTitle(String title) {
		tvTitle.setText(title);
	}

}
