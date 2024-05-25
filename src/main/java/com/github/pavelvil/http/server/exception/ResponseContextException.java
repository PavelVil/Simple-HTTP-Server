package com.github.pavelvil.http.server.exception;

public class ResponseContextException extends RuntimeException {
    public ResponseContextException() {
    }

    public ResponseContextException(String message) {
        super(message);
    }

    public ResponseContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseContextException(Throwable cause) {
        super(cause);
    }

    public ResponseContextException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
