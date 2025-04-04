package com.test.microblogging.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.test.microblogging.utils.enums.InteractionType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity representing user interactions with tweets (comments and likes).
 * This entity allows users to comment on tweets and/or mark them as liked.
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

    /**
     * The type of interaction (COMMENT or LIKE)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type", nullable = false)
    private InteractionType interactionType;

    /**
     * Comment text (only used when interactionType is COMMENT)
     */
    @Column(name = "comment_text", length = 280)
    private String commentText;

    /**
     * When the interaction was created
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * The tweet being interacted with
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tweet_id", nullable = false)
    @JsonBackReference
    private Tweet tweet;

    /**
     * The user who created the interaction
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-interactions")
    private User user;

    /**
     * Pre-persist hook to set the creation timestamp
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
}