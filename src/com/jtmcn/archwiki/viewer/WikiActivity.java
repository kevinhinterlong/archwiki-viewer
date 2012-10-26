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

		WikiChromeClient myChrome = new WikiChromeClient(progressBar, tvTitle);
		wikiViewer.setWebChromeClient(myChrome);
	}

	public void setWebSettings() {
		WebSettings webSettings = wikiViewer.getSettings();
		// webSettings.setBuiltInZoomControls(true);

		PreferenceManager.setDefaultValues(getApplicationContext(),
				R.xml.settings, false);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String fontSize = prefs.getString("listPref", "Normal");
		// Toast.makeText(this, "fontSize: " + fontSize,
		// Toast.LENGTH_LONG).show();

		int fontDpi = Integer.parseInt(fontSize);

		webSettings.setDefaultFontSize(fontDpi);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			onSearchRequested();
			break;
		case R.id.overflow:
			// Toast.makeText(this, "Overflow", Toast.LENGTH_LONG).show();
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
			// Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
			Intent p = new Intent("com.jtmcn.archwiki.viewer.WIKIPREFS");
			startActivityForResult(p, 0);
			break;
		case R.id.exit:
			// Toast.makeText(this, "Exit!", Toast.LENGTH_LONG).show();
			// tvTitle = null;
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