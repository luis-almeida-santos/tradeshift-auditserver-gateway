#!/usr/bin/env bash

docker-compose -f docker/auditlog-server-gateway.yml up $* rabbit1 rabbit2 rabbit3