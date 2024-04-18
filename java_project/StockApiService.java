import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class StockApiService {

    private static final String API_KEY = "79XMX15IRSM5SQB9";

    public String getStockData(String symbol) {
        String alphaVantageUrl = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s", symbol, API_KEY);
        StringBuffer content = new StringBuffer();
        try {
            URL url = new URL(alphaVantageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int status = connection.getResponseCode();
            System.out.println("Response Code: " + status);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
        return content.toString();
    }
}






















// Pre Connection Code -----------------

// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.io.BufferedReader;
// import java.io.InputStreamReader;

// public class Api {
//     public static void main(String[] args) {
//         String api_key = "79XMX15IRSM5SQB9";
//         String symbol = "GME";
        
//         String alpha_vantage_url = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s", symbol, api_key);
        
//         try{
//             // Create URL object
//             URL url = new URL(alpha_vantage_url);

//             // Open Connection
//             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//             connection.setRequestMethod("GET");

//             // Request Properties
//             connection.setRequestProperty("Accept", "application/json");

//             // Check response code for successful request
//             int status = connection.getResponseCode();
//             System.out.println("Response Code: " + status);

//             // Read the response
//             BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//             String inputLine;
//             StringBuffer content = new StringBuffer();
//             while ((inputLine = in.readLine()) != null) {
//                 content.append(inputLine);
//             }
//             in.close();

//             // Print the content
//             System.out.println("Response: ");
//             System.out.println(content.toString());
            
//             // Close the connection
//             connection.disconnect();


//         } catch  (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }