package com.jtmcn.archwiki.viewer.data

import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.jtmcn.archwiki.viewer.ARCHWIKI_BASE

/**
 * Builds a string url to fetch search results.
 *
 * @param query the text to search for.
 * @param limit the maximum number of results to retrieve.
 * @return a url to fetch.
 */
fun getSearchQuery(query: String, limit: Int = 10): String {
    return "${ARCHWIKI_BASE}api.php?" +
            "action=opensearch&format=json&formatversion=2&namespace=0&suggest=true" +
            "&search=$query" +
            "&limit=$limit"
}

/**
 * Builds a [List] from the result of fetching with [getSearchQuery].
 *
 * @param jsonResult the string returned from the query.
 * @return a parsed list of the results.
 */
fun parseSearchResults(jsonResult: String): List<SearchResult> {
    val jsonRoot = JsonParser().parse(jsonResult)
    if (!jsonRoot.isJsonArray || jsonRoot.asJsonArray.size() != 4) return listOf()

    val jsonArray = jsonRoot.asJsonArray

    val listOfPageTitles = getJsonArrayAsStringArray(jsonArray.get(1).asJsonArray)
    val listOfPageUrls = getJsonArrayAsStringArray(jsonArray.get(3).asJsonArray)

    return listOfPageTitles.zip(listOfPageUrls).map { SearchResult(it.first, it.second) }
}

/**
 * Convert a [JsonArray] into an array of strings.
 *
 * @param jsonArray the array to be parsed.
 * @return the string array which was parsed.
 */
private fun getJsonArrayAsStringArray(jsonArray: JsonArray): List<String> {
    return jsonArray.map { it.asString }.filterNotNull()
}