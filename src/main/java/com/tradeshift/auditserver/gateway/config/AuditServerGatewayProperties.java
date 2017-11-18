package com.tradeshift.auditserver.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Configuration
@ConfigurationProperties(prefix = "auditserver-gateway")
@Validated
public class AuditServerGatewayProperties {

    @NotEmpty
    private String queueName;

    @NotEmpty
    private String exchangeName;

    @NotEmpty
    private String auditServerURL;

    @Min(1)
    private Integer auditServerTimeout;
}
