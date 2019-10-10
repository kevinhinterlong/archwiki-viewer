
package com.jtmcn.archwiki.viewer.utils

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException



private val client = OkHttpClient.Builder().build()
private val builder = Request.Builder()

/**
 * Fetches a url with optional caching.
 *
 * @param url url to be fetched.
 * @param cb callback to handle result
 */
fun fetchURL(url: String): Call = client.newCall(builder.url(url).build())
