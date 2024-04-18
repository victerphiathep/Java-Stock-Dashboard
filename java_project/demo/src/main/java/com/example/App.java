package com.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

        @Override
    public void start(Stage stage) {
        // Real-time date and time label
        Label dateTimeLabel = new Label();
        dateTimeLabel.setText(getFormattedDateTime());
        dateTimeLabel.setFont(Font.font("Georgia", 24));

        // AnimationTimer for updating the label with formatted date
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                dateTimeLabel.setText(getFormattedDateTime());
            }
        }.start();

        // Static label as a header
        Label headerLabel = new Label("Stock Market Dashboard");
        headerLabel.setFont(Font.font("Georgia", 32));


        // Search bar (TextField)
        TextField searchBar = new TextField();
        searchBar.setPromptText("Enter Stock Symbol..."); // Placeholder text
        searchBar.setPrefWidth(200);



        // VBox layout with spacing between nodes
        VBox layout = new VBox(10); // Spacing of 10 pixels
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(headerLabel, dateTimeLabel, searchBar);



        Scene scene = new Scene(layout, 640, 480);

        stage.setScene(scene);
        stage.show();
    }

    private String getFormattedDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("MMMM d'" + getDayOfMonthSuffix(now.getDayOfMonth()) + "' yyyy, h:mm:ss a"));
    }

    private String getDayOfMonthSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}