package com.jtmcn.archwiki.viewer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jtmcn.archwiki.viewer.data.WikiPage;
import com.jtmcn.archwiki.viewer.data.WikiPageBuilder;
import com.jtmcn.archwiki.viewer.utils.AndroidUtils;

import java.lang.ref.WeakReference;
import java.util.Stack;

import static com.jtmcn.archwiki.viewer.Constants.ARCHWIKI_BASE;
import static com.jtmcn.archwiki.viewer.Constants.TEXT_HTML_MIME;
import static com.jtmcn.archwiki.viewer.Constants.UTF_8;

public class WikiClient extends WebViewClient {
	protected static WeakReference<WebView> wrWeb;
	private static boolean pageFinished;
	private static WikiPage webpage;
	private static Stack<WikiPage> webpageStack = new Stack<>();
	private String myUrl; //todo replace with WikiPage variable
	private String pageTitle;

	public WikiClient(WebView wikiViewer) {
		wrWeb = new WeakReference<>(wikiViewer);
	}

	/*
	 * Manage page history
	 */
	public static void addHistory(WikiPage wikiPage) {
		webpageStack.push(wikiPage);
	}

	public static void loadWikiHtml(String wikiHtml) {
		// load the page in webview
		wrWeb.get().loadDataWithBaseURL(
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
		myUrl = url;
		if (myUrl.startsWith(ARCHWIKI_BASE)) {
			pageFinished = false;

			wrWeb.get().stopLoading();

			new Read().execute(myUrl);

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
		webpageStack.removeAllElements();
		// pageTitle = null;
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

	public String getPageTitle() {

		return pageTitle;
	}

	public void setPageTitle() {
		if (webpageStack.size() > 0) {
			pageTitle = webpageStack.elementAt(webpageStack.size() - 1).getPageTitle();
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			pageTitle = null;
		} else {
			ArchWikiApplication instance = ArchWikiApplication.getInstance();
			if (instance != null) {
				Context context = ArchWikiApplication.getInstance().getApplicationContext();
				pageTitle = context.getString(R.string.app_name);
			}
		}

		WikiChromeClient.setTvTitle(pageTitle);
	}

	public void goBackHistory() {
		String prevHtml = getHistory();
		loadWikiHtml(prevHtml);
	}

	public WikiPage getCurrentWebPage() {
		return webpage;
	}

	/*
	 * Background thread to download and manipulate page data.
	 * todo create separate class for async task
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

			loadWikiHtml(webpage.getHtmlString());

			addHistory(webpage);

		}

	}

}