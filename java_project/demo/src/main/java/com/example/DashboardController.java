package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.concurrent.Task;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.services.StockApiService;
import com.example.services.StockData;
import com.example.utils.DateTimeUtils;
import java.util.*;

public class DashboardController {
    @FXML private Label dateTimeLabel;
    @FXML private TextField searchBar;
    @FXML private Label stockSymbolLabel; // Stock Ticker to display
    @FXML private Button searchButton;
    @FXML private Button quit_button;
    @FXML private Label api_time;
    @FXML private Label last_refreshed;
    @FXML private Label percent_change;
    

    @FXML private Label pane_1_date, pane_1_open, pane_1_high, pane_1_low, pane_1_close, pane_1_volume;
    @FXML private Label pane_2_date, pane_2_open, pane_2_high, pane_2_low, pane_2_close, pane_2_volume;
    @FXML private Label pane_3_date, pane_3_open, pane_3_high, pane_3_low, pane_3_close, pane_3_volume;
    @FXML private Label pane_4_date, pane_4_open, pane_4_high, pane_4_low, pane_4_close, pane_4_volume;
    @FXML private Label pane_5_date, pane_5_open, pane_5_high, pane_5_low, pane_5_close, pane_5_volume;
    @FXML private Label pane_6_date, pane_6_open, pane_6_high, pane_6_low, pane_6_close, pane_6_volume;

    private StockApiService stockApiService = new StockApiService();

    @FXML
    public void initialize() {
        // DateTime Handler
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                dateTimeLabel.setText(DateTimeUtils.getFormattedDateTime());
            }
        };
        timer.start();

        // Search Button Event Handler
        searchButton.setOnAction(event -> fetchStockData());
        quit_button.setOnAction(event -> Platform.exit());
    }

    private void fetchStockData() {
        String symbol = searchBar.getText();
        if (symbol.isEmpty()) {
            Platform.runLater(() -> {
                stockSymbolLabel.setText("Enter a symbol!");
                last_refreshed.setText("");
            });
            return;
        }
    
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Map<String, StockData> stockData = stockApiService.getStockData(symbol, "DAILY");
                List<String> lastSixDays = new ArrayList<>(stockData.keySet());
                Collections.sort(lastSixDays, Collections.reverseOrder());
    
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // Update UI components on the JavaFX application thread
                Platform.runLater(() -> {
                    if (lastSixDays.size() >= 6) {

                        // Get Percent Change of Day 1 closing to Day 2
                        StockData day1Data = stockData.get(lastSixDays.get(0));
                        StockData day2Data = stockData.get(lastSixDays.get(1));

                        updatePaneData(pane_1_date, pane_1_open, pane_1_high, pane_1_low, pane_1_close, pane_1_volume, day1Data);
                        updatePaneData(pane_2_date, pane_2_open, pane_2_high, pane_2_low, pane_2_close, pane_2_volume, day2Data);

                        double closeDay1 = day1Data.getClose();
                        double closeDay2 = day2Data.getClose();
                        double percentChange = ((closeDay1 - closeDay2) / closeDay2) * 100;

                        percent_change.setText(String.format("Percent Change: %.2f%%", percentChange));

                        // Update Panels with API data
                        updatePaneData(pane_1_date, pane_1_open, pane_1_high, pane_1_low, pane_1_close, pane_1_volume, stockData.get(lastSixDays.get(0)));
                        updatePaneData(pane_2_date, pane_2_open, pane_2_high, pane_2_low, pane_2_close, pane_2_volume, stockData.get(lastSixDays.get(1)));
                        updatePaneData(pane_3_date, pane_3_open, pane_3_high, pane_3_low, pane_3_close, pane_3_volume, stockData.get(lastSixDays.get(2)));
                        updatePaneData(pane_4_date, pane_4_open, pane_4_high, pane_4_low, pane_4_close, pane_4_volume, stockData.get(lastSixDays.get(3)));
                        updatePaneData(pane_5_date, pane_5_open, pane_5_high, pane_5_low, pane_5_close, pane_5_volume, stockData.get(lastSixDays.get(4)));
                        updatePaneData(pane_6_date, pane_6_open, pane_6_high, pane_6_low, pane_6_close, pane_6_volume, stockData.get(lastSixDays.get(5)));
                    }
    
                    // Set the symbol label
                    stockSymbolLabel.setText(symbol.toUpperCase());
                    api_time.setText("DAILY");
                    last_refreshed.setText("Last Refreshed: " + formatter.format(now));

                });
    
                return null;
            }
        };
    
        new Thread(task).start();
    }

    private void updatePaneData(Label dateLabel, Label openLabel, Label highLabel, Label lowLabel, Label closeLabel, Label volumeLabel, StockData data) {
        // Run the UI update on the JavaFX application thread
        Platform.runLater(() -> {
            if (data != null) {
                dateLabel.setText(data.getDate());
                openLabel.setText(String.format("Open: %.2f", data.getOpen()));
                highLabel.setText(String.format("High: %.2f", data.getHigh()));
                lowLabel.setText(String.format("Low: %.2f", data.getLow()));
                closeLabel.setText(String.format("Close: %.2f", data.getClose()));
                volumeLabel.setText(String.format("Volume: %d", data.getVolume()));
            }
        });
    }
}
    
