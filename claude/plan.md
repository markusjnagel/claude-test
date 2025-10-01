# Quarkus MQTT Message Viewer - Implementation Plan

## Project Overview
Build a containerized reactive Quarkus web application that connects to an MQTT broker, subscribes to a topic, and displays incoming messages in real-time on a web frontend.

## Technology Stack
- **Framework**: Quarkus (reactive)
- **MQTT Client**: Eclipse Paho MQTT or SmallRye Reactive Messaging MQTT
- **Web**: Quarkus RESTEasy Reactive + WebSockets for real-time updates
- **Frontend**: HTML/CSS/JavaScript with Server-Sent Events (SSE) or WebSockets
- **Build Tool**: Maven
- **Container**: Podman
- **Base Image**: ubi8/openjdk-17 or quarkus native image

## Architecture
1. **MQTT Subscriber**: Reactive component that connects to MQTT broker and subscribes to topic
2. **Message Store**: In-memory buffer to store recent messages
3. **WebSocket/SSE Endpoint**: Pushes messages to connected web clients in real-time
4. **REST API**: Serves the web frontend and provides message history
5. **Configuration**: Environment variables for MQTT broker URL and topic

## Implementation Steps

### 1. Project Setup
- Generate Quarkus project with required extensions:
  - `quarkus-resteasy-reactive`
  - `quarkus-websockets` or `quarkus-resteasy-reactive-jackson`
  - `quarkus-smallrye-reactive-messaging-mqtt` or MQTT client library
  - `quarkus-container-image-docker` (for containerization)
- Configure Maven/Gradle build

### 2. MQTT Integration
- Create MQTT configuration class reading from environment variables:
  - `MQTT_BROKER_URL` (e.g., tcp://192.168.1.100:1883)
  - `MQTT_TOPIC` (e.g., iot/smartmeter/sensor/+/obis/+)
- Implement reactive MQTT subscriber using SmallRye Reactive Messaging
- Handle connection lifecycle and reconnection logic
- Parse incoming messages and extract payload, topic, QoS

### 3. Message Management
- Create Message model class (topic, payload, QoS, timestamp)
- Implement in-memory circular buffer to store last N messages (e.g., 100)
- Ensure thread-safe access to message store

### 4. Web Backend
- Create REST endpoint to serve static HTML page
- Implement SSE endpoint to stream messages to web clients
- Add REST endpoint to fetch message history on initial page load

### 5. Web Frontend
- Create HTML page with:
  - Header showing connection status
  - Message list container
  - CSS styling matching the screenshot design:
    - Card layout with left accent border (yellow/green)
    - Topic and QoS in header
    - Message payload prominently displayed
    - Timestamp at bottom
    - Light gray background for cards
- Implement JavaScript to:
  - Connect to SSE endpoint
  - Receive and render new messages dynamically
  - Auto-scroll to show latest messages

### 6. Containerization
- Create `Dockerfile` or use Quarkus container-image extension
- Configure JVM mode container build
- Expose web port (8080)
- Set up environment variable placeholders for MQTT config

### 7. Configuration Files
- Create `application.properties` with defaults
- Document all configurable parameters
- Set up logging for MQTT connection status

### 8. Documentation
- Create README with build and run instructions
- Document environment variables
- Provide example commands

## Build Instructions

### Prerequisites
- Java 17 or later
- Maven 3.8+
- Podman

### Step 1: Generate Quarkus Project
```bash
mvn io.quarkus:quarkus-maven-plugin:3.15.1:create \
    -DprojectGroupId=com.example \
    -DprojectArtifactId=mqtt-viewer \
    -DprojectVersion=1.0.0-SNAPSHOT \
    -Dextensions="resteasy-reactive,websockets,smallrye-reactive-messaging-mqtt,container-image-docker"
```

### Step 2: Build the Application
```bash
cd mqtt-viewer
./mvnw clean package
```

### Step 3: Build Container Image with Podman
```bash
# Using Quarkus container-image extension
./mvnw clean package -Dquarkus.container-image.build=true -Dquarkus.container-image.builder=docker

# Or manually with Dockerfile
podman build -f src/main/docker/Dockerfile.jvm -t mqtt-viewer:latest .
```

## Run Instructions

### Run Locally (Development Mode)
```bash
export MQTT_BROKER_URL=tcp://192.168.1.100:1883
export MQTT_TOPIC=iot/smartmeter/sensor/#
./mvnw quarkus:dev
```

### Run Container with Podman
```bash
podman run -d \
  --name mqtt-viewer \
  -p 8080:8080 \
  -e MQTT_BROKER_URL=tcp://192.168.1.100:1883 \
  -e MQTT_TOPIC=iot/smartmeter/sensor/1/obis/1-0:1.8.0/255/value \
  mqtt-viewer:latest
```

Access the application at: http://localhost:8080

### Stop Container
```bash
podman stop mqtt-viewer
podman rm mqtt-viewer
```

### View Logs
```bash
podman logs -f mqtt-viewer
```

## Configuration Parameters

| Parameter | Description | Example |
|-----------|-------------|---------|
| `MQTT_BROKER_URL` | MQTT broker connection URL | `tcp://192.168.1.100:1883` |
| `MQTT_TOPIC` | MQTT topic to subscribe to (supports wildcards) | `iot/smartmeter/#` |
| `QUARKUS_HTTP_PORT` | HTTP port (default: 8080) | `8080` |

## UI Design Specifications
- **Card Layout**: Light gray background (#f5f5f5)
- **Accent Border**: 4px left border (yellow-green gradient or solid #cddc39)
- **Typography**:
  - Topic/QoS: Small gray text (12px)
  - Payload: Large bold text (24px)
  - Timestamp: Small gray text (11px)
- **Spacing**: Adequate padding (16px) between elements
- **Auto-scroll**: Newest messages appear at top or auto-scroll to bottom

## Next Steps
Once approved, implementation will proceed through the steps above, creating all necessary files and configurations.
