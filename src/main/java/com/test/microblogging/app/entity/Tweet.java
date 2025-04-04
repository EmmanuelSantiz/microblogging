package com.test.microblogging.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo de la entidad Tweet.
 * Representa un tweet realizado por un usuario.
 * Contiene información sobre el contenido del tweet, la fecha de creación y la relación con el usuario que lo creó.
 * @author Emmanuel Santiz
 * @date 2025-04-01
 */
@Getter
@Setter
@Entity
@Table(name = "tweet", indexes = {
    @Index(name = "idx_tweet_user_id", columnList = "user_id"),
    @Index(name = "idx_tweet_created_at", columnList = "created_at")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "content"}),
})
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column(nullable = false, length=512)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TweetInteraction> interactions;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
