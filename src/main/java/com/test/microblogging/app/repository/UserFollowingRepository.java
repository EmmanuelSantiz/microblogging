package com.test.microblogging.app.repository;

import com.test.microblogging.app.entity.UserFollowing;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFollowingRepository extends JpaRepository<UserFollowing, Long> {

    /**
     * Consulta para verificar si un usuario sigue a otro
     * @param followerId
     * @param followedId
     * @return Optional<UserFollowing
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    Optional<UserFollowing> findByFollower_IdAndFollowed_Id(Long followerId, Long followedId);

    /**
     * Consulta para obtener la lista de seguidores de un usuario
     * @param followedId
     * @return List<UserFollowing>
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    List<UserFollowing> findByFollowed_Id(Long followedId, Pageable pageable);

    /**
     * Consulta para obtener la lista de usuarios que sigue un usuario
     * @param followerId
     * @return List<UserFollowing>
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    List<UserFollowing> findByFollower_Id(Long followerId, Pageable pageable);
}
