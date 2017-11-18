package com.tradeshift.auditserver.gateway.dispatcher;

import com.tradeshift.auditserver.gateway.model.AuditServerGatewayEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditServerGatewayEventDispatcher {

    private static final Logger log = LoggerFactory.getLogger(AuditServerGatewayEventDispatcher.class);

    @RabbitListener(queues = "${auditserver-gateway.queueName}", concurrency = "${auditserver-gateway.consumerCount:1}")
    @Transactional
    public void handleMessage(Message message, @Payload AuditServerGatewayEvent auditServerGatewayEvent) {

        log.info("Dispatching a message: {}", message);
        // if(new Random().nextBoolean()) {
        //     throw new RuntimeException("Aborting dispatch");
        // }
    }
}
