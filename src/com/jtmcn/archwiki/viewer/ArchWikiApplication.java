package com.jtmcn.archwiki.viewer;

import android.app.Application;

/**
 * Application singleton for the ArchWiki app
 * Created by vishnu on 2/22/14.
 */
public class ArchWikiApplication extends Application{
    public String mCurrentUrl;
    public String mCurrentTitle;
    public static final ArchWikiApplication mApplication = new ArchWikiApplication();

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

    public static ArchWikiApplication getInstance(){
        return mApplication;
    }
}
