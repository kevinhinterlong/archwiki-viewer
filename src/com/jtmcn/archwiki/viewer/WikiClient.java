package com.jtmcn.archwiki.viewer;

import java.lang.ref.WeakReference;
import java.util.Stack;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WikiClient extends WebViewClient {

	static WikiPageBuilder webpage;
	static String savedPage;
	static boolean pageFinished;
	String myUrl;
//	ProgressBar myProg;
	Context context;

	protected static WeakReference<WebView> wrWeb;

	private Stack<String> histStack = new Stack<String>();

//	public WikiClient(WebView wikiViewer, ProgressBar progress) {
	public WikiClient(WebView wikiViewer) {
		wrWeb = new WeakReference<WebView>(wikiViewer);
//		myProg = progress;
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

			addHistory(myUrl);

			new Read().execute(url);

//			myProg.setVisibility(View.VISIBLE);

			return false;
		} else {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			context.startActivity(intent);
			return true;
		}
	}

	/*
	 * Show Toast message on error. Either my code has been perfect or this
	 * doesn't ever get called.
	 */
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		Toast.makeText(view.getContext(), "Error: " + description,
				Toast.LENGTH_SHORT).show();
	}

	/*
	 * When everything is done, turn off progress wheel
	 */
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
//		if (pageFinished)
//			myProg.setVisibility(View.GONE);
	}

	/*
	 * Execute new thread to create search page
	 */
	public void searchWiki(String searchUrl) {
		new Read().execute(searchUrl);
	}

	/*
	 * Manage data to be reloaded on orientation change
	 */
	public static void savePage(String sPage) {
		savedPage = sPage;
	}

	public void restorePage() {
		String urlStr = "https://wiki.archlinux.org/";
		String mimeType = "text/html";
		String encoding = "UTF-8";

		wrWeb.get().loadDataWithBaseURL(urlStr, savedPage, mimeType, encoding,
				null);
	}

	/*
	 * Manage page history
	 */
	public void addHistory(String histUrl) {
		histStack.push(histUrl);
	}

	public void reduceStackSize() {
		// called on local html page reload
		histStack.removeAllElements();
	}

	public int histStackSize() {
		int histSize = histStack.size();
		return histSize;
	}

	public String getHistory() {
		// load the 2nd to last page
		String loadUrl = histStack.elementAt(histStack.size() - 2);
		// remove the current page
		histStack.remove(histStack.size() - 1);
		return loadUrl;
	}

	public void goBackHistory() {
		String prevUrl = getHistory();
		new Read().execute(prevUrl);
	}

	/*
	 * Background thread to download and manipulate page data.
	 */
	private static class Read extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			webpage = new WikiPageBuilder(params[0]); // url
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			pageFinished = true;

			String urlStr = "https://wiki.archlinux.org/";
			String mimeType = "text/html";
			String encoding = "UTF-8";
			String pageData = webpage.getHtmlString();

			// load the page in webview
			wrWeb.get().loadDataWithBaseURL(urlStr, pageData, mimeType,
					encoding, null);
			// save page data string incase of orientation change to
			// prevent unnecessary reloading
			savePage(pageData);
		}

	}

}