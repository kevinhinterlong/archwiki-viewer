package com.jtmcn.archwiki.viewer.tasks

import android.os.AsyncTask
import com.jtmcn.archwiki.viewer.utils.fetchURL
import timber.log.Timber
import java.io.IOException

/**
 * Fetches a url, [mapper] maps it to a [Result], and returns it.
 * */
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
            Timber.w(e,"Could not connect to $url")
            toReturn = StringBuilder()
        }

        return toReturn
    }
}
