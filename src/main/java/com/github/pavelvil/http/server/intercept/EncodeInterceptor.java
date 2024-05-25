package com.github.pavelvil.http.server.intercept;

import com.github.pavelvil.http.server.exception.InterceptorException;
import com.github.pavelvil.http.server.request.RequestContext;
import com.github.pavelvil.http.server.response.ResponseContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

public class EncodeInterceptor implements Interceptor {

    @Override
    public void beforeSendResponse(RequestContext requestContext, ResponseContext responseContext) {
        var acceptEncoding = requestContext.getHeaders().getFirst("Accept-Encoding");
        if (acceptEncoding != null && !acceptEncoding.isBlank() && responseContext.getResponseBody() != null) {
            var parts = acceptEncoding.split(",");
            Arrays.stream(parts)
                    .filter(it -> it.trim().equalsIgnoreCase("gzip"))
                    .findFirst()
                    .ifPresent(gzipString -> {
                        byte[] responseBody = compressResponseBody(responseContext.getResponseBody());
                        responseContext.getHeaders().set("Content-Length", String.valueOf(responseBody.length));
                        responseContext.getHeaders().set("Content-Encoding", "gzip");
                        responseContext.setResponseBody(responseBody);
                    });
        }
    }

    private byte[] compressResponseBody(byte[] responseBody) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
                gzipOutputStream.write(responseBody);
            }
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new InterceptorException("Exception trying to gzip response body! " + new String(responseBody));
        }
    }
}
