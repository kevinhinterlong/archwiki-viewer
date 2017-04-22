package com.jtmcn.archwiki.viewer;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.jtmcn.archwiki.viewer.data.WikiPage;
import com.jtmcn.archwiki.viewer.utils.AndroidUtils;

import java.text.MessageFormat;

import static com.jtmcn.archwiki.viewer.Constants.QUERY_URL;

public class WikiActivity extends Activity {
	private Menu optionMenu;
	private WikiView wikiViewer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wiki_layout);

		Intent intent = getIntent();

		initializeUI();
		setWebSettings();

		// reset historyStacks
		wikiViewer.resetApplication();

		handleIntent(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				optionMenu.findItem(R.id.menu_search).collapseActionView();
			}
			String query = intent.getStringExtra(SearchManager.QUERY);
			String searchUrl = MessageFormat.format(QUERY_URL, query);
			wikiViewer.passSearch(searchUrl);
		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			String url = intent.getDataString();
			wikiViewer.wikiClient.shouldOverrideUrlLoading(wikiViewer, url);
		}
	}

	public void initializeUI() {
		// associate xml variables
		wikiViewer = (WikiView) findViewById(R.id.wvMain);
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);

		WikiChromeClient wikiChrome = new WikiChromeClient(progressBar, getActionBar());

		wikiViewer.setWebChromeClient(wikiChrome);
	}

	public void setWebSettings() {
		WebSettings webSettings = wikiViewer.getSettings();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		// due to a known bug(?), the size cannot be stored in an integer array
		String fontSizePref = prefs.getString(WikiPrefs.KEY_LIST_PREFERENCE, "2");
		int fontSize = Integer.valueOf(fontSizePref);

		// deprecated method must be used for consistency with variable
		// resolutions and dpi
		//https://developer.android.com/reference/android/webkit/WebSettings.TextSize.html#NORMAL
		switch (fontSize) {
			case 0:
				webSettings.setTextSize(WebSettings.TextSize.SMALLEST); //50%
				break;
			case 1:
				webSettings.setTextSize(WebSettings.TextSize.SMALLER); //75%
				break;
			case 2:
				webSettings.setTextSize(WebSettings.TextSize.NORMAL); //100%
				break;
			case 3:
				webSettings.setTextSize(WebSettings.TextSize.LARGER); //150%
				break;
			case 4:
				webSettings.setTextSize(WebSettings.TextSize.LARGEST); //200%
				break;
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options, menu);
		optionMenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_settings:
				Intent p = new Intent("com.jtmcn.archwiki.viewer.WIKIPREFS");
				startActivityForResult(p, 0);
				break;
			case R.id.menu_share:
				WikiPage wikiPage = wikiViewer.getCurrentWebPage();
				if (wikiPage != null && !wikiPage.getPageUrl().isEmpty()) {
					AndroidUtils.shareText(wikiPage.getPageTitle(), wikiPage.getPageUrl(), this);
				} else {
					Toast.makeText(this, "Sorry, can't share this page!", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.exit:
				finish();
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