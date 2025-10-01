package com.example.resource;

import com.example.model.MqttMessage;
import com.example.service.MessageStore;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestStreamElementType;

import java.util.List;

@Path("/api")
public class MessageResource {

    @Inject
    MessageStore messageStore;

    @GET
    @Path("/messages")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MqttMessage> getMessages() {
        return messageStore.getRecentMessages();
    }

    @GET
    @Path("/messages/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<MqttMessage> streamMessages() {
        return messageStore.getMessageStream();
    }
}
