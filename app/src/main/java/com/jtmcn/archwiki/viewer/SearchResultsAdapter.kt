package com.jtmcn.archwiki.viewer

import android.app.SearchManager
import android.content.Context
import android.database.MatrixCursor
import android.provider.BaseColumns
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter

import com.jtmcn.archwiki.viewer.data.SearchResult

/**
 * Helper for creating a [SimpleCursorAdapter] which will
 * list the search results for a [android.widget.SearchView]
 */
object SearchResultsAdapter {
    private val columnNames = arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1)
    private val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
    private val to = intArrayOf(R.id.url)

    /**
     * Creates a cursor adapter given a [<].
     * https://stackoverflow.com/questions/11628172/converting-an-arrayadapter-to-cursoradapter-for-use-in-a-searchview/11628527#11628527
     *
     * @param results the results to be placed in the adapter.
     * @return the adapter.
     */
    fun getCursorAdapter(context: Context, results: List<SearchResult>): CursorAdapter {
        var id = 0
        val cursor = MatrixCursor(columnNames)
        for ((pageName) in results) {
            val temp = arrayOfNulls<String>(2)
            temp[0] = id.toString() // "_id"
            temp[1] = pageName // "title"

            cursor.addRow(temp)
            id++
        }

        return SimpleCursorAdapter(
                context,
                R.layout.search_suggestions_list_item,
                cursor,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
    }
}
