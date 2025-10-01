# MQTT Message Viewer

A reactive Quarkus web application that connects to an MQTT broker, subscribes to topics, and displays incoming messages in real-time on a web interface.

## Features

- 🔄 Real-time MQTT message streaming
- 🌐 Web-based message viewer with live updates
- 🐳 Containerized application (podman/docker)
- ⚙️ Configurable MQTT broker and topic via environment variables
- 📱 Responsive card-based UI design
- 🔌 Connects to any MQTT broker IP address

## Technology Stack

- **Quarkus** - Reactive Java framework
- **SmallRye Reactive Messaging** - MQTT client integration
- **Server-Sent Events (SSE)** - Real-time web updates
- **Podman/Docker** - Containerization

## Prerequisites

- Java 21 or later
- Maven 3.8+
- Podman (or Docker)

## Building the Application

### 1. Build the JAR

```bash
./mvnw clean package -DskipTests
```

### 2. Build the Container Image

Using podman:

```bash
podman build -f src/main/docker/Dockerfile.jvm -t mqtt-viewer:latest .
```

Using docker:

```bash
docker build -f src/main/docker/Dockerfile.jvm -t mqtt-viewer:latest .
```

## Running the Application

### Run Locally (Development Mode)

```bash
export MQTT_BROKER_HOST=192.168.1.100
export MQTT_BROKER_PORT=1883
export MQTT_TOPIC="iot/smartmeter/sensor/#"
./mvnw quarkus:dev
```

The application will be available at: http://localhost:8080

### Run with Podman

```bash
podman run -d \
  --name mqtt-viewer \
  -p 8080:8080 \
  -e MQTT_BROKER_HOST=192.168.1.100 \
  -e MQTT_BROKER_PORT=1883 \
  -e MQTT_TOPIC="warp3/wallbox/#" \
  mqtt-viewer:latest
```

### Run with Docker

```bash
docker run -d \
  --name mqtt-viewer \
  -p 8080:8080 \
  -e MQTT_BROKER_HOST=192.168.1.100 \
  -e MQTT_BROKER_PORT=1883 \
  -e MQTT_TOPIC="warp3/wallbox/#" \
  mqtt-viewer:latest
```

Access the web interface at: http://localhost:8080

## Container Management

### View Logs

```bash
podman logs -f mqtt-viewer
```

### Stop Container

```bash
podman stop mqtt-viewer
```

### Remove Container

```bash
podman rm mqtt-viewer
```

### List Running Containers

```bash
podman ps
```

## Configuration

The application is configured via environment variables:

| Variable | Description | Default | Example |
|----------|-------------|---------|---------|
| `MQTT_BROKER_HOST` | MQTT broker hostname or IP address | `localhost` | `192.168.1.100` |
| `MQTT_BROKER_PORT` | MQTT broker port | `1883` | `1883` |
| `MQTT_TOPIC` | MQTT topic pattern (supports wildcards) | `test/#` | `warp3/wallbox/#` |
| `QUARKUS_HTTP_PORT` | HTTP port for web interface | `8080` | `8080` |

### MQTT Topic Wildcards

- `#` - Multi-level wildcard (e.g., `sensor/#` matches `sensor/temp`, `sensor/temp/room1`, etc.)
- `+` - Single-level wildcard (e.g., `sensor/+/temp` matches `sensor/room1/temp`, `sensor/room2/temp`, etc.)

## Web Interface

The web interface displays MQTT messages in a card-based layout showing:

- **Topic** - The MQTT topic path
- **QoS** - Quality of Service level (0, 1, or 2)
- **Payload** - The message content
- **Timestamp** - When the message was received

Messages are displayed in real-time as they arrive, with the newest messages appearing at the top.

## Project Structure

```
mqtt-viewer/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── model/
│   │   │   │   └── MqttMessage.java          # Message model
│   │   │   ├── resource/
│   │   │   │   └── MessageResource.java      # REST/SSE endpoints
│   │   │   └── service/
│   │   │       ├── MessageStore.java         # Message buffer
│   │   │       └── MqttSubscriber.java       # MQTT subscriber
│   │   ├── resources/
│   │   │   ├── META-INF/resources/
│   │   │   │   └── index.html                # Web interface
│   │   │   └── application.properties        # Configuration
│   │   └── docker/
│   │       └── Dockerfile.jvm                # Container definition
│   └── test/
├── pom.xml
└── README.md
```

## Troubleshooting

### Cannot connect to MQTT broker

- Verify the MQTT broker host and port are correct
- Ensure the broker is accessible from the container/host
- Check network connectivity: `ping <broker-ip>`
- Verify broker is running and listening on the specified port

### No messages appearing

- Confirm messages are being published to the subscribed topic
- Check the topic pattern matches published messages
- Review container logs: `podman logs mqtt-viewer`
- Verify QoS settings are compatible

### Web interface not loading

- Ensure port 8080 is not already in use
- Check container is running: `podman ps`
- Verify port mapping is correct in the run command

## Development

### Running Tests

```bash
./mvnw test
```

### Hot Reload (Dev Mode)

Quarkus dev mode provides live reload:

```bash
./mvnw quarkus:dev
```

Make changes to Java files or resources, and they'll be automatically recompiled.

## License

This project was created as a test application for exploring Claude's capabilities.

---

*Built with Quarkus and SmallRye Reactive Messaging*
