package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Service
public class YahooFinanceAbsService {

    @Value("${api.key}")
    private String apiKey;

    private final String baseUrl = "https://yfapi.net/v8/finance/spark";

    public List<Map<String, Object>> getStockData(String symbol) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "?symbols=" + symbol + "&range=1y&interval=1d";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);
            return parseResponse(response.getBody(), symbol);
        } catch (HttpClientErrorException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch data: " + e.getMessage());
            List<Map<String, Object>> errorList = new ArrayList<>();
            errorList.add(errorResponse);
            return errorList;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to parse data: " + e.getMessage());
            List<Map<String, Object>> errorList = new ArrayList<>();
            errorList.add(errorResponse);
            return errorList;
        }
    }

    private List<Map<String, Object>> parseResponse(String jsonResponse, String symbol) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonResponse);
        JsonNode symbolNode = root.path(symbol);

        JsonNode timestamps = symbolNode.path("timestamp");
        JsonNode closes = symbolNode.path("close");

        List<Map<String, Object>> stockDataList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        double initialPrice = closes.get(0).asDouble(); // 1년 전의 주식 가격

        for (int i = 0; i < timestamps.size(); i++) {
            long timestamp = timestamps.get(i).asLong() * 1000L; // Convert to milliseconds
            Date date = new Date(timestamp);
            String dateString = sdf.format(date);
            double close = closes.get(i).asDouble();

            double change = ((close - initialPrice) / initialPrice) * 100; // 증감 비율 계산

            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("date", dateString);
            dataPoint.put("change", change);

            stockDataList.add(dataPoint);
        }

        return stockDataList;
    }
}
