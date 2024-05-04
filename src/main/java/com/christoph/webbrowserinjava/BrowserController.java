package com.christoph.webbrowserinjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class BrowserController {

    @FXML
    public TabPane tabPane;

    @FXML
    private TextField addressBar;

    @FXML
    private void initialize() {
        // Add an initial tab when the application starts
        addNewTab("https://www.google.com");

        // Add listener to the address bar to handle URL changes
        addressBar.setOnAction(event -> openUrlInNewTab(addressBar.getText()));
    }

    @FXML
    private void addNewTab(String url) {
        // Create a new tab with the title set to the website name
        Tab newTab = new Tab();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Parse the URL to extract the website name
        String websiteName = extractWebsiteName(url);
        newTab.setText(websiteName);

        // Add the WebView to the tab content
        newTab.setContent(webView);

        // Add the new tab to the tab pane
        tabPane.getTabs().add(newTab);

        // Load the specified URL into the WebView
        webEngine.load(url);
    }

    @FXML
    private void addNewTabWithDefaultUrl() {
        addNewTab("https://www.example.com"); // or any other default URL you prefer
    }

    private String extractWebsiteName(String url) {
        // Remove protocol (http:// or https://) from the URL
        String domain = url.replaceAll("^(http://|https://)", "");

        // Get the hostname from the URL

        // Return the hostname
        return URI.create(domain).getHost();
    }

    @FXML
    private void openUrlInNewTab(String url) {
        // Create a new tab
        Tab newTab = new Tab("New Tab");
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Add the WebView to the tab content
        newTab.setContent(webView);

        // Add the new tab to the tab pane
        tabPane.getTabs().add(newTab);

        // Load the specified URL into the WebView
        webEngine.load(url);
    }

    @FXML
    private void navigateBack() {
        // Get the selected tab
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        // Get the WebEngine associated with the WebView in the selected tab
        WebView webView = (WebView) selectedTab.getContent();
        WebEngine webEngine = webView.getEngine();

        // Navigate back
        if (webEngine.getHistory().getCurrentIndex() > 0) {
            webEngine.getHistory().go(-1);
        }
    }

    @FXML
    private void navigateForward() {
        // Get the selected tab
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        // Get the WebEngine associated with the WebView in the selected tab
        WebView webView = (WebView) selectedTab.getContent();
        WebEngine webEngine = webView.getEngine();

        // Navigate forward
        if (webEngine.getHistory().getCurrentIndex() < webEngine.getHistory().getEntries().size() - 1) {
            webEngine.getHistory().go(1);
        }
    }

    @FXML
    private void refreshPage() {
        // Get the selected tab
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        // Get the WebEngine associated with the WebView in the selected tab
        WebView webView = (WebView) selectedTab.getContent();
        WebEngine webEngine = webView.getEngine();

        // Refresh the page
        webEngine.reload();
    }
}
