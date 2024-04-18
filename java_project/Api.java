import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Api {
    public static void main(String[] args) {
        String api_key = "79XMX15IRSM5SQB9";
        String symbol = "IBM";
        
        String alpha_vantage_url = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s", symbol, api_key);
        
        try{
            // Create URL object
            URL url = new URL(alpha_vantage_url);

            // Open Connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Request Properties
            connection.setRequestProperty("Accept", "application/json");

            // Check response code for successful request
            int status = connection.getResponseCode();
            System.out.println("Response Code: " + status);

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // Print the content
            System.out.println("Response: ");
            System.out.println(content.toString());
            
            // Close the connection
            connection.disconnect();


        } catch  (Exception e) {
            e.printStackTrace();
        }
    }
}