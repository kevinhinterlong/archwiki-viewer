package com.jtmcn.archwiki.viewer.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import static com.jtmcn.archwiki.viewer.Constants.ARCHWIKI_BASE;

/**
 * Provides a simple interface to make queries against
 * and parse data from the arch wiki for searches.
 */
public class SearchResultsBuilder {
	public static final String SEARCH_URL = ARCHWIKI_BASE + "api.php?action=opensearch" +
			"&format=json&formatversion=2&namespace=0&limit=%d" +
			"&suggest=true&search=%s";
	private static final int DEFAULT_LIMIT = 10;

	private SearchResultsBuilder() {

	}

	/**
	 * Builds a string url to fetch search results.
	 *
	 * @param query the text to search for.
	 * @return a url to fetch.
	 */
	public static String getSearchQuery(String query) {
		return getSearchQuery(query, DEFAULT_LIMIT);
	}

	/**
	 * Builds a string url to fetch search results.
	 *
	 * @param query the text to search for.
	 * @param limit the maximum number of results to retrieve.
	 * @return a url to fetch.
	 */
	public static String getSearchQuery(String query, int limit) {
		return String.format(SEARCH_URL, limit, query);
	}

	/**
	 * Builds a {@link List<SearchResult>} from the result of fetching with {@link #getSearchQuery(String, int)}.
	 *
	 * @param jsonResult the string returned from the query.
	 * @return a parsed list of the results.
	 */
	public static List<SearchResult> parseSearchResults(String jsonResult) {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonRoot = jsonParser.parse(jsonResult);
		List<SearchResult> toReturn = new ArrayList<>();
		if (jsonRoot.isJsonArray()) {
			JsonArray jsonArray = jsonRoot.getAsJsonArray();
			if (jsonArray.size() == 4) {
				String[] listOfPageTitles = getJsonArrayAsStringArray(jsonArray.get(1).getAsJsonArray());
				String[] listOfPageUrls = getJsonArrayAsStringArray(jsonArray.get(3).getAsJsonArray());
				for (int i = 0; i < listOfPageTitles.length; i++) {
					toReturn.add(new SearchResult(listOfPageTitles[i], listOfPageUrls[i]));
				}
			}
		}
		return toReturn;
	}

	/**
	 * Convert a {@link JsonArray} into an array of strings.
	 *
	 * @param jsonArray the array to be parsed.
	 * @return the string array which was parsed.
	 */
	private static String[] getJsonArrayAsStringArray(JsonArray jsonArray) {
		String[] s2 = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			s2[i] = jsonArray.get(i).getAsString();
		}
		return s2;
	}
}
