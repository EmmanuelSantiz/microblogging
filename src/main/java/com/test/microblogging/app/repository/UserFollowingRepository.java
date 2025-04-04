package com.test.microblogging.app.repository;

import com.test.microblogging.app.entity.UserFollowing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFollowingRepository extends JpaRepository<UserFollowing, Long> {

    Optional<UserFollowing> findByFollower_IdAndFollowed_Id(Long followerId, Long followedId);

    // Consultas personalizadas para obtener seguidores o seguidos por usuario
    List<UserFollowing> findByFollowed_Id(Long followedId);
    List<UserFollowing> findByFollower_Id(Long followerId);
}
