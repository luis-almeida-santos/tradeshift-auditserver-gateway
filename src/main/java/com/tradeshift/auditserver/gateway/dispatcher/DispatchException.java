package com.tradeshift.auditserver.gateway.dispatcher;

import org.springframework.http.HttpStatus;

public class DispatchException extends RuntimeException {

    public DispatchException(HttpStatus responseStatus, String body) {
        super(String.format("Failed to dispatch event to auditserver. Received: [{}] - {}", responseStatus, body));
    }
}
