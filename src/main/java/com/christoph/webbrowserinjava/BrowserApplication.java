package com.christoph.webbrowserinjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class BrowserApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/christoph/webbrowserinjava/browser-view.fxml")));
        primaryStage.setTitle("JavaFX Browser");
        Scene scene = new Scene(root, 1500, 900);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/christoph/webbrowserinjava/browser-view.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}