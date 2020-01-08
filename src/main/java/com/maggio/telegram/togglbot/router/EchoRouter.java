package com.maggio.telegram.togglbot.router;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class EchoRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoRouter.class);

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
            .route(RequestPredicates.POST("/echo").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                request -> request.bodyToMono(JsonNode.class)
                    .map(node -> node.get("message"))
                    .map((message) -> {
                        LOGGER.debug("message: {}", message);
                        ObjectNode node = JsonNodeFactory.instance.objectNode();
                        node.put("method", "sendMessage");
                        LOGGER.debug("chat: {}", message.get("chat"));
                        node.put("chat_id", message.get("chat").get("id").asInt());
                        node.put("text", message.get("text").asText());
                        return node;
                    }).flatMap(req -> {
                        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(req));
                    })
            );
    }
}