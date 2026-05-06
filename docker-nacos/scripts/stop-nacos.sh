#!/bin/bash

NACOS_CONTAINER_NAME="nacos-local"

stop_nacos() {
    if docker ps --format '{{.Names}}' | grep -q "^${NACOS_CONTAINER_NAME}$"; then
        echo "停止 Nacos 容器..."
        docker stop ${NACOS_CONTAINER_NAME}
        echo "Nacos 容器已停止"
    else
        echo "Nacos 容器未在运行"
    fi
}

remove_nacos() {
    if docker ps -a --format '{{.Names}}' | grep -q "^${NACOS_CONTAINER_NAME}$"; then
        echo "删除 Nacos 容器..."
        docker rm -f ${NACOS_CONTAINER_NAME}
        echo "Nacos 容器已删除"
        echo "注意: 数据已持久化到 ~/.nacos/ 目录，不会丢失"
    else
        echo "Nacos 容器不存在"
    fi
}

case "${1:-stop}" in
    stop)
        stop_nacos
        ;;
    remove)
        stop_nacos
        remove_nacos
        ;;
    *)
        echo "用法: $0 {stop|remove}"
        echo "  stop   - 停止 Nacos 容器"
        echo "  remove - 停止并删除 Nacos 容器"
        ;;
esac