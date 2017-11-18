package com.tradeshift.auditserver.gateway.dispatcher;

import com.tradeshift.auditserver.gateway.config.AuditServerGatewayProperties;
import com.tradeshift.auditserver.gateway.model.AuditServerGatewayEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuditServerGatewayEventDispatcher {

    private static final Logger log = LoggerFactory.getLogger(AuditServerGatewayEventDispatcher.class);

    private final RestTemplate restTemplate;

    private AuditServerGatewayProperties auditServerGatewayProperties;

    @RabbitListener(queues = "${auditserver-gateway.queueName}", concurrency = "${auditserver-gateway.consumerCount:1}")
    @Transactional
    public void handleMessage(Message message, @Payload AuditServerGatewayEvent auditServerGatewayEvent) {

        log.debug("Dispatching a message: {}", message);

        String id = auditServerGatewayEvent.getId();
        String request = auditServerGatewayEvent.getEvent();
        String url = auditServerGatewayProperties.getAuditServerURL();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class, id);

        HttpStatus responseStatus = response.getStatusCode();

        if (!responseStatus.is2xxSuccessful() ) {
            throw new DispatchException(responseStatus, response.getBody());
        }
    }
}
