package com.christoph.webbrowserinjava;

import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class BrowserController {

    private static final List<String> EXTERNAL_BROWSER_URLS = List.of("chatgpt", "gemini", "llama");

    @FXML
    private TextField searchField;

    @FXML
    public TabPane tabPane;

    @FXML
    private TextField addressBar;

    private void handleKeyPressed(KeyEvent event) {
        // Check if Ctrl + F is pressed
        if (event.isControlDown() && event.getCode() == KeyCode.F) {
            // Show the search field
            searchField.setVisible(true);
            searchField.requestFocus();
        }
    }

    @FXML
    private void searchInPage() {
        WebView webView = (WebView) tabPane.getSelectionModel().getSelectedItem().getContent();
        WebEngine webEngine = webView.getEngine();
        String searchText = searchField.getText();
        webEngine.executeScript("window.find('" + searchText + "')");
    }

    private boolean shouldOpenInExternalBrowser(String url) {
        return EXTERNAL_BROWSER_URLS.stream().anyMatch(url::contains);
    }

    boolean openInExternalBrowser(String url) {
        String osName = System.getProperty("os.name").toLowerCase();
        String[] command;

        if (osName.contains("win")) {
            command = new String[]{"C:\\Program Files\\Mozilla Firefox\\firefox.exe", "-new-tab", url};
        } else if (osName.contains("nix") || osName.contains("nux")) {
            command = new String[]{"firefox", "-new-tab", url};
        } else if (osName.contains("mac")) {
            command = new String[]{"open", "-a", "Safari", url};
        } else {
            showErrorDialog("Unsupported OS", "This feature is not supported on your operating system.");
            return false;
        }

        try {
            new ProcessBuilder(command).start();
        } catch (IOException e) {
            showErrorDialog("Error", "Failed to open the URL in the external browser.");
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    private void addNewTab(String url) {
        String completeUrl = URLAutoComplete.suggestCompletion(url);

        if (shouldOpenInExternalBrowser(completeUrl)) {
            openInExternalBrowser(completeUrl);
            return;
        }

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.FAILED) {
                showErrorDialog("Failed to load page", "The URL you entered could not be loaded.");
            }
        });

        String websiteName = extractWebsiteName(completeUrl);
        Tab newTab = new Tab(websiteName);
        newTab.setContent(webView);
        tabPane.getTabs().add(newTab);
        webEngine.load(completeUrl);
    }

    @FXML
    private void openInFirefox() {
        String url = addressBar.getText();
        String completeUrl = URLAutoComplete.suggestCompletion(url);
        openInExternalBrowser(completeUrl);
    }

    @FXML
    private void initialize() {

        // Add key pressed event handler to the root node
        tabPane.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);

        // Add listener to close tabs with Ctrl + W
        tabPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.isControlDown() && event.getCode() == KeyCode.W) {
                    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
                    if (selectedTab != null) {
                        tabPane.getTabs().remove(selectedTab);
                    }
                }
            }
        });

        // Add an initial tab when the application starts
        addNewTab("https://www.google.com");

        // Add listener to the address bar to handle URL changes
        addressBar.setOnAction(event -> openUrlInNewTab(addressBar.getText()));

    }

    @FXML
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void addNewTabWithDefaultUrl() {
        addNewTab("https://www.google.com"); // or any other default URL you prefer
    }

    @FXML
    private String extractWebsiteName(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            if (domain != null) {
                return domain.startsWith("www.") ? domain.substring(4) : domain;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "New Tab";
    }

    @FXML
    private void openUrlInNewTab(String url) {
        // Use auto-completion to suggest completing the URL
        String completedUrl = URLAutoComplete.suggestCompletion(url);

        // Create a new WebView and WebEngine
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Add a listener to handle page load errors
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.FAILED) {
                showErrorDialog("Failed to load page", "The URL you entered could not be loaded.");
            }
        });

        // Create a new tab and set its content to the WebView
        Tab newTab = new Tab();
        newTab.setContent(webView);

        // Set the title of the tab to the website name
        String websiteName = extractWebsiteName(completedUrl);
        newTab.setText(websiteName);

        // Add the new tab to the tab pane
        tabPane.getTabs().add(newTab);

        // Load the completed URL into the WebView
        webEngine.load(completedUrl);
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
