package com.test.microblogging.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_following", indexes = {
    @Index(name = "idx_user_following_follower_id", columnList = "follower_id"), // Índice en 'follower_id'
    @Index(name = "idx_user_following_followed_id", columnList = "followed_id") // Índice en 'followed_id'
}, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"follower_id", "followed_id"})
})
public class UserFollowing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    @Column(nullable = false)
    private LocalDateTime followedAt = LocalDateTime.now();
}
