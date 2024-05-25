package com.github.pavelvil.http.server.request;

import com.github.pavelvil.http.server.common.HttpHeaders;
import com.github.pavelvil.http.server.common.HttpMethod;
import com.github.pavelvil.http.server.exception.RequestContextException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestContext {

    private final HttpMethod method;

    private final String path;

    private final HttpHeaders headers;

    private final List<String> pathParts;

    private String body;

    public RequestContext(HttpMethod method, String path, HttpHeaders headers) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.pathParts = Arrays.stream(path.split("/")).toList();
    }

    public static RequestContext buildContext(BufferedReader reader) {
        try {
            var requestLine = reader.readLine();
            if (requestLine == null || requestLine.isBlank()) {
                return null;
            }
            var methodWithPath = extractMethodAndPath(requestLine);
            List<String> headers = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                headers.add(line);
            }

            var httpHeaders = HttpHeaders.fromHeaderList(headers);
            var requestContext = new RequestContext(methodWithPath.getKey(), methodWithPath.getValue(), httpHeaders);

            var contentLength = httpHeaders.getFirst("Content-Length");
            if (contentLength != null && !contentLength.isBlank()) {
                int bodySize = Integer.parseInt(contentLength);
                char[] bodyBuffer = new char[bodySize];
                reader.read(bodyBuffer);
                requestContext.setBody(new String(bodyBuffer));
            }

            return requestContext;
        } catch (IOException ex) {
            System.out.println("Exception trying to build request context");
            throw new RequestContextException(ex);
        }
    }

    private static AbstractMap.SimpleEntry<HttpMethod, String> extractMethodAndPath(String requestLine) {
        var parts = requestLine.split(" ");
        return new AbstractMap.SimpleEntry<>(HttpMethod.fromType(parts[0]), parts[1]);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public String getPath() {
        return path;
    }

    public boolean hasPath() {
        return path != null && !path.isBlank();
    }

    public boolean pathStartsWith(String value) {
        return hasPath() && path.startsWith(value);
    }

    public String getLastPart() {
        return getPart(pathParts.size() - 1);
    }

    public String getPart(int index) {
        if (pathParts.size() < index) {
            return null;
        }
        return pathParts.get(index);
    }

    public boolean pathIsEqualsTo(String actualPath) {
        return hasPath() && path.equals(actualPath);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "RequestContext{" +
                "method=" + method +
                ", path='" + path + '\'' +
                '}';
    }
}
