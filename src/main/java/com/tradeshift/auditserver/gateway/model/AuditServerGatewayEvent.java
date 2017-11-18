package com.tradeshift.auditserver.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditServerGatewayEvent {

    private String id;
    private String event;
}
