package com.example.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StockApiService {

    private static final String API_KEY = "79XMX15IRSM5SQB9";


    public String getStockSymbol(String symbol) {
        String alphaVantageUrl = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s", symbol, API_KEY);
        StringBuilder content = new StringBuilder();
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

            // Use Gson to parse JSON
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(content.toString(), JsonObject.class);

            // Line to get Symbol from JSON
            return jsonObject.getAsJsonObject("Meta Data").get("2. Symbol").getAsString();

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public Map<String, StockData> getStockData(String symbol) {
        String urlString = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s", symbol, API_KEY);
        Map<String, StockData> stockDataMap = new HashMap<>();
        try {
            URL url = new URL(urlString);
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

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(content.toString(), JsonObject.class);
            JsonObject timeSeries = jsonObject.getAsJsonObject("Time Series (Daily)");

            for (String date : timeSeries.keySet()) {
                JsonObject dayData = timeSeries.getAsJsonObject(date);
                double open = dayData.get("1. open").getAsDouble();
                double high = dayData.get("2. high").getAsDouble();
                double low = dayData.get("3. low").getAsDouble();
                double close = dayData.get("4. close").getAsDouble();
                long volume = dayData.get("5. volume").getAsLong();
                stockDataMap.put(date, new StockData(date, open, high, low, close, volume));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stockDataMap;
    }


}

