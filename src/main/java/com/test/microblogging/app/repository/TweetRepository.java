package com.test.microblogging.app.repository;

import com.test.microblogging.app.entity.Tweet;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    /**
     * Find all tweets by user IDs in descending order of creation date.
     *
     * @param userIds List of user IDs to find tweets for.
     * @return List of tweets ordered by creation date in descending order.
     */
    List<Tweet> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds);

    /**
     * Find a tweet by its ID.
     *
     * @param id ID of the tweet to find.
     * @return Optional containing the found tweet, or empty if not found.
     */
    Optional<Tweet> findById(Long id);

    /**
     * Find all tweets by a specific user ID in descending order of creation date.
     *
     * @param userId ID of the user to find tweets for.
     * @return List of tweets ordered by creation date in descending order.
     */
    List<Tweet> findByUserIdOrderByCreatedAtDesc(Long userId);

}
