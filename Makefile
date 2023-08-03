clean:
	gradle clean

build: clean
	gradle bootJar
	podman build -t kafka-skeleton .

create-network:
	podman network create kafka-network 2>/dev/null || true

stop-zookeeper:
	podman kill zookeeper || true
	podman rm zookeeper || true

start-zookeeper: stop-zookeeper create-network
	podman run --name zookeeper --network=kafka-network -d \
		-p 2181:2181 -p 2888:2888 -p 3888:3888 \
		-e ZOOKEEPER_CLIENT_PORT=2181 \
		-e ZOOKEEPER_TICK_TIME=2000 \
		confluentinc/cp-zookeeper:latest

stop-kafka:
	podman kill kafka || true
	podman rm kafka || true

start-kafka: stop-kafka start-zookeeper
	podman run --name kafka --network=kafka-network -d -p 29092:29092 \
		-e KAFKA_BROKER_ID=1 \
		-e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
		-e KAFKA_ADVERTISED_LISTENERS='PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092' \
		-e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP='PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT' \
		-e KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT \
		-e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
		confluentinc/cp-kafka:latest

stop-kafka-ui:
	podman kill kafka-ui || true
	podman rm kafka-ui || true

start-kafka-ui: stop-kafka-ui start-kafka
	podman run --name kafka-ui --network=kafka-network -d -p 8081:8080 \
		-e DYNAMIC_CONFIG_ENABLED=true \
		provectuslabs/kafka-ui:latest

stop-service:
	podman kill kafka-skeleton || true
	podman rm kafka-skeleton || true

start-service: stop-service create-network
	podman run --name kafka-skeleton --network kafka-network -d -p 8080:8080 \
		-e KAFKA_HOST=kafka \
		kafka-skeleton:latest

full-start: start-kafka-ui start-service

full-stop: stop-service stop-kafka-ui stop-kafka stop-zookeeper