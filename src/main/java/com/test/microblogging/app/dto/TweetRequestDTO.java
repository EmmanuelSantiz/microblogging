package com.test.microblogging.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.test.microblogging.app.entity.Tweet;
import com.test.microblogging.app.entity.TweetInteraction;
import com.test.microblogging.app.entity.User;
import com.test.microblogging.utils.Constantes;
import com.test.microblogging.utils.enums.InteractionType;

import jakarta.validation.constraints.Size;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TweetRequestDTO {

    @jakarta.validation.constraints.NotNull(message = "Comentario no puedes ser null")
    @jakarta.validation.constraints.NotEmpty(message = "Comentario no puede estar vacío")
    @Size(min = 1, max = Constantes.MAX_CHARACTERES, message = "Comentario debe tener entre 1  y " + Constantes.MAX_CHARACTERES + " caracteres")
    private String content;

    @jakarta.validation.constraints.NotNull(message = "Nombre de usuario no puede ser null")
    @jakarta.validation.constraints.NotEmpty(message = "Nombre de usuario no puede estar vacio")
    @Size(min = 3, max = 20, message = "Nombre de usuario debe tener entre 3 y 20 characters")
    private String username;

    public Tweet toEntity(TweetRequestDTO tweetRequestDTO, User user) {
        Tweet tweet = new Tweet();
        tweet.setCreatedAt(LocalDateTime.now());
        tweet.setUser(user);
        tweet.setContent(tweetRequestDTO.getContent());
        return tweet;
    }

    public TweetInteraction toEntityInteraction(TweetRequestDTO tweetRequestDTO, User user, Tweet tweet) {
        TweetInteraction comment = new TweetInteraction();
        comment.setInteractionType(InteractionType.COMMENT);
        comment.setCommentText(tweetRequestDTO.getContent());
        comment.setUser(user);
        comment.setTweet(tweet);
        return comment;
    }
 }
