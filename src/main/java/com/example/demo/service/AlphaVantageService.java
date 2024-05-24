package com.example.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AlphaVantageService {
    private final String apiKey = "WFNA12AAP73M61QY";
    private final String baseUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol={symbol}&apikey={apiKey}";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> getStockData(String symbol) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("apiKey", apiKey);

        // JSON 응답을 문자열로 받기
        String response = restTemplate.getForObject(baseUrl, String.class, params);
        System.out.println("****" + response + "*****");

        Map<String, Object> safeData = null;

        try {
            // 문자열을 Map<String, Object>로 변환
            safeData = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return safeData;
    }
}