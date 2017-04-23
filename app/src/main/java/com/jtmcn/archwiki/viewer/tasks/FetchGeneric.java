package com.jtmcn.archwiki.viewer.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.jtmcn.archwiki.viewer.utils.NetworkUtils;

import java.io.IOException;

public class FetchGeneric<Result> extends AsyncTask<String, Integer, Result> {
	public interface OnFinish<Result> {
		void onFinish(Result results);
	}
	public interface FetchGenericMapper<T,R> {
		R mapTo(String url, T t);
	}

	public static final String TAG = FetchGeneric.class.getSimpleName();
	private OnFinish<Result> onFinish;
	private FetchGenericMapper<StringBuilder, Result> mapper;

	/**
	 * Fetches a list of urls and publishes progress on the {@link OnFinish} listener.
	 *
	 * @param onFinish The listener to be called when progress is ready.
	 * @param mapper   The function to map from the url and downloaded page to the desired type.
	 */
	public FetchGeneric(
			OnFinish<Result> onFinish,
			FetchGenericMapper<StringBuilder, Result> mapper
	) {
		this.onFinish = onFinish;
		this.mapper = mapper;
	}

	@Override
	protected Result doInBackground(String... params) {
		String url = params[0];
		StringBuilder toAdd = getItem(url);
		if (toAdd != null) {
			return mapper.mapTo(url, toAdd);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Result values) {
		super.onPostExecute(values);
		if (onFinish != null) {
			onFinish.onFinish(values);
		}
	}

	/**
	 * Fetches a list of movies from the URL
	 *
	 * @param url to query
	 */
	private StringBuilder getItem(String url) {
		StringBuilder toReturn = null;
		try {
			toReturn = NetworkUtils.fetchURL(url);
		} catch (IOException e) { //network exception
			Log.w(TAG, "Could not connect to: " + url, e);
			toReturn = new StringBuilder("Could not connect");
		}

		return toReturn;
	}
}
