package com.tradeshift.auditserver.gateway.rest;

import com.tradeshift.auditserver.gateway.model.AuditServerGatewayEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.Base64;


@Controller
@AllArgsConstructor
public class RestController {

    private static final Logger log = LoggerFactory.getLogger(RestController.class);

    private final RabbitTemplate rabbitTemplate;

    private final TopicExchange exchange;

    @PostMapping("/rest/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addLog(@PathVariable("id") String id, HttpEntity<String> input) throws IOException {

        log.debug("request received: {}", id);

        String event = decodeIfRequired(input.getBody());
        AuditServerGatewayEvent gatewayEvent = new AuditServerGatewayEvent(id, event);
        String routingKey = "";
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, gatewayEvent);
    }


    /**
     * Copied from tradeshift-adutiserver
     * <p>
     * Some components (v4-apps-server) sends JSON base64 encoded (double encoded).
     * <p>
     * Example:
     * <pre>
     *      input                             |  output
     *    ------------------------------------+-----------------------
     *     "eyJldmVudFR5cGUiOiAidGVzdCJ9Cg==" | {"eventType": "test"}
     *     {"eventType": "test"}              | {"eventType": "test"}
     * </pre>
     *
     * @param body non-null Json object that contain either an quoted string or an Json object.
     * @return input stream of Json object
     */
    private String decodeIfRequired(String body) {
        Base64.Decoder b64Decoder = Base64.getDecoder();
        // If the byte array contains quotes at start and end, Base 64 decode the sliced the array and wrap the
        // decoded array.
        if (body.length() > 2 && body.charAt(0) == '"' && body.charAt(body.length() - 1) == '"') {
            return new String(b64Decoder.decode(body.substring(1, body.length())));
        }
        return body;
    }
}
