package com.jtmcn.archwiki.viewer;

import android.content.Context;
import android.database.MatrixCursor;
import android.support.v4.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import com.jtmcn.archwiki.viewer.data.SearchResult;

import java.util.List;

/**
 * Created by kevin on 4/22/2017.
 */

public class SearchResultsAdapter {
	private static final String[] columnNames = {"_id", "title"};
	private static final String[] from = {"title"};
	private static final int[] to = new int[]{android.R.id.text1};

	/**
	 * https://stackoverflow.com/questions/11628172/converting-an-arrayadapter-to-cursoradapter-for-use-in-a-searchview/11628527#11628527
	 *
	 * @param results
	 * @return
	 */
	public static SimpleCursorAdapter getCursorAdapter(Context context, List<SearchResult> results) {
		int id = 0;
		MatrixCursor cursor = new MatrixCursor(columnNames);
		for (SearchResult item : results) {
			String[] temp = new String[2];
			temp[0] = String.valueOf(id);
			temp[1] = item.getPageName();

			cursor.addRow(temp);
			id++;
		}
		return new SimpleCursorAdapter(
				context,
				android.R.layout.simple_list_item_1,
				cursor,
				from,
				to,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
		);
	}
}
