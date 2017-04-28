package com.jtmcn.archwiki.viewer.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.jtmcn.archwiki.viewer.utils.NetworkUtils;

import java.io.IOException;

public class FetchUrl<Result> extends AsyncTask<String, Integer, Result> {
	public interface OnFinish<Result> {
		void onFinish(Result results);
	}
	public interface FetchGenericMapper<R> {
		R mapTo(String url, StringBuilder sb);
	}

	public static final String TAG = FetchUrl.class.getSimpleName();
	private OnFinish<Result> onFinish;
	private FetchGenericMapper<Result> mapper;

	/**
	 * Fetches the first url and notifies the {@link OnFinish} listener.
	 *
	 * @param onFinish The function to be called when the result is ready.
	 * @param mapper   The function to map from the url and downloaded page to the desired type.
	 */
	public FetchUrl(
			OnFinish<Result> onFinish,
			FetchGenericMapper<Result> mapper
	) {
		this.onFinish = onFinish;
		this.mapper = mapper;
	}

	@Override
	protected Result doInBackground(String... params) {
		if(params.length >= 1) {
			String url = params[0];
			StringBuilder toAdd = getItem(url);
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
	 * Fetches a url and returns what was downloaded or null
	 *
	 * @param url to query
	 */
	private StringBuilder getItem(String url) {
		StringBuilder toReturn = null;
		try {
			toReturn = NetworkUtils.fetchURL(url);
		} catch (IOException e) { //network exception
			Log.w(TAG, "Could not connect to: " + url, e);
		}

		return toReturn;
	}
}
