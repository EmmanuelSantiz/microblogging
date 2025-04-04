package com.test.microblogging.app.dto;

import com.test.microblogging.app.entity.Tweet;
import com.test.microblogging.app.entity.TweetInteraction;
import com.test.microblogging.app.entity.User;
import com.test.microblogging.utils.enums.InteractionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentTweetRequestDTO {

    @jakarta.validation.constraints.NotNull(message = "ID cannot be null")
    @jakarta.validation.constraints.NotEmpty(message = "ID cannot be empty")
    private String tweetId;

    @jakarta.validation.constraints.NotNull(message = "Comment cannot be null")
    @jakarta.validation.constraints.NotEmpty(message = "Comment cannot be empty")
    private String comment;

    public TweetInteraction toEntity(CommentTweetRequestDTO commentTweetRequestDTO, User user, Tweet tweet) {
        TweetInteraction comment = new TweetInteraction();
        comment.setInteractionType(InteractionType.COMMENT);
        comment.setCommentText(commentTweetRequestDTO.getComment());
        comment.setUser(user);
        comment.setTweet(tweet);
        return comment;
    }
}
