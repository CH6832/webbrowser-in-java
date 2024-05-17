package com.christoph.webbrowserinjava;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BrowserControllerTest {

    @Test
    void shouldSuggestCompletion() {
        // Given
        String partialUrl = "google";

        // When
        String completedUrl = URLAutoComplete.suggestCompletion(partialUrl);

        // Then
        assertEquals("https://www.google.com", completedUrl);
    }

    @Test
    void shouldNotSuggestCompletionForCompleteUrl() {
        // Given
        String partialUrl = "https://www.google.com";

        // When
        String completedUrl = URLAutoComplete.suggestCompletion(partialUrl);

        // Then
        assertEquals(partialUrl, completedUrl);
    }

    @Test
    void shouldAddWwwPrefix() {
        // Given
        String url = "https://google.com";

        // When
        String urlWithWww = URLAutoComplete.addWwwPrefix(url);

        // Then
        assertEquals("https://www.google.com", urlWithWww);
    }

    @Test
    void shouldNotAddWwwPrefixIfAlreadyPresent() {
        // Given
        String url = "https://www.google.com";

        // When
        String urlWithWww = URLAutoComplete.addWwwPrefix(url);

        // Then
        assertEquals("https://www.google.com", urlWithWww);
    }

    @Test
    void shouldOpenInExternalBrowser() {
        // Given
        BrowserController browserController = new BrowserController();
        String url = "https://www.google.com";

        // When
        boolean opened = browserController.openInExternalBrowser(url);

        // Then
        assertTrue(opened);
    }

    @Test
    void shouldNotOpenInExternalBrowser() {
        // Given
        BrowserController browserController = new BrowserController();
        String url = "example.com";

        // When
        boolean opened = browserController.openInExternalBrowser(url);

        // Then
        assertFalse(opened);
    }
}
