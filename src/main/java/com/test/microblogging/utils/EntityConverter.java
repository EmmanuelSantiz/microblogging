package com.test.microblogging.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.microblogging.app.entity.Tweet;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class EntityConverter {
    
    private final ObjectMapper objectMapper;

    public EntityConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Convertir la entidad Tweet a un mapa
    public Map<String, Object> convertTweetToMap(Tweet tweet) {
        return objectMapper.convertValue(tweet, Map.class);
    }
}
