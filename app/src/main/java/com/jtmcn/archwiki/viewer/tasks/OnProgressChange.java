package com.jtmcn.archwiki.viewer.tasks;

import java.util.List;

/**
 * Created by kevin on 4/22/2017.
 */

public interface OnProgressChange<E> {
	void onAdd(E e);
	void onFinish(List<E> results);
	void onProgressUpdate(int value);
}
