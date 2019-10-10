package com.jtmcn.archwiki.viewer.data;

import com.jtmcn.archwiki.viewer.Constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WikiPageBuilderTest {
	@Test
	public void getPageTitle() throws Exception {
		String fakeTitle = "fake title..1!@#!@#!";
		StringBuilder wrapped = new StringBuilder(WikiPageBuilder.INSTANCE.getHTML_TITLE_OPEN())
				.append(fakeTitle)
				.append(WikiPageBuilder.INSTANCE.getHTML_TITLE_CLOSE());
		assertEquals(fakeTitle, WikiPageBuilder.INSTANCE.getPageTitle(wrapped));
	}

	@Test
	public void getEmptyTitle() throws Exception {
		assertEquals("No title found", WikiPageBuilder.INSTANCE.getPageTitle(new StringBuilder()));
	}

	@Test
	public void injectLocalCSS() throws Exception {
		StringBuilder head = new StringBuilder(WikiPageBuilder.INSTANCE.getHTML_HEAD_OPEN())
				.append(WikiPageBuilder.INSTANCE.getHTML_HEAD_CLOSE());
		boolean passed = WikiPageBuilder.INSTANCE.injectLocalCSS(head, Constants.INSTANCE.getLOCAL_CSS());
		assertTrue(passed);
		assertEquals("<head><link rel='stylesheet' href='file:///android_asset/style.css' /><meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=no' /></head>",
				head.toString());
	}

	@Test
	public void injectLocalCSSFail() throws Exception {
		String fakeHead = "<head> <head>";
		StringBuilder head = new StringBuilder(fakeHead);
		boolean passed = WikiPageBuilder.INSTANCE.injectLocalCSS(head, Constants.INSTANCE.getLOCAL_CSS());
		assertFalse(passed);
		assertEquals(fakeHead, head.toString());
	}

}