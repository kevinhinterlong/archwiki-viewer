package com.jtmcn.archwiki.viewer;

import android.app.Application;

/**
 * Application singleton for the ArchWiki app
 * Created by vishnu on 2/22/14.
 */
public class ArchWikiApplication extends Application {
	private static ArchWikiApplication mApplication;
	private String mCurrentUrl;
	private String mCurrentTitle;

	public static ArchWikiApplication getInstance() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
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
