package com.jtmcn.archwiki.viewer;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jtmcn.archwiki.viewer.data.WikiPage;
import com.jtmcn.archwiki.viewer.tasks.FetchWikiPage;
import com.jtmcn.archwiki.viewer.tasks.OnProgressChange;
import com.jtmcn.archwiki.viewer.utils.AndroidUtils;

import java.util.List;
import java.util.Stack;

import static com.jtmcn.archwiki.viewer.Constants.ARCHWIKI_BASE;
import static com.jtmcn.archwiki.viewer.Constants.TEXT_HTML_MIME;
import static com.jtmcn.archwiki.viewer.Constants.UTF_8;

public class WikiClient extends WebViewClient implements OnProgressChange<WikiPage> {
	//// TODO: 4/22/2017 maybe give this class an interface to make changes on.
	protected WebView webView;
	private boolean pageFinished;
	// todo let's create this class in the main activity and on startup push the main wiki page so everything is in the stack
	private WikiPage currentWikiPage;
	private Stack<WikiPage> webpageStack = new Stack<>();

	public WikiClient(WebView wikiViewer) {
		webView = wikiViewer;
	}

	/*
	 * Manage page history
	 */
	public void addHistory(WikiPage wikiPage) {
		webpageStack.push(wikiPage);
	}

	public void loadWikiHtml(String wikiHtml) {
		// load the page in webview
		webView.loadDataWithBaseURL(
				ARCHWIKI_BASE,
				wikiHtml,
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

			new FetchWikiPage(this).execute(url);

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
		new FetchWikiPage(this).execute(searchUrl);
		WikiChromeClient.showProgress();
	}

	public void resetStackSize() {
		// called on local html page reload
		webpageStack.removeAllElements();
	}

	public int histStackSize() {
		return webpageStack.size();
	}

	public String getHistory() {
		// load the 2nd to last page
		WikiPage wikiPage = webpageStack.elementAt(webpageStack.size() - 2);

		// remove the current page
		webpageStack.remove(webpageStack.size() - 1);
		return wikiPage.getHtmlString();
	}

	public void setPageTitle() {
		String pageTitle = null;
		if (webpageStack.size() > 0) {
			pageTitle = webpageStack.elementAt(webpageStack.size() - 1).getPageTitle();
		}

		WikiChromeClient.setSubtitle(pageTitle);
	}

	public void goBackHistory() {
		String prevHtml = getHistory();
		loadWikiHtml(prevHtml);
	}

	public WikiPage getCurrentWebPage() {
		return currentWikiPage;
	}

	@Override
	public void onAdd(WikiPage wikiPage) {
		currentWikiPage = wikiPage;
	}

	@Override
	public void onFinish(List<WikiPage> results) {

	}

	@Override
	public void onProgressUpdate(int value) {
		loadWikiHtml(currentWikiPage.getHtmlString());
		addHistory(currentWikiPage);
		pageFinished = true;
	}
}