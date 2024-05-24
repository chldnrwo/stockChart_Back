package com.example.demo.controller;

import com.example.demo.service.YahooFinanceAbsService;
import com.example.demo.service.YahooFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stocks")
@CrossOrigin(origins = "http://localhost:8081") // Vue.js 애플리케이션이 실행되는 도메인
public class StockController {

    @Autowired
    private YahooFinanceService yahooFinanceService;

    @Autowired
    private YahooFinanceAbsService yahooFinanceAbsService;

    @GetMapping("/{symbol}")
    public List<Map<String, Object>> getStock(@PathVariable("symbol") String symbol) {
        return yahooFinanceService.getStockData(symbol);
    }

    @GetMapping("/abs/{symbol}")
    public List<Map<String, Object>> getStockAbs(@PathVariable("symbol") String symbol) {
        return yahooFinanceAbsService.getStockData(symbol);
    }

    @GetMapping("/M7")
    public Map<String, List<Map<String, Object>>> getTeslaAndNvidiaStockData() {
        Map<String, List<Map<String, Object>>> stockData = new HashMap<>();
        stockData.put("TSLA", yahooFinanceService.getStockData("TSLA"));
        stockData.put("NVDA", yahooFinanceService.getStockData("NVDA"));
        stockData.put("GOOGL", yahooFinanceService.getStockData("GOOGL"));
        stockData.put("AAPL", yahooFinanceService.getStockData("AAPL"));
        stockData.put("MSFT", yahooFinanceService.getStockData("MSFT"));
        stockData.put("AMZN", yahooFinanceService.getStockData("AMZN"));
        stockData.put("META", yahooFinanceService.getStockData("META"));
        return stockData;
    }

    @GetMapping("/AbsM7")
    public Map<String, List<Map<String, Object>>> getTeslaAndNvidiaStockData2() {
        Map<String, List<Map<String, Object>>> stockData = new HashMap<>();
        stockData.put("TSLA", yahooFinanceAbsService.getStockData("TSLA"));
        stockData.put("NVDA", yahooFinanceAbsService.getStockData("NVDA"));
        stockData.put("GOOGL", yahooFinanceAbsService.getStockData("GOOGL"));
        stockData.put("AAPL", yahooFinanceAbsService.getStockData("AAPL"));
        stockData.put("MSFT", yahooFinanceAbsService.getStockData("MSFT"));
        stockData.put("AMZN", yahooFinanceAbsService.getStockData("AMZN"));
        stockData.put("META", yahooFinanceAbsService.getStockData("META"));
        return stockData;
    }
}