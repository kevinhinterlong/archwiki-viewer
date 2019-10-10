package com.jtmcn.archwiki.viewer.tasks

import android.os.AsyncTask
import android.util.Log
import com.jtmcn.archwiki.viewer.utils.fetchURL

import java.io.IOException

/**
 * Fetches a url, maps it to a [Result], and returns it.
 *
 * @param <Result> The type which the fetched url's text will be mapped to.
</Result> */
class FetchUrl<Result>(
    private val onFinish: (Result) -> Unit,
    private val mapper: (url: String, html: StringBuilder) -> Result
) : AsyncTask<String, Void, Result>() {

    override fun doInBackground(vararg params: String): Result? {
        if (params.isNotEmpty()) {
            val url = params[0]
            val toAdd = getItem(url)
            return mapper(url, toAdd)
        }
        return null
    }

    override fun onPostExecute(values: Result) {
        super.onPostExecute(values)
        onFinish(values)
    }

    /**
     * Fetches a url and returns what was downloaded or null
     *
     * @param url to query
     */
    private fun getItem(url: String): StringBuilder {
        var toReturn: StringBuilder
        try {
            val response = fetchURL(url).execute().body()?.string() ?: ""
            toReturn = StringBuilder(response)
        } catch (e: IOException) { //network exception
            Log.w(TAG, "Could not connect to: $url", e)
            toReturn = StringBuilder()
        }

        return toReturn
    }

    companion object {
        private val TAG = FetchUrl::class.java.simpleName
    }
}
