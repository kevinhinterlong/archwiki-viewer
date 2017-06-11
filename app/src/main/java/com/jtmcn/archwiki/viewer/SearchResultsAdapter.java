package com.jtmcn.archwiki.viewer;

import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;

import com.jtmcn.archwiki.viewer.data.SearchResult;

import java.util.List;

/**
 * Helper for creating a {@link SimpleCursorAdapter} which will
 * list the search results for a {@link android.widget.SearchView}
 */
public class SearchResultsAdapter {
	private static final String[] columnNames = {BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1};
	private static final String[] from = {SearchManager.SUGGEST_COLUMN_TEXT_1};
	private static final int[] to = new int[]{R.id.url};

	/**
	 * Creates a cursor adapter given a {@link List<SearchResult>}.
	 * https://stackoverflow.com/questions/11628172/converting-an-arrayadapter-to-cursoradapter-for-use-in-a-searchview/11628527#11628527
	 *
	 * @param results the results to be placed in the adapter.
	 * @return the adapter.
	 */
	public static CursorAdapter getCursorAdapter(Context context, List<SearchResult> results) {
		int id = 0;
		MatrixCursor cursor = new MatrixCursor(columnNames);
		for (SearchResult item : results) {
			String[] temp = new String[2];
			temp[0] = String.valueOf(id); // "_id"
			temp[1] = item.getPageName(); // "title"

			cursor.addRow(temp);
			id++;
		}

		return new SimpleCursorAdapter(
				context,
				R.layout.search_suggestions_list_item,
				cursor,
				from,
				to,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
		);
	}
}
