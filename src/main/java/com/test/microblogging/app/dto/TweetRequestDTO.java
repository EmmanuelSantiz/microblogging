package com.test.microblogging.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.test.microblogging.app.entity.Tweet;
import com.test.microblogging.app.entity.User;
import com.test.microblogging.utils.Constantes;

import jakarta.validation.constraints.Size;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TweetRequestDTO {

    @jakarta.validation.constraints.NotNull(message = "Content cannot be null")
    @jakarta.validation.constraints.NotEmpty(message = "Username cannot be empty")
    @Size(min = 1, max = Constantes.MAX_CHARACTERES, message = "Content must be between 1 and " + Constantes.MAX_CHARACTERES + " characters")
    private String content;

    @jakarta.validation.constraints.NotNull(message = "Content cannot be null")
    @jakarta.validation.constraints.NotEmpty(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    public Tweet toEntity(TweetRequestDTO tweetRequestDTO, User user) {
        Tweet tweet = new Tweet();
        tweet.setCreatedAt(LocalDateTime.now());
        tweet.setUser(user);
        tweet.setContent(tweetRequestDTO.getContent());
        return tweet;
    }
 }
