package com.christoph.webbrowserinjava;

import java.util.Arrays;
import java.util.List;

public class URLAutoComplete {

    // List of common domain extensions
    private static final List<String> DOMAIN_EXTENSIONS = Arrays.asList(
            ".com", ".org", ".net", ".edu", ".gov", ".co", ".io", ".info", ".biz", ".tv", ".me"
            // Add more extensions as needed
    );

    // Method to suggest completing the URL
    public static String suggestCompletion(String partialUrl) {
        // Check if the partial URL starts with "https://"
        if (!partialUrl.startsWith("https://")) {
            // Suggest completing with "https://" prefix
            partialUrl = "https://" + partialUrl;
        }

        // Check if the partial URL contains "www."
        if (!partialUrl.contains("www.")) {
            // Suggest completing with "www." prefix
            partialUrl = addWwwPrefix(partialUrl);
        }

        // Check if the partial URL ends with a domain extension
        boolean hasExtension = false;
        for (String extension : DOMAIN_EXTENSIONS) {
            if (partialUrl.endsWith(extension)) {
                hasExtension = true;
                break;
            }
        }

        // If not, suggest completing with the first domain extension
        if (!hasExtension && !DOMAIN_EXTENSIONS.isEmpty()) {
            String firstExtension = DOMAIN_EXTENSIONS.get(0);
            if (!partialUrl.endsWith(firstExtension)) {
                partialUrl += firstExtension;
            }
        }

        // Return the completed URL
        return partialUrl;
    }

    // Method to add "www." prefix to the URL
    static String addWwwPrefix(String url) {
        // Split the URL into protocol and the rest of the URL
        int protocolEndIndex = url.indexOf("://") + 3;
        String protocol = url.substring(0, protocolEndIndex);
        String restOfUrl = url.substring(protocolEndIndex);

        // Check if "www." is already present
        if (!restOfUrl.startsWith("www.")) {
            restOfUrl = "www." + restOfUrl;
        }

        return protocol + restOfUrl;
    }
}

