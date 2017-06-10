package com.jtmcn.archwiki.viewer;

import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jtmcn.archwiki.viewer.data.WikiPage;
import com.jtmcn.archwiki.viewer.tasks.Fetch;
import com.jtmcn.archwiki.viewer.tasks.FetchUrl;
import com.jtmcn.archwiki.viewer.utils.AndroidUtils;

import java.util.Stack;

import static com.jtmcn.archwiki.viewer.Constants.ARCHWIKI_BASE;
import static com.jtmcn.archwiki.viewer.Constants.TEXT_HTML_MIME;
import static com.jtmcn.archwiki.viewer.Constants.UTF_8;

public class WikiClient extends WebViewClient implements FetchUrl.OnFinish<WikiPage> {
	public static final String TAG = WikiClient.class.getSimpleName();
	private final WebView webView;
	private final Stack<WikiPage> webpageStack = new Stack<>();
	private final ProgressBar progressBar;
	private final ActionBar actionBar;

	public WikiClient(ProgressBar progressBar, ActionBar actionBar, WebView wikiViewer) {
		this.progressBar = progressBar;
		this.actionBar = actionBar;
		webView = wikiViewer;
	}

	/*
	 * Manage page history
	 */
	public void addHistory(WikiPage wikiPage) {
		if (webpageStack.size() > 0) {
			Log.d(TAG, "Saving " + getCurrentWebPage().getPageTitle() + " at " + webView.getScrollY());
			getCurrentWebPage().setScrollPosition(webView.getScrollY());
		}
		webpageStack.push(wikiPage);
		Log.i(TAG, "Adding page " + wikiPage.getPageTitle() + ". Stack size= " + webpageStack.size());
	}

	/**
	 * Loads the html from a {@link WikiPage} into the webview.
	 *
	 * @param wikiPage the page to be loaded.
	 */
	public void loadWikiHtml(WikiPage wikiPage) {
		webView.loadDataWithBaseURL(
				wikiPage.getPageUrl(),
				wikiPage.getHtmlString(),
				TEXT_HTML_MIME,
				UTF_8,
				null
		);

		setSubtitle(wikiPage.getPageTitle());
	}

	/**
	 * Intercept url when clicked. If it's part of the wiki load it here.
	 * If not, open the device's default browser.
	 *
	 * @param view webview being loaded into
	 * @param url  url being loaded
	 * @return true if should override url loading
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// deprecated until min api 21 is used
		if (url.startsWith(ARCHWIKI_BASE)) {
			webView.stopLoading();
			Fetch.page(this, url, true);
			showProgress();

			return false;
		} else {
			AndroidUtils.openLink(url, view.getContext());
			return true;
		}
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		Log.d(TAG, "Calling onPageFinished(view, url)");
		final WikiPage currentWebPage = getCurrentWebPage();
		// make sure we're loading the current page and that
		// this page's url doesn't have an anchor (only on first page load)
		if (url.equals(currentWebPage.getPageUrl())) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					int scrollY = currentWebPage.setScrollPosition();
					Log.d(TAG, "Restoring " + currentWebPage.getPageTitle() + " at " + scrollY);
					webView.setScrollY(scrollY);
					hideProgress();
				}
			}, 25);
		}
	}

	public void showProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}

	public void hideProgress() {
		progressBar.setVisibility(View.GONE);
	}

	public void setSubtitle(String title) {
		if (actionBar != null) {
			actionBar.setSubtitle(title);
		}
	}

	/**
	 * Get the number of pages that are in the history.
	 *
	 * @return number of pages on the stack.
	 */
	public int getHistoryStackSize() {
		return webpageStack.size();
	}

	/**
	 * Go back to the last loaded page.
	 */
	public void goBackHistory() {
		WikiPage removed = webpageStack.pop();
		Log.i(TAG, "Removing " + removed.getPageTitle() + " from stack");
		WikiPage newPage = webpageStack.peek();
		loadWikiHtml(newPage);
	}

	/**
	 * Returns null or the current page.
	 *
	 * @return The current page
	 */
	public WikiPage getCurrentWebPage() {
		return webpageStack.size() == 0 ? null : webpageStack.peek();
	}

	@Override
	public void onFinish(WikiPage results) {
		addHistory(results);
		loadWikiHtml(getCurrentWebPage());
	}

	public void refreshPage() {
		WikiPage currentWebPage = getCurrentWebPage();
		currentWebPage.setScrollPosition(0);

		String url = currentWebPage.getPageUrl();
		Fetch.page(new FetchUrl.OnFinish<WikiPage>() {
			@Override
			public void onFinish(WikiPage wikiPage) {
				loadWikiHtml(wikiPage);
			}
		}, url, false);
	}
}