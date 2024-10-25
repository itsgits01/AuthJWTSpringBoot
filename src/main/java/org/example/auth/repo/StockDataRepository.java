package org.example.auth.repo;


import org.example.auth.entity.StockData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockDataRepository extends JpaRepository<StockData, Long> {
    List<StockData> findByStockSymbolAndDateBetween(String stockSymbol, LocalDate startDate, LocalDate endDate);
}
