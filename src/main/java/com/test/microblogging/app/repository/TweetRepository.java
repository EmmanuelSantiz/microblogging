package com.test.microblogging.app.repository;

import com.test.microblogging.app.entity.Tweet;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    
    /**
     * Consulta todos los tweets por lista de IDs de usuarios en orden decendente con relacion a la fecha.
     * @param userIds
     * @return List<Tweet>
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    List<Tweet> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds, Pageable pageable);

    /**
     * Consulta un tweet por ID.
     * @param id
     * @return Optional<Tweet>
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    Optional<Tweet> findById(Long id);

    /**
     * Consulta todos los tweets por ID de usuario en orden decendente con relacion a la fecha.
     * @param userId
     * @return List<Tweet>
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    List<Tweet> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Consulta si existe ya un tweet con esta informacion
     * @param username
     * @param content
     * @return Tweet
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    Optional<Tweet> findByUserUsernameAndContent(String username, String content);

}
