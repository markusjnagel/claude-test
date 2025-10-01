package com.example.model;

import java.time.LocalDateTime;

public class MqttMessage {
    private String topic;
    private String payload;
    private int qos;
    private LocalDateTime timestamp;

    public MqttMessage() {
    }

    public MqttMessage(String topic, String payload, int qos) {
        this.topic = topic;
        this.payload = payload;
        this.qos = qos;
        this.timestamp = LocalDateTime.now();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
