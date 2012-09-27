package com.jtmcn.archwiki.viewer;

import java.lang.ref.WeakReference;
import java.util.Stack;

import android.app.Activity;
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

	static BuildWikiPage webpage;
	String myUrl;
	ProgressBar myProg;
	Context context;

	static boolean pageFinished;
	protected static WeakReference<WebView> wrWeb;

	private Stack<String> histStack = new Stack<String>();

	public WikiClient(WebView wikiViewer, ProgressBar progress) {
		wrWeb = new WeakReference<WebView>(wikiViewer);
		myProg = progress;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {

		myUrl = url;

		// check if page is part of the wiki
		if (myUrl.startsWith("https://wiki.archlinux.org/")) {

			pageFinished = false;
			wrWeb.get().stopLoading();
			addHistory(myUrl);

			new Read().execute(url);

			myProg.setVisibility(View.VISIBLE);

			return false;
		} else {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			context.startActivity(intent);
			return true;
		}

	}

	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		Toast.makeText(view.getContext(), "Error: " + description,
				Toast.LENGTH_SHORT).show();
	}

	public void addHistory(String histUrl) {
		histStack.push(histUrl);
	}

	public void searchWiki(String searchUrl) {
		new Read().execute(searchUrl);
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

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		if (pageFinished)
			myProg.setVisibility(View.GONE);
	}

	public int histStackSize() {
		int histSize = histStack.size();
		return histSize;
	}

	public void reduceStackSize() {
		histStack.removeAllElements();
	}

	private static class Read extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			webpage = new BuildWikiPage(params[0]); // url
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			pageFinished = true;

			String urlStr = "https://wiki.archlinux.org/";
			String mimeType = "text/html";
			String encoding = "UTF-8";
			String pageData = webpage.getHtmlString();

			wrWeb.get().loadDataWithBaseURL(urlStr, pageData, mimeType,
					encoding, null);

		}

	}

}