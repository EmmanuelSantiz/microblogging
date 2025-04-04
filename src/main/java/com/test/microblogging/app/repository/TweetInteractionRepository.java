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
     * Consulta para obtener todos las interacciones de un tweet por id
     * @param tweetId
     * @return List<TweetInteraction>
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    List<TweetInteraction> findByTweetId(Long tweetId);
    
    /**
     * Consulta para obtener todas las interacciones de un tweet por tipo de interaccion
     * @param tweetId
     * @param interactionType
     * @return List<TweetInteraction>
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    List<TweetInteraction> findByTweetIdAndInteractionType(Long tweetId, InteractionType interactionType);
    
    /**
     * Consulta para contar todas las interacciones de un tweet por tipo de interaccion y tipo
     * @param tweetId
     * @param interactionType
     * @return long
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    long countByTweetIdAndInteractionType(Long tweetId, InteractionType interactionType);
    
    /**
     * Consulta para contar todas las interacciones de un tweet por tipo de interaccion y usuario
     * @param tweetId
     * @param userId
     * @param interactionType
     * @return long
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    boolean existsByTweetIdAndUserIdAndInteractionType(Long tweetId, Long userId, InteractionType interactionType);
    
    /**
     * Consulta para obtener una interaccion de un tweet por ID de usuario y tipo de interaccion
     * @param tweetId
     * @param userId
     * @param interactionType
     * @return Optional<TweetInteraction>
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    Optional<TweetInteraction> findByTweetIdAndUserIdAndInteractionType(Long tweetId, Long userId, InteractionType interactionType);
    
    /**
     * Consulta para obtener todas las interacciones de un usuario
     * @param userId
     * @return List<TweetInteraction>
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    List<TweetInteraction> findByUserId(Long userId);
}