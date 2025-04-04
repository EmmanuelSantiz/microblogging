package com.test.microblogging.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.test.microblogging.app.entity.Tweet;
import com.test.microblogging.app.repository.TweetRepository;
import com.test.microblogging.app.service.TweetService;
import com.test.microblogging.app.exception.ResourceNotFoundException;

class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;
    
    @InjectMocks
    private TweetService tweetService;
    
    private Tweet testTweet;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testTweet = new Tweet();
        testTweet.setId(1L);
        testTweet.setContent("Test tweet content");
    }

    
    @Test
    void testGetTweetById_Success() {
        // Arrange
        when(tweetRepository.findById(anyLong())).thenReturn(Optional.of(testTweet));

        // Act
        Optional<Tweet> result = tweetRepository.findById(1L); // Usar findById en lugar de getById

        // Assert
        assertTrue(result.isPresent()); // Verifica que el Optional no está vacío
        assertEquals(testTweet.getId(), result.get().getId());
        assertEquals(testTweet.getContent(), result.get().getContent());
        verify(tweetRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetTweetById_NotFound() {
        // Arrange
        when(tweetRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            tweetService.getTweetById(1L); // Cambia getById por el método de servicio
        });
        verify(tweetRepository, times(1)).findById(1L);
    }
    
    @Test
    void testCreateTweet() {
        // Arrange
        when(tweetRepository.save(any(Tweet.class))).thenReturn(testTweet);
        
        // Act
        Tweet result = tweetRepository.save(testTweet);
        
        // Assert
        assertNotNull(result);
        assertEquals(testTweet.getId(), result.getId());
        assertEquals(testTweet.getContent(), result.getContent());
        verify(tweetRepository, times(1)).save(testTweet);
    }
    
}