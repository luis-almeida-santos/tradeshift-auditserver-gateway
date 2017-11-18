package com.tradeshift.auditserver.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotEmpty;

@Data
@Configuration
@ConfigurationProperties(prefix = "auditserver-gateway")
public class AuditServerGatewayProperties {

    @NotEmpty
    private String queueName;

    @NotEmpty
    private String exchangeName;
}
