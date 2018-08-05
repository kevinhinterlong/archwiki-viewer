package com.jtmcn.archwiki.viewer.tasks

import android.os.AsyncTask
import android.util.Log
import com.jtmcn.archwiki.viewer.utils.NetworkUtils
import java.io.IOException


typealias OnFinish<Result> = (Result) -> Unit
typealias FetchUrlMapper<Result> = (url: String, sb: StringBuilder) -> Result

/**
 * Fetches a url, maps it to a [Result], and returns it.
 *
 * @param <Result> The type which the fetched url's text will be mapped to.
</Result> */
class FetchUrl<Result>
/**
 * Fetches the first url and notifies the [OnFinish] listener.
 *
 * @param onFinish The function to be called when the result is ready.
 * @param mapper   The function to map from the url and downloaded page to the desired type.
 * @param caching  Whether or not to use cached results
 */
@JvmOverloads constructor(private val onFinish: OnFinish<Result>?, private val mapper: FetchUrlMapper<Result>, private val caching: Boolean = true) : AsyncTask<String, Void, Result>() {
    private val TAG = FetchUrl::class.java.simpleName

    override fun doInBackground(vararg params: String): Result? {
        if (params.isNotEmpty()) {
            val url = params[0]
            val toAdd = getItem(url)
            return mapper.invoke(url, toAdd)
        }
        return null
    }

    override fun onPostExecute(values: Result) {
        super.onPostExecute(values)
        onFinish?.invoke(values)
    }

    /**
     * Fetches a url and returns what was downloaded or null
     *
     * @param url to query
     */
    private fun getItem(url: String): StringBuilder {
        var toReturn: StringBuilder
        try {
            toReturn = NetworkUtils.fetchURL(url, caching)
        } catch (e: IOException) { //network exception
            Log.w(TAG, "Could not connect to: $url", e)
            toReturn = StringBuilder()
        }

        return toReturn
    }

}
