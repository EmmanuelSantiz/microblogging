package com.test.microblogging.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.test.microblogging.utils.enums.InteractionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Modelo de la entidad TweetInteraction.
 * Representa una interacción (comentario, like, etc.) realizada por un usuario en un tweet.
 * Contiene información sobre el tipo de interacción, el texto del comentario (si aplica), la fecha de creación y la relación con el tweet y el usuario.
 * @author Emmanuel Santiz
 * @date 2025-04-01
 */
@Getter
@Setter
@Entity
@Table(name = "tweet_interaction", indexes = {
    @Index(name = "idx_interaction_tweet", columnList = "tweet_id"),
    @Index(name = "idx_interaction_user", columnList = "user_id"),
    @Index(name = "idx_interaction_type", columnList = "interaction_type")
})
public class TweetInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type", nullable = false)
    private InteractionType interactionType;

    @Column(name = "comment_text", length = 280)
    private String commentText;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tweet_id", nullable = false)
    @JsonBackReference
    private Tweet tweet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-interactions")
    private User user;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
}