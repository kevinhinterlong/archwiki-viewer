package com.jtmcn.archwiki.viewer.data

import com.jtmcn.archwiki.viewer.LOCAL_CSS
import org.junit.Assert.*

import org.junit.Test

class WikiPageBuilderTest {
    @Test
    @Throws(Exception::class)
    fun getPageTitle() {
        val fakeTitle = "fake title..1!@#!@#!"
        assertEquals(fakeTitle, getPageTitle(wrappedTitle(fakeTitle)))

        assertEquals("", getPageTitle(wrappedTitle("")))
    }

    private fun wrappedTitle(fakeTitle: String) =
            StringBuilder("${HTML_TITLE_OPEN}${fakeTitle}${HTML_TITLE_CLOSE}")

    @Test
    @Throws(Exception::class)
    fun getEmptyTitle() {
        assertNull(getPageTitle(StringBuilder()))
    }

    @Test
    @Throws(Exception::class)
    fun injectLocalCSS() {
        val head = StringBuilder("${HTML_HEAD_OPEN}${HTML_HEAD_CLOSE}")
        val passed = injectLocalCSS(head, LOCAL_CSS)
        assertTrue(passed)
        assertEquals("<head><link rel='stylesheet' href='file:///android_asset/style.css' /><meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=no' /></head>",
                head.toString())
    }

    @Test
    @Throws(Exception::class)
    fun injectLocalCSSFail() {
        val fakeHead = "<head> <head>"
        val head = StringBuilder(fakeHead)
        val passed = injectLocalCSS(head, LOCAL_CSS)
        assertFalse(passed)
        assertEquals(fakeHead, head.toString())
    }

}