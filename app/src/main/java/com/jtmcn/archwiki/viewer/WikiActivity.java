package com.jtmcn.archwiki.viewer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class WikiActivity extends Activity implements OnClickListener {

    private WikiView wikiViewer;
    TextView tvTitle;
    ProgressBar progressBar;
    Button searchButton;
    Button overflowButton;
    Menu optionMenu;

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                optionMenu.findItem(R.id.menu_search).collapseActionView();
            }
            String query = intent.getStringExtra(SearchManager.QUERY);
            String searchUrl = "https://wiki.archlinux.org/index.php?&search="
                    + query;
            wikiViewer.passSearch(searchUrl);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            final String url = intent.getDataString();
            wikiViewer.wikiClient.shouldOverrideUrlLoading(wikiViewer, url);
        }
    }

    public void initializeUI() {
        // associate xml variables
        wikiViewer = (WikiView) findViewById(R.id.wvMain);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);

        WikiChromeClient wikiChrome;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wikiChrome = new WikiChromeClient(progressBar, null, getActionBar());
        } else {
            searchButton = (Button) findViewById(R.id.search);
            searchButton.setOnClickListener(this);

            overflowButton = (Button) findViewById(R.id.overflow);
            overflowButton.setOnClickListener(this);

            tvTitle = (TextView) findViewById(R.id.title);
            wikiChrome = new WikiChromeClient(progressBar, tvTitle, null);
        }

        wikiViewer.setWebChromeClient(wikiChrome);
    }

    public void setWebSettings() {
        WebSettings webSettings = wikiViewer.getSettings();

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        // due to a known bug(?), the size cannot be stored in an integer array
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
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
                String url = ArchWikiApplication.getInstance().getCurrentUrl();
                String title = ArchWikiApplication.getInstance().getCurrentTitle();
                if (url != null && !url.isEmpty()) {
                    Utils.shareText(title, url, this);
                } else {
                    Toast.makeText(this, "Sorry, can't share this page!", Toast.LENGTH_SHORT).show();
                }
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