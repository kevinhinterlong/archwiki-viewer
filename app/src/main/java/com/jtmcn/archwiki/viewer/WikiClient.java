package com.jtmcn.archwiki.viewer;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
	private WebView webView;
	private Stack<WikiPage> webpageStack = new Stack<>();

	public WikiClient(WebView wikiViewer) {
		webView = wikiViewer;
	}

	/*
	 * Manage page history
	 */
	public void addHistory(WikiPage wikiPage) {
		webpageStack.push(wikiPage);
		Log.d(TAG, "Adding page " + wikiPage.getPageTitle() + ". Stack size= " + webpageStack.size());
	}

	/**
	 * Loads the html from a {@link WikiPage} into the webview.
	 *
	 * @param wikiPage the page to be loaded.
	 */
	public void loadWikiHtml(WikiPage wikiPage) {
		webView.loadDataWithBaseURL(
				ARCHWIKI_BASE,
				wikiPage.getHtmlString(),
				TEXT_HTML_MIME,
				UTF_8,
				null
		);

		WikiChromeClient.setSubtitle(wikiPage.getPageTitle());
		WikiChromeClient.hideProgress();
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
		if (url.startsWith(ARCHWIKI_BASE)) {
			webView.stopLoading();
			Fetch.page(this, url);
			WikiChromeClient.showProgress();

			return false;
		} else {
			AndroidUtils.openLink(url, view.getContext());
			return true;
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
		Log.d(TAG, "Removing " + removed.getPageTitle() + " from stack");
		loadWikiHtml(webpageStack.peek());
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
}