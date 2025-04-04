package com.test.microblogging.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
