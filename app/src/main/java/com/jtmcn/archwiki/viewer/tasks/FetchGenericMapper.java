package com.jtmcn.archwiki.viewer.tasks;

/**
 * Created by kevin on 4/22/2017.
 */

public interface FetchGenericMapper<T,R> {
	R mapTo(String url, T t);
}
