package com.test.microblogging.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.microblogging.app.entity.Tweet;
import com.test.microblogging.app.entity.User;

class EntityConverterTest {

    @InjectMocks
    private final EntityConverter entityConverter = new EntityConverter(new ObjectMapper()); // Inicialización manual del ObjectMappe

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertTweetToMap() {
        // Arrange
        Long id = 1L;
        String content = "Test tweet content";
        
        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setContent(content);
        tweet.setCreatedAt(null);
        tweet.setUser(new User());
        
        // Act
        Map<String, Object> result = entityConverter.convertTweetToMap(tweet);
        
        // Assert
        assertNotNull(result);
        assertEquals(id, result.get("id"));
        assertEquals(content, result.get("content"));
    }
    
    @Test
    void testConvertTweetToMapWithNullValues() {
        // Arrange
        Tweet tweet = new Tweet();
        tweet.setId(1L);
        
        // Act
        Map<String, Object> result = entityConverter.convertTweetToMap(tweet);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.get("id"));
        assertTrue(result.containsKey("content"));
    }
}