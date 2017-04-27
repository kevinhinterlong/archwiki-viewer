package com.jtmcn.archwiki.viewer;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jtmcn.archwiki.viewer.data.WikiPage;
import com.jtmcn.archwiki.viewer.tasks.Fetch;
import com.jtmcn.archwiki.viewer.tasks.FetchGeneric;
import com.jtmcn.archwiki.viewer.utils.AndroidUtils;

import java.util.Stack;

import static com.jtmcn.archwiki.viewer.Constants.ARCHWIKI_BASE;
import static com.jtmcn.archwiki.viewer.Constants.TEXT_HTML_MIME;
import static com.jtmcn.archwiki.viewer.Constants.UTF_8;

public class WikiClient extends WebViewClient implements FetchGeneric.OnFinish<WikiPage> {
	public static final String TAG = WikiClient.class.getSimpleName();
	private WebView webView;
	private boolean pageFinished;
	private Stack<WikiPage> webpageStack = new Stack<>();

	public WikiClient(WebView wikiViewer) {
		webView = wikiViewer;
	}

	/*
	 * Manage page history
	 */
	public void addHistory(WikiPage wikiPage) {
		webpageStack.push(wikiPage);
		Log.d(TAG, "Adding page " + wikiPage.getPageTitle() + ". Stack size now at " + webpageStack.size());
	}

	public void loadWikiHtml(WikiPage wikiPage) {
		// load the page in webview
		webView.loadDataWithBaseURL(
				ARCHWIKI_BASE,
				wikiPage.getHtmlString(),
				TEXT_HTML_MIME,
				UTF_8,
				null
		);
	}

	/*
	 * Intercept url when clicked. If it's part of the wiki create a new thread
	 * to load the page. If not, open the device's default browser.
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (url.startsWith(ARCHWIKI_BASE)) {
			pageFinished = false;

			webView.stopLoading();

			Fetch.page(this).execute(url);

			WikiChromeClient.showProgress();

			return false;
		} else {
			AndroidUtils.openLink(url, view.getContext());
			return true;
		}
	}

	/*
	 * Show Toast message on error. This is never called...?
	 */
	public void onReceivedError(WebView view, int errorCode,
								String description, String failingUrl) {
		Toast.makeText(view.getContext(), "Error: " + description,
				Toast.LENGTH_SHORT).show();
	}

	/*
	 * When everything is done, turn off progress wheel. The boolean is
	 * necessary for the progressBar to continue after initial
	 * webView.get().stopLoading();
	 */
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		if (pageFinished) {
			WikiChromeClient.hideProgress();
		}
		setPageTitle();
	}

	/*
	 * Execute new thread to create search page
	 */
	public void searchWiki(String searchUrl) {
		Fetch.page(this).execute(searchUrl);
		WikiChromeClient.showProgress();
	}

	public void resetStackSize() {
		// called on local html page reload
		webpageStack.removeAllElements();
	}

	public int histStackSize() {
		return webpageStack.size();
	}

	public void setPageTitle() {
		String pageTitle = null;
		if (webpageStack.size() > 0) {
			pageTitle = webpageStack.elementAt(webpageStack.size() - 1).getPageTitle();
		}

		WikiChromeClient.setSubtitle(pageTitle);
	}

	public void goBackHistory() {
		WikiPage removed = webpageStack.pop();
		Log.d(TAG, "Removing " + removed.getPageTitle() + " from stack");
		loadWikiHtml(webpageStack.peek());
	}

	public WikiPage getCurrentWebPage() {
		return webpageStack.size() == 0 ? null : webpageStack.peek();
	}

	@Override
	public void onFinish(WikiPage results) {
		addHistory(results);
		loadWikiHtml(getCurrentWebPage());
		pageFinished = true;
	}
}