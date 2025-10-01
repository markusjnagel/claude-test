package com.example.service;

import com.example.model.MqttMessage;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MqttSubscriber {

    private static final Logger LOG = Logger.getLogger(MqttSubscriber.class);

    @Inject
    MessageStore messageStore;

    @Incoming("mqtt-messages")
    public Uni<Void> receiveMessage(Message<byte[]> message) {
        try {
            io.smallrye.reactive.messaging.mqtt.MqttMessage<?> mqttMessage =
                (io.smallrye.reactive.messaging.mqtt.MqttMessage<?>) message;

            String topic = mqttMessage.getTopic();
            String payload = new String((byte[]) mqttMessage.getPayload());
            int qos = mqttMessage.getQosLevel().value();

            LOG.infof("Received message on topic %s with QoS %d: %s", topic, qos, payload);

            MqttMessage msg = new MqttMessage(topic, payload, qos);
            messageStore.addMessage(msg);

            return Uni.createFrom().completionStage(message.ack());
        } catch (Exception e) {
            LOG.error("Error processing MQTT message", e);
            return Uni.createFrom().completionStage(message.nack(e));
        }
    }
}
