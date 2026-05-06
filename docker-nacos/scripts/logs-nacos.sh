#!/bin/bash

NACOS_CONTAINER_NAME="nacos-local"

if docker ps -a --format '{{.Names}}' | grep -q "^${NACOS_CONTAINER_NAME}$"; then
    docker logs -f ${NACOS_CONTAINER_NAME} --tail 100
else
    echo "Nacos 容器不存在"
fi