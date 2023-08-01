# Build project with Kafka containers
build:
podman network create kafka-network
podman run --name zookeeper --network=kafka-network -p 2181:2181 -p 2888:2888 -p 3888:3888 -e ZOOKEEPER_CLIENT_PORT=2181 -e ZOOKEEPER_TICK_TIME=2000 confluentinc/cp-zookeeper:latest
podman run --name kafka --network=kafka-network -p 9092:9092 -e KAFKA_BROKER_ID=1 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_ADVERTISED_LISTENERS='PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092' -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP='PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT' -e KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 -it confluentinc/cp-kafka:latest
podman run --name kafka-ui --network=kafka-network -p 8081:8080 -e DYNAMIC_CONFIG_ENABLED=true provectuslabs/kafka-ui:latest