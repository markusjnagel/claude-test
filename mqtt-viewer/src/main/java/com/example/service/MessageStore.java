package com.example.service;

import com.example.model.MqttMessage;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class MessageStore {

    private static final int MAX_MESSAGES = 100;
    private final List<MqttMessage> messages = new CopyOnWriteArrayList<>();
    private final BroadcastProcessor<MqttMessage> broadcaster = BroadcastProcessor.create();

    public void addMessage(MqttMessage message) {
        messages.add(0, message);

        // Keep only the last MAX_MESSAGES
        if (messages.size() > MAX_MESSAGES) {
            messages.remove(messages.size() - 1);
        }

        // Broadcast to all connected clients
        broadcaster.onNext(message);
    }

    public List<MqttMessage> getRecentMessages() {
        return new ArrayList<>(messages);
    }

    public Multi<MqttMessage> getMessageStream() {
        return broadcaster
            .onOverflow().drop();
    }
}
