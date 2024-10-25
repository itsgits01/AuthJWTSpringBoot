package org.example.auth.controller;

import org.example.auth.service.StockDAtaYahooFetch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockDataController {

    @Autowired
    private StockDAtaYahooFetch stockDAtaYahooFetch;

    @PostMapping("/fetch")
    public void fetchStockData() {
        // List of top 100 NASDAQ stock symbols (You can replace with the actual symbols)
        List<String> stockSymbols = Arrays.asList("AAPL", "MSFT", "GOOGL", "AMZN", "TSLA", "FB", "BRK.B", "NVDA", "JPM", "JNJ");

        stockDAtaYahooFetch.fetchAndStoreStockData(stockSymbols);
    }
}
