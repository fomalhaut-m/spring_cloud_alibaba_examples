#!/bin/bash

NACOS_CONTAINER_NAME="nacos-local"
NACOS_VERSION="v3.0.3"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
NACOS_DATA_DIR="${SCRIPT_DIR}/../nacos-data"

# 检测 Windows 环境
is_windows() {
    [[ "$(uname -s)" == *MINGW* ]] || [[ "$(uname -s)" == *CYGWIN* ]] || [[ -n "$MSYSTEM" ]]
}

check_docker() {
    if ! command -v docker &> /dev/null; then
        echo "Docker 未安装，请先安装 Docker Desktop"
        exit 1
    fi
    
    # Windows 环境检查
    if is_windows; then
        # 检查 Docker Desktop 是否在 WSL2 模式下
        if command -v wsl.exe &> /dev/null; then
            if ! wsl.exe -d docker-desktop -- test -S /var/run/docker.sock 2>/dev/null; then
                # 尝试直接在 Windows 上检查 Docker
                if ! docker version &> /dev/null; then
                    echo "Docker Desktop 未运行，请启动 Docker Desktop"
                    exit 1
                fi
            fi
        else
            if ! docker version &> /dev/null; then
                echo "Docker Desktop 未运行，请启动 Docker Desktop"
                exit 1
            fi
        fi
    fi
}

check_container_exists() {
    if docker ps -a --format '{{.Names}}' | grep -q "^${NACOS_CONTAINER_NAME}$"; then
        return 0
    else
        return 1
    fi
}

check_container_running() {
    if docker ps --format '{{.Names}}' | grep -q "^${NACOS_CONTAINER_NAME}$"; then
        return 0
    else
        return 1
    fi
}

create_nacos_dirs() {
    mkdir -p "${NACOS_DATA_DIR}"
    echo "Nacos 数据目录已创建: ${NACOS_DATA_DIR}"
}

start_nacos() {
    check_docker
    create_nacos_dirs

    if check_container_running; then
        echo "Nacos 容器已在运行中"
        echo "访问地址: http://localhost:8848/nacos"
        return
    fi

    if check_container_exists; then
        echo "启动已存在的 Nacos 容器..."
        docker start ${NACOS_CONTAINER_NAME}
    else
        echo "创建并启动 Nacos 容器..."
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
    fi

    if check_container_running; then
        echo ""
        echo "=========================================="
        echo "  Nacos 启动成功！"
        echo "=========================================="
        echo "  访问地址: http://localhost:8848/nacos"
        echo "  默认账号: nacos"
        echo "  默认密码: nacos"
        echo "=========================================="
    else
        echo "Nacos 启动失败，请检查日志: docker logs ${NACOS_CONTAINER_NAME}"
    fi
}

show_status() {
    if check_container_running; then
        echo "Nacos 容器状态: 运行中"
        docker ps --filter "name=${NACOS_CONTAINER_NAME}" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    else
        echo "Nacos 容器状态: 未运行"
    fi
}

case "${1:-start}" in
    start)
        start_nacos
        ;;
    status)
        show_status
        ;;
    *)
        echo "用法: $0 {start|status}"
        echo "  start  - 启动 Nacos 容器"
        echo "  status - 查看 Nacos 容器状态"
        ;;
esac