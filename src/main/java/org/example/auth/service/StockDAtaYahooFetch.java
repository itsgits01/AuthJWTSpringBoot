package org.example.auth.service;


import org.example.auth.entity.StockData;
import org.example.auth.repo.StockDataRepository;
import org.example.auth.response.YahooFinanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class StockDAtaYahooFetch {

    @Autowired
    private StockDataRepository stockDataRepository;

    private static final String YAHOO_FINANCE_API_URL = "https://query1.finance.yahoo.com/v8/finance/chart/";

    public void fetchAndStoreStockData(List<String> stockSymbols) {
        for (String symbol : stockSymbols) {
            // Fetch data from Yahoo Finance
            String url = YAHOO_FINANCE_API_URL + symbol + "?range=5y&interval=1d";
            RestTemplate restTemplate = new RestTemplate();
            YahooFinanceResponse response = restTemplate.getForObject(url, YahooFinanceResponse.class);

            if (response != null && response.getChart() != null && response.getChart().getResult() != null) {
                // Process the response to extract stock data
                for (int i = 0; i < response.getChart().getResult()[0].getTimestamp().length; i++) {
                    Long timestamp = response.getChart().getResult()[0].getTimestamp()[i];

                    // Convert timestamp to LocalDate
                    LocalDate stockDate = LocalDate.ofEpochDay(timestamp / 86400); // Convert seconds to days

                    // Check if the date is valid before inserting
                    if (stockDate.getYear() >= 1970 && stockDate.getYear() <= 9999) {
                        StockData stockData = new StockData();
                        stockData.setStockSymbol(symbol);
                        stockData.setDate(stockDate);
                        stockData.setOpen(BigDecimal.valueOf(response.getChart().getResult()[0].getIndicators().getQuote()[0].getOpen()[i]));
                        stockData.setHigh(BigDecimal.valueOf(response.getChart().getResult()[0].getIndicators().getQuote()[0].getHigh()[i]));
                        stockData.setLow(BigDecimal.valueOf(response.getChart().getResult()[0].getIndicators().getQuote()[0].getLow()[i]));
                        stockData.setClose(BigDecimal.valueOf(response.getChart().getResult()[0].getIndicators().getQuote()[0].getClose()[i]));
                        stockData.setVolume(response.getChart().getResult()[0].getIndicators().getQuote()[0].getVolume()[i]);

                        // Save the stock data to the database
                        stockDataRepository.save(stockData);
                    } else {
                        // Log or handle the invalid date case
                        System.out.println("Invalid date for symbol " + symbol + ": " + stockDate);
                    }
                }
            }
        }
    }

}
