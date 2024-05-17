package com.christoph.webbrowserinjava;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class URLAutoCompleteTest {

    @Test
    void testSuggestCompletionWithHttpPrefix() {
        String partialUrl = "https://google";
        String expectedUrl = "https://www.google.com";
        String actualUrl = URLAutoComplete.suggestCompletion(partialUrl);
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void testSuggestCompletionWithoutHttpPrefix() {
        String partialUrl = "google";
        String expectedUrl = "https://www.google.com";
        String actualUrl = URLAutoComplete.suggestCompletion(partialUrl);
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void testSuggestCompletionWithWwwPrefix() {
        String partialUrl = "www.google";
        String expectedUrl = "https://www.google.com";
        String actualUrl = URLAutoComplete.suggestCompletion(partialUrl);
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void testSuggestCompletionWithDomain() {
        String partialUrl = "google.com";
        String expectedUrl = "https://www.google.com";
        String actualUrl = URLAutoComplete.suggestCompletion(partialUrl);
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void testSuggestCompletionWithFullUrl() {
        String partialUrl = "https://www.google.com";
        String expectedUrl = "https://www.google.com";
        String actualUrl = URLAutoComplete.suggestCompletion(partialUrl);
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void testSuggestCompletionWithPartialDomain() {
        String partialUrl = "google";
        String expectedUrl = "https://www.google.com";
        String actualUrl = URLAutoComplete.suggestCompletion(partialUrl);
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void testSuggestCompletionWithDifferentExtension() {
        String partialUrl = "google";
        String expectedUrl = "https://www.google.com";
        String actualUrl = URLAutoComplete.suggestCompletion(partialUrl);
        assertEquals(expectedUrl, actualUrl);

        partialUrl = "google.org";
        expectedUrl = "https://www.google.org";
        actualUrl = URLAutoComplete.suggestCompletion(partialUrl);
        assertEquals(expectedUrl, actualUrl);
    }
}
