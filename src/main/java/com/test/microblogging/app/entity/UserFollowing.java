package com.test.microblogging.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

/**
 * Modelo de la entidad UserFollowing.
 * Representa la relación de seguimiento entre dos usuarios en la aplicación de microblogging.
 * Contiene información sobre el usuario que sigue (follower), el usuario seguido (followed) y la fecha de seguimiento.
 * @author Emmanuel Santiz
 * @date 2025-04-01
 */
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
