package com.jtmcn.archwiki.viewer;

import android.app.ActionBar;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WikiChromeClient extends WebChromeClient {

	static ProgressBar progressBar;
	static TextView tvTitle;
	static ActionBar actionBar;

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
