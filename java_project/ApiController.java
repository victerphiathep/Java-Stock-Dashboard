import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ApiController {

    @FXML
    private TextArea textArea; // Assuming you have a TextArea in your FXML to display the API data

    public void fetchAndDisplayApiData() {
        String api_key = "79XMX15IRSM5SQB9";
        String symbol = "IBM";
        String alpha_vantage_url = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s", symbol, api_key);
        
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                URL url = new URL(alpha_vantage_url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                connection.disconnect();

                // Update the UI Thread with the response
                final String response = content.toString();
                javafx.application.Platform.runLater(() -> {
                    textArea.setText(response); // Display the formatted data in the TextArea
                });
                
                return null;
            }
        };
        
        new Thread(task).start(); // Start the background task to fetch API data
    }
}