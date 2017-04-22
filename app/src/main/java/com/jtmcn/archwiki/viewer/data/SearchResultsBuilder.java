package com.jtmcn.archwiki.viewer.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.jtmcn.archwiki.viewer.Constants.ARCHWIKI_BASE;

/**
 * Created by kevin on 4/4/2017.
 */

public class SearchResultsBuilder {
	public static final String SEARCH_URL = ARCHWIKI_BASE + "api.php?action=opensearch" +
			"&format=json&formatversion=2&namespace=0&limit={0}" +
			"&suggest=true&search={1}";
	private static final int DEFAULT_LIMIT = 10;

	private SearchResultsBuilder() {
	}

	public static String getSearchQuery(String query) {
		return getSearchQuery(query,DEFAULT_LIMIT);
	}

	public static String getSearchQuery(String query, int limit) {
		return MessageFormat.format(SEARCH_URL, String.valueOf(limit), query);
	}

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

	private static String[] getJsonArrayAsStringArray(JsonArray jsonArray) {
		String[] s2 = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			s2[i] = jsonArray.get(i).getAsString();
		}
		return s2;
	}
}
