#!/bin/bash

EXCHANGE_NAME=auditserver-gateway-exchange
AUDITSERVER_QUEUE_NAME=ha.auditserver_queue
PULSE_QUEUE_NAME=ha.pulse_queue
CREDENTIALS="-u admin -p admin"

RABBITMQADMIN=/tmp/rabbitmqadmin

wget http://127.0.0.1:15672/cli/rabbitmqadmin -O $RABBITMQADMIN

chmod +x $RABBITMQADMIN

rabbitmqctl set_policy ha-all-nodes "^ha\." '{"ha-mode":"all"}'

$RABBITMQADMIN $CREDENTIALS declare queue name=$AUDITSERVER_QUEUE_NAME durable=true
$RABBITMQADMIN $CREDENTIALS declare queue name=$PULSE_QUEUE_NAME durable=true

$RABBITMQADMIN $CREDENTIALS declare exchange name=$EXCHANGE_NAME type=topic

$RABBITMQADMIN $CREDENTIALS declare binding source=$EXCHANGE_NAME destination_type="queue" destination=$AUDITSERVER_QUEUE_NAME routing_key="#"
$RABBITMQADMIN $CREDENTIALS declare binding source=$EXCHANGE_NAME destination_type="queue" destination=$PULSE_QUEUE_NAME routing_key="#"

