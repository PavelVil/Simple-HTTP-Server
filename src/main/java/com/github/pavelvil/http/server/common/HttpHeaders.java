package com.github.pavelvil.http.server.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpHeaders {
    private final Map<String, List<String>> headers;

    public HttpHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public static HttpHeaders fromHeaderList(List<String> headerList) {
        Map<String, List<String>> headers = new HashMap<>();

        headerList.forEach(line -> {
            int colonIndex = line.indexOf(':');
            if (colonIndex != -1) {
                String name = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();

                headers.computeIfAbsent(name.toLowerCase(), k -> new ArrayList<>()).add(value);
            }
        });

        return new HttpHeaders(headers);
    }

    public static HttpHeaders fromHeaderMap(Map<String, String> headerMap) {
        var httpHeader = new HttpHeaders(new HashMap<>());
        httpHeader.setAll(headerMap);
        return httpHeader;
    }

    public String getFirst(String key) {
        List<String> values = headers.get(key.toLowerCase());
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    public List<String> get(String key) {
        return headers.get(key.toLowerCase());
    }

    public void add(String key, String value) {
        List<String> currentValues = headers.computeIfAbsent(key.toLowerCase(), k -> new ArrayList<>());
        currentValues.add(value);
    }

    public void addAll(String key, List<String> values) {
        List<String> currentValues = headers.computeIfAbsent(key.toLowerCase(), k -> new ArrayList<>());
        currentValues.addAll(values);
    }

    public void addAll(Map<String, List<String>> newValues) {
        newValues.forEach(this::addAll);
    }

    public void set(String key, String value) {
        List<String> newValueList = new ArrayList<>();
        newValueList.add(value);
        headers.put(key.toLowerCase(), newValueList);
    }

    public void setAll(Map<String, String> values) {
        values.forEach(this::set);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            sb.append(key).append(": ");

            for (int i = 0; i < values.size(); i++) {
                sb.append(values.get(i));
                if (i < values.size() - 1) {
                    sb.append(", ");
                }
            }

            sb.append("\r\n");
        }

        return sb.toString();
    }
}
