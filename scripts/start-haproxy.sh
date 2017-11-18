#!/usr/bin/env bash

docker-compose -f docker/auditlog-server-gateway.yml up $* haproxy