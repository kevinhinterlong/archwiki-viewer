package com.jtmcn.archwiki.viewer.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.jtmcn.archwiki.viewer.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FetchGeneric<Result> extends AsyncTask<String, Integer, List<Result>> {
	public static final String TAG = FetchGeneric.class.getSimpleName();
	private static ExecutorService executorService;
	private OnProgressChange<Result> onProgressChange;
	private FetchGenericMapper<StringBuilder, Result> mapper;
	private boolean blocking;

	/**
	 * Fetches a list of urls and publishes progress on the {@link OnProgressChange} listener.
	 *
	 * @param onProgressChange  The listener to be called when progress is ready.
	 * @param mapper  The function to map from the url and downloaded page to the desired type.
	 * @param blocking  Whether or not it should force all connections to be finished.
	 */
	public FetchGeneric(
			OnProgressChange<Result> onProgressChange,
			FetchGenericMapper<StringBuilder, Result> mapper,
			boolean blocking
	) {
		this.onProgressChange = onProgressChange;
		this.mapper = mapper;
		this.blocking = blocking;
		if (executorService == null) {
			int availableProcessors = Runtime.getRuntime().availableProcessors();
			Log.d(TAG, "Running on " + availableProcessors + " processors");
			executorService = Executors.newFixedThreadPool(Math.max(availableProcessors, 1));
		}
	}

	@Override
	protected List<Result> doInBackground(String... params) {
		List<Result> toReturn = new ArrayList<>();
		getAllItems(params, toReturn);
		return toReturn;
	}

	@Override
	protected void onPostExecute(List<Result> values) {
		super.onPostExecute(values);
		if (onProgressChange != null) {
			onProgressChange.onFinish(values);
		}
	}

	@Override
	protected final void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (onProgressChange != null) {
			onProgressChange.onProgressUpdate(values[0]);
		}
	}

	private void getAllItems(String[] urlsToFetch, final Collection<Result> pagesToReturn) {
		try {
			List<Future> futures = new ArrayList<>();
			for (final String url : urlsToFetch) {
				Future future = executorService.submit(new Runnable() {
					@Override
					public void run() {
						StringBuilder toAdd = getItem(url);
						if (toAdd != null) {
							Result r = mapper.mapTo(url, toAdd);
							pagesToReturn.add(r);
							if (onProgressChange != null) {
								onProgressChange.onAdd(r);
							}
							publishProgress(pagesToReturn.size());
						}
					}
				});
				futures.add(future);
			}
			if (blocking) {
				finishAll(futures);
			}
		} finally {
			Log.d(TAG, "Finished setting up all all tasks");
		}
	}

	private void finishAll(List<Future> futures) {
		for (Future<?> f : futures) {
			try {
				f.get();
			} catch (InterruptedException | ExecutionException e) {
				Log.e(TAG, "Failed to finish download task - executionException. ", e);
			}
		}
		Log.d(TAG, "All tasks have been finished");
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
			Log.d(TAG, "Could not connect to: " + url, e);
			toReturn = new StringBuilder("Could not connect");
		}

		return toReturn;
	}
}
