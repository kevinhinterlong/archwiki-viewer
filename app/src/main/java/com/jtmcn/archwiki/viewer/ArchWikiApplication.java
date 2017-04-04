package com.jtmcn.archwiki.viewer;

import android.app.Application;

/**
 * Application singleton for the ArchWiki app
 * Created by vishnu on 2/22/14.
 */
public class ArchWikiApplication extends Application {
	public static final ArchWikiApplication mApplication = new ArchWikiApplication();
	private String mCurrentUrl;
	private String mCurrentTitle;

	public static ArchWikiApplication getInstance() {
		return mApplication;
	}

	public String getCurrentUrl() {
		return mCurrentUrl;
	}

	public void setCurrentUrl(String currentUrl) {
		mCurrentUrl = currentUrl;
	}

	public String getCurrentTitle() {
		return mCurrentTitle;
	}

	public void setCurrentTitle(String currentTitle) {
		mCurrentTitle = currentTitle;
	}
}
