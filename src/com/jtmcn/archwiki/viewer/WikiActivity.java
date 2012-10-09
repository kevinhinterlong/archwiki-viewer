package com.jtmcn.archwiki.viewer;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WikiActivity extends Activity implements OnClickListener {

	private WikiView wikiViewer;

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
//			myClient.searchWiki(searchUrl);
		}
	}

	public void initializeUI() {
		// associate xml variables
		wikiViewer = (WikiView) findViewById(R.id.wvMain);
		progress = (ProgressBar) findViewById(R.id.ProgressBar);
		searchButton = (Button) findViewById(R.id.search);
		searchButton.setOnClickListener(this);


	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// restore the WikiClient
//		myClient.restorePage();
	}

	public void onClick(View v) {
		onSearchRequested();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	        Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
	    }
	}

	
}