package com.github.pavelvil.http.server.exception;

public class RequestContextException extends RuntimeException {
    public RequestContextException() {
    }

    public RequestContextException(String message) {
        super(message);
    }

    public RequestContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestContextException(Throwable cause) {
        super(cause);
    }
}
