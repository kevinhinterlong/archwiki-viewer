package com.jtmcn.archwiki.viewer;

import android.os.AsyncTask;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Stack;

public class WikiClient extends WebViewClient {

	protected static WeakReference<WebView> wrWeb;
	private static boolean pageFinished;
	private static WikiPage webpage;
	private static Stack<String> histHtmlStack = new Stack<>();
	private static Stack<String> histTitleStack = new Stack<>();
	private String myUrl;
	private String pageTitle;

	public WikiClient(WebView wikiViewer) {
		wrWeb = new WeakReference<>(wikiViewer);
	}

	/*
	 * Manage page history
	 */
	public static void addHistory(String histHtml, String histTitle) {
		histHtmlStack.push(histHtml);
		histTitleStack.push(histTitle);
	}

	public static void loadWikiHtml(String wikiHtml) {
		String urlStr = "https://wiki.archlinux.org/";
		String mimeType = "text/html";
		String encoding = "UTF-8";

		// load the page in webview
		wrWeb.get().loadDataWithBaseURL(urlStr, wikiHtml, mimeType, encoding,
				null);
	}

	/*
	 * Intercept url when clicked. If it's part of the wiki create a new thread
	 * to load the page. If not, open the device's default browser.
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		myUrl = url;
		if (myUrl.startsWith("https://wiki.archlinux.org/")) {
			pageFinished = false;

			wrWeb.get().stopLoading();

			new Read().execute(myUrl);

			WikiChromeClient.showProgress();

			return false;
		} else {
			Utils.openLink(url, view.getContext());
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
	 * wrWeb.get().stopLoading();
	 */
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		if (pageFinished)
			WikiChromeClient.hideProgress();
		setPageTitle();
	}

	/*
	 * Execute new thread to create search page
	 */
	public void searchWiki(String searchUrl) {
		new Read().execute(searchUrl);
		WikiChromeClient.showProgress();
	}

	public void resetStackSize() {
		// called on local html page reload
		histHtmlStack.removeAllElements();
		histTitleStack.removeAllElements();
		// pageTitle = null;
	}

	public int histStackSize() {
		return histHtmlStack.size();
	}

	public String getHistory() {
		// load the 2nd to last page
		String loadHtml = histHtmlStack.elementAt(histHtmlStack.size() - 2);

		// remove the current page
		histHtmlStack.remove(histHtmlStack.size() - 1);
		histTitleStack.remove(histTitleStack.size() - 1);
		return loadHtml;
	}

	public String getPageTitle() {

		return pageTitle;
	}

	public void setPageTitle() {

		if (histTitleStack.size() > 0)
			pageTitle = histTitleStack.elementAt(histTitleStack.size() - 1);
		else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			pageTitle = null;
		else
			pageTitle = ArchWikiApplication.getInstance().getApplicationContext().getString(R.string.app_name);

		WikiChromeClient.setTvTitle(pageTitle);
	}

	public void goBackHistory() {
		String prevHtml = getHistory();
		loadWikiHtml(prevHtml);
	}

	/*
	 * Background thread to download and manipulate page data.
	 */
	private static class Read extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			webpage = WikiPageBuilder.getWikiPage(params[0]); // url
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			pageFinished = true;

			String pageData = webpage.getHtmlString();

			String pageTitleData = webpage.getPageTitle();

			ArchWikiApplication.getInstance().setCurrentTitle(pageTitleData);
			loadWikiHtml(pageData);

			addHistory(pageData, pageTitleData);

		}

	}

}