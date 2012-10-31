package com.jtmcn.archwiki.viewer;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class WikiActivity extends Activity implements OnClickListener {

	private WikiView wikiViewer;
	LinearLayout titleBar;
	TextView tvTitle;
	String titleString;
	ProgressBar progressBar;
	Button searchButton;
	Button overflowButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();

		setContentView(R.layout.wiki_layout);

		initializeUI();
		setWebSettings();

		// reset historyStacks
		wikiViewer.resetApplication();

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			String searchUrl = "https://wiki.archlinux.org/index.php?&search="
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

		overflowButton = (Button) findViewById(R.id.overflow);
		overflowButton.setOnClickListener(this);

		tvTitle = (TextView) findViewById(R.id.title);

		WikiChromeClient wikiChrome = new WikiChromeClient(progressBar, tvTitle);
		wikiViewer.setWebChromeClient(wikiChrome);

	}

	public void setWebSettings() {
		WebSettings webSettings = wikiViewer.getSettings();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		// due to a known bug, the size cannot be stored in an integer array
		String fontSizePref = prefs.getString("listPref", "2");
		int fontSize = Integer.valueOf(fontSizePref);

		// deprecated method must be used for consistency with variable
		// resolutions and dpi

		switch (fontSize) {
		case 0:
			webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
			break;
		case 1:
			webSettings.setTextSize(WebSettings.TextSize.SMALLER);
			break;
		case 2:
			webSettings.setTextSize(WebSettings.TextSize.NORMAL);
			break;
		case 3:
			webSettings.setTextSize(WebSettings.TextSize.LARGER);
			break;
		case 4:
			webSettings.setTextSize(WebSettings.TextSize.LARGEST);
			break;
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			onSearchRequested();
			break;
		case R.id.overflow:
			openOptionsMenu();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent p = new Intent("com.jtmcn.archwiki.viewer.WIKIPREFS");
			startActivityForResult(p, 0);
			break;
		case R.id.exit:
			this.finish();
			break;
		}
		return true;
	}

	/*
	 * Activity needs to be reloaded if the font size is changed. This isn't
	 * ideal because it also reloads when nothing changes.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		setWebSettings();
	}
}