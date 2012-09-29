package com.jtmcn.archwiki.viewer;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class WikiActivity extends Activity implements OnClickListener {

	private WebView wikiViewer;
	static WikiClient myClient;
	LinearLayout titleBar;
	ProgressBar progress;
	Button searchButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();

		setContentView(R.layout.wiki_layout);

		initializeUI();

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			String searchUrl = "https://wiki.archlinux.org/index.php?title=Special%3ASearch&search="
					+ query;
			myClient.searchWiki(searchUrl);
		}
	}

	public void initializeUI() {
		// associate xml variables
		wikiViewer = (WebView) findViewById(R.id.wvMain);
		progress = (ProgressBar) findViewById(R.id.ProgressBar);
		searchButton = (Button) findViewById(R.id.search);
		searchButton.setOnClickListener(this);

		myClient = new WikiClient(wikiViewer, progress);
		wikiViewer.setWebViewClient(myClient);
		wikiViewer.loadUrl("file:///android_asset/startPage.html");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// restore the WikiClient
		myClient.restorePage();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history
		// history needs to be managed manually
		if ((keyCode == KeyEvent.KEYCODE_BACK)
				&& (myClient.histStackSize() == 1)) {
			// if there's only one entry load local html
			progress.setVisibility(View.VISIBLE);
			wikiViewer.loadUrl("file:///android_asset/startPage.html");
			myClient.reduceStackSize();
			return true;
		} else if ((keyCode == KeyEvent.KEYCODE_BACK)
				&& (myClient.histStackSize() > 1)) {
			progress.setVisibility(View.VISIBLE);
			myClient.goBackHistory();
			return true;
		} else {
			// if there are zero entries exit application
			return super.onKeyDown(keyCode, event);
		}
	}

	public void onClick(View v) {
		onSearchRequested();
	}

}