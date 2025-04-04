package com.test.microblogging.app.repository;

import com.test.microblogging.app.entity.TweetInteraction;
import com.test.microblogging.utils.enums.InteractionType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import java.util.List;
import java.util.Optional;

@Repository
public interface TweetInteractionRepository extends JpaRepository<TweetInteraction, Long> {

    /**
     * Find all interactions for a specific tweet
     */
    List<TweetInteraction> findByTweetId(Long tweetId);
    
    /**
     * Find all comments for a specific tweet
     */
    List<TweetInteraction> findByTweetIdAndInteractionType(Long tweetId, InteractionType interactionType);
    
    /**
     * Count the number of likes for a specific tweet
     */
    long countByTweetIdAndInteractionType(Long tweetId, InteractionType interactionType);
    
    /**
     * Check if a user has liked a specific tweet
     */
    boolean existsByTweetIdAndUserIdAndInteractionType(Long tweetId, Long userId, InteractionType interactionType);
    
    /**
     * Find a specific like by tweet and user
     */
    Optional<TweetInteraction> findByTweetIdAndUserIdAndInteractionType(Long tweetId, Long userId, InteractionType interactionType);
    
    /**
     * Find all interactions by a specific user
     */
    List<TweetInteraction> findByUserId(Long userId);
}