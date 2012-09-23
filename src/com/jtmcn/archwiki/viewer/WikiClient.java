package com.jtmcn.archwiki.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Stack;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


public class WikiClient extends WebViewClient {

	WebView myView;
	String webpage;
	String myUrl;
	ProgressBar myProg;
	Activity thisActivity;
	boolean pageFinished;

	private Stack<String> histStack = new Stack<String>();

	public WikiClient(Activity act, ProgressBar progress) {
		myProg = progress;
		thisActivity = act;
	}

	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		myView = view;
		myUrl = url;

		// check if page is part of the wiki
		if (myUrl.startsWith("https://wiki.archlinux.org/")) {

			pageFinished = false;
			myView.stopLoading();

			addHistory(myUrl);

			new Read().execute(url);

			myProg.setVisibility(View.VISIBLE);

			return false;
		} else {

			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

			thisActivity.startActivity(intent);
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

	public void searchWiki(WebView wikiViewer, String searchUrl) {
		myView = wikiViewer;
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

	private String buildPage(String urlString) {
		myUrl = urlString;
		try {
			URL url = new URL(urlString);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			urlConnection.setReadTimeout(10000 /* milliseconds */);
			urlConnection.setConnectTimeout(15000 /* milliseconds */);
			urlConnection.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));

			StringBuffer sb = new StringBuffer("");
			String l = "";
			String nl = System.getProperty("line.separator");

			while ((l = in.readLine()) != null) {
				sb.append(l + nl);
			}

			urlConnection.disconnect();
			in.close();

			String pageString = sb.toString();

			StringBuilder htmlString = new StringBuilder();
			htmlString.append(pageString);

			int headStart = htmlString.indexOf("<head>");
			int headEnd = htmlString.indexOf("</head>");

			String head = "<link rel='stylesheet' href='file:///android_asset/style.css'></head>";
			String page = htmlString.replace(headStart, headEnd, head)
					.toString();

			return page;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public int histStackSize() {
		int histSize = histStack.size();
		return histSize;
	}

	public void reduceStackSize() {
		histStack.removeAllElements();
	}

	public class Read extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			webpage = buildPage(params[0]); // url
			return webpage;
		}

		@Override
		protected void onPostExecute(String result) {
			pageFinished = true;

			String urlStr = "https://wiki.archlinux.org/";
			String mimeType = "text/html";
			String encoding = "UTF-8";
			String pageData = result;

			myView.loadDataWithBaseURL(urlStr, pageData, mimeType, encoding,
					null);

		}

	}

}