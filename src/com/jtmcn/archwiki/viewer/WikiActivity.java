package com.jtmcn.archwiki.viewer;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WikiActivity extends Activity implements OnClickListener {

	private WikiView wikiViewer;
	LinearLayout titleBar;
	TextView tvTitle;
	String titleString;
	ProgressBar progressBar;
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
			wikiViewer.passSearch(searchUrl);

		}
	}

	public void initializeUI() {
		// associate xml variables
		wikiViewer = (WikiView) findViewById(R.id.wvMain);
		progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
		searchButton = (Button) findViewById(R.id.search);
		searchButton.setOnClickListener(this);
		
		tvTitle = (TextView) findViewById(R.id.title); 
		
		WikiChromeClient myChrome = new WikiChromeClient(progressBar, tvTitle);
		wikiViewer.setWebChromeClient(myChrome);
	}

	public void onClick(View v) {
		onSearchRequested();
	}

}