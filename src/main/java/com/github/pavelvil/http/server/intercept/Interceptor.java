package com.github.pavelvil.http.server.intercept;

import com.github.pavelvil.http.server.request.RequestContext;
import com.github.pavelvil.http.server.response.ResponseContext;

public interface Interceptor {
    void beforeSendResponse(RequestContext requestContext, ResponseContext responseContext);
}
