package com.maggio.telegram.togglbot.controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    @Value("${exchangeratesapi.url}") private String url;

    @Value("${exchangeratesapi.latest}") private String latest;

    @GetMapping("/latest")
    public Flux<JsonNode> latest() {
        return WebClient.create(url).get().uri(latest).retrieve().bodyToFlux(JsonNode.class)
            .flatMap((n) -> {
                List<Entry<String, JsonNode>> a = new LinkedList<>();
                Iterator<Entry<String, JsonNode>> i = n.get("rates").fields();
                while(i.hasNext()){
                    a.add(i.next());
                }
                return Flux.fromStream(a.stream());
            }).map(n -> {
                ObjectNode node = JsonNodeFactory.instance.objectNode();
                node.put(n.getKey(), n.getValue().asDouble());
                return node;
            });
    }
}