package com.test.microblogging.app.service;

import com.test.microblogging.app.dto.CommentTweetRequestDTO;
import com.test.microblogging.app.entity.Tweet;
import com.test.microblogging.app.entity.TweetInteraction;
import com.test.microblogging.app.entity.User;
import com.test.microblogging.app.exception.InvalidRequestException;
import com.test.microblogging.app.exception.ResourceNotFoundException;
import com.test.microblogging.app.repository.TweetInteractionRepository;
import com.test.microblogging.app.repository.TweetRepository;
import com.test.microblogging.utils.enums.InteractionType;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
public class TweetInteractionService {

    private final TweetInteractionRepository interactionRepository;
    private final TweetRepository tweetRepository;
    private final UserService userService;

    @Autowired
    public TweetInteractionService(
            TweetInteractionRepository interactionRepository,
            TweetRepository tweetRepository,
            UserService userService) {
        this.interactionRepository = interactionRepository;
        this.tweetRepository = tweetRepository;
        this.userService = userService;
    }

    /**
     * Add a comment to a tweet
     */
    @Transactional
    public TweetInteraction addComment(String username, CommentTweetRequestDTO commentTweetRequestDTO) {
        User user = userService.getUserOrAdd(username.trim());
        if (Objects.isNull(user)) {
            log.error("Error en usuario: {}", username);
            throw new ResourceNotFoundException("User not found");
        }
        
        Tweet tweet = tweetRepository.findById(Long.parseLong(commentTweetRequestDTO.getTweetId()))
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found with ID: " + commentTweetRequestDTO.getTweetId()));

        TweetInteraction comment = commentTweetRequestDTO.toEntity(commentTweetRequestDTO, user, tweet);
        return interactionRepository.save(comment);
    }

    /**
     * Like a tweet
     */
    @Transactional
    public TweetInteraction likeTweet(String username, Long tweetId) {
        User user = userService.getUserOrAdd(username.trim());
        if (Objects.isNull(user)) {
            log.error("Error en usuario: {}", user);
            throw new ResourceNotFoundException("User not found");
        }
        
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found with ID: " + tweetId));
        
        if (interactionRepository.existsByTweetIdAndUserIdAndInteractionType(
                tweetId, user.getId(), InteractionType.LIKE)) {
            throw new InvalidRequestException("User already liked this tweet");
        }
        
        TweetInteraction like = new TweetInteraction();
        like.setInteractionType(InteractionType.LIKE);
        like.setUser(user);
        like.setTweet(tweet);
        
        return interactionRepository.save(like);
    }

    /**
     * Unlike a tweet
     */
    @Transactional
    public void unlikeTweet(String username, Long tweetId) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        
        Optional<TweetInteraction> like = interactionRepository.findByTweetIdAndUserIdAndInteractionType(
                tweetId, user.getId(), InteractionType.LIKE);
        
        if (like.isPresent()) {
            interactionRepository.delete(like.get());
        } else {
            throw new ResourceNotFoundException("Like not found");
        }
    }

    /**
     * Get all comments for a tweet
     */
    public List<TweetInteraction> getComments(Long tweetId) {
        return interactionRepository.findByTweetIdAndInteractionType(
                tweetId, InteractionType.COMMENT);
    }

    /**
     * Get like count for a tweet
     */
    public long getLikeCount(Long tweetId) {
        return interactionRepository.countByTweetIdAndInteractionType(
                tweetId, InteractionType.LIKE);
    }

    /**
     * Check if a user has liked a tweet
     */
    public boolean hasUserLikedTweet(String username, Long tweetId) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        
        return interactionRepository.existsByTweetIdAndUserIdAndInteractionType(
                tweetId, user.getId(), InteractionType.LIKE);
    }
}