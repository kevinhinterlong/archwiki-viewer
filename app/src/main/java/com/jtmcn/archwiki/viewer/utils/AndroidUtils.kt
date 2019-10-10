package com.jtmcn.archwiki.viewer.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Creates an intent to open a link.
 *
 * @param url     The url to be opened.
 * @param context The context needed to start the intent.
 */
fun openLink(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
