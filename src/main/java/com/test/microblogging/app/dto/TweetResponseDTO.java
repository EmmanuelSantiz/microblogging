package com.test.microblogging.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.test.microblogging.app.entity.Tweet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String username;
    //private Long userId;

    public TweetResponseDTO from(Tweet tweet) {
        TweetResponseDTO tweetResponseDTO = new TweetResponseDTO();
        tweetResponseDTO.setId(tweet.getId());
        tweetResponseDTO.setContent(tweet.getContent());
        tweetResponseDTO.setCreatedAt(tweet.getCreatedAt());
        tweetResponseDTO.setUsername(tweet.getUser().getUsername());
        return tweetResponseDTO;
    }
}
