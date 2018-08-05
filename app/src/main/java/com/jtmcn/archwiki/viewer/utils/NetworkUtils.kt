package com.jtmcn.archwiki.viewer.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * Utility class for performing basic network tasks.
 */
object NetworkUtils {
    private val downloadCache = HashMap<URL, StringBuilder>()

    /**
     * Fetches a url with optional caching.
     *
     * @param stringUrl url to be fetched.
     * @param useCache  whether or not it should return a cached value.
     * @return the string that was fetched.
     * @throws IOException on network failure.
     */
    @Throws(IOException::class)
    @JvmOverloads
    @JvmStatic
    fun fetchURL(stringUrl: String, useCache: Boolean = true): StringBuilder {
        val sb = StringBuilder("")
        val url = URL(stringUrl)
        if (useCache && downloadCache.containsKey(url)) {
            return StringBuilder(downloadCache[url])
        }
        val urlConnection = url.openConnection() as HttpURLConnection

        urlConnection.readTimeout = 10000 // milliseconds
        urlConnection.connectTimeout = 15000 // milliseconds
        urlConnection.requestMethod = "GET"

        val inStream = BufferedReader(InputStreamReader(
                urlConnection.inputStream), 8)// buffer 8k

        var line: String? = ""
        val lineSeparator = System.getProperty("line.separator")

        while (true) {
            line = inStream.readLine()
            if (line == null) {
                break;
            }
            sb.append(line).append(lineSeparator)
        }

        urlConnection.disconnect()
        inStream.close()

        downloadCache[url] = StringBuilder(sb)
        return sb
    }
}