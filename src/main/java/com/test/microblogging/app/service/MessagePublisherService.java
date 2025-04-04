package com.test.microblogging.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.microblogging.app.entity.Tweet;
import com.test.microblogging.utils.EntityConverter;

import lombok.extern.log4j.Log4j2;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log4j2
@Service
public class MessagePublisherService {

    private final RabbitTemplate rabbitTemplate;
    private final EntityConverter entityConverter;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessagePublisherService(RabbitTemplate rabbitTemplate, EntityConverter entityConverter, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.entityConverter = entityConverter;
        this.objectMapper = objectMapper;
    }

    public void publishTweet(Tweet tweet) {
        // Obtener el traceId del MDC (Mapped Diagnostic Context) de Sleuth
        String traceId = org.slf4j.MDC.get("X-B3-TraceId");

        // Convertir el Tweet a un mapa
        Map<String, Object> tweetMap = entityConverter.convertToMap(tweet);

        try {
            // Convertir el mapa a un JSON (String)
            String tweetJson = objectMapper.writeValueAsString(tweetMap);

            // Crear un mensaje con el traceId como propiedad
            Message rabbitMessage = MessageBuilder.withBody(tweetJson.getBytes())
                    .setHeader(MessageHeaders.CONTENT_TYPE, "application/json")
                    .setHeader("X-B3-TraceId", traceId)
                    .build();

            // Enviar el mensaje a RabbitMQ
            rabbitTemplate.send("myExchange", "myRoutingKey", rabbitMessage);
            log.info("Mensaje enviado a RabbitMQ: {}", tweetJson);
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de excepciones (por ejemplo, si hay un error al serializar el mapa)
        }
    }
}
