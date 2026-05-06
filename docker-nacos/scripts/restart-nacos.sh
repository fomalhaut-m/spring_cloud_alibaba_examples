#!/bin/bash

NACOS_CONTAINER_NAME="nacos-local"
NACOS_VERSION="v3.0.3"
NACOS_DATA_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)/nacos-data"

check_container_exists() {
    if docker ps -a --format '{{.Names}}' | grep -q "^${NACOS_CONTAINER_NAME}$"; then
        return 0
    else
        return 1
    fi
}

restart_nacos() {
    if ! check_container_exists; then
        echo "Nacos 容器不存在，请先运行 start-nacos.sh"
        exit 1
    fi

    echo "停止旧容器..."
    docker stop ${NACOS_CONTAINER_NAME} 2>/dev/null
    docker rm ${NACOS_CONTAINER_NAME} 2>/dev/null

    echo "重启 Nacos 容器..."

    docker run -d \
        --name ${NACOS_CONTAINER_NAME} \
        -e MODE=standalone \
        -e NACOS_AUTH_ENABLE=false \
        -e NACOS_AUTH_TOKEN=U2VjcmV0S2V5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5 \
        -e NACOS_AUTH_IDENTITY_KEY=nacos \
        -e NACOS_AUTH_IDENTITY_VALUE=nacos \
        -e JVM_XMS=256m \
        -e JVM_XMX=512m \
        -p 8080:8080 \
        -p 8848:8848 \
        -p 9848-9849:9848-9849 \
        --restart=unless-stopped \
        -v ${NACOS_DATA_DIR}:/home/nacos/data \
        nacos/nacos-server:${NACOS_VERSION}

    sleep 2

    echo ""
    echo "=========================================="
    echo "  Nacos 重启成功！"
    echo "=========================================="
    echo "  访问地址: http://localhost:8848/nacos"
    echo "=========================================="
}

show_usage() {
    echo "用法: $0"
    echo "  重启 Nacos 容器"
}

case "${1:-}" in
    *)
        restart_nacos
        ;;
esac