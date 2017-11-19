# How to run:

* `./scripts/start-rabbit.sh  -d`
  * Starts a rabbitMQ cluster with 3 nodes
* `./scripts/configure-rabbitmq.sh`
  * Configures rabbitMQ exchange and queues
* `./scripts/start-haproxy.sh -d`
  * Starts an haproxy that connects to 3 auditlog-gateway instances running on the host (ports: `8397`, `18397`, `28397`, `38397`, `48397`)
* Execute `AuditServerGatewayApplication`
    * Set port with jvm option `-Dserver.port=<port number>` 

#### HAProxy stats:
    * http://127.0.0.1:8080/stats

#### RabbitMQ Management: 
    * http://localhost:15672
    * http://localhost:15673
    * http://localhost:15674