package com.test.microblogging.app.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.microblogging.app.dto.TweetResponseDTO;
import com.test.microblogging.app.dto.TweetRequestDTO;
import com.test.microblogging.app.entity.Tweet;
import com.test.microblogging.app.entity.User;
import com.test.microblogging.app.exception.InvalidRequestException;
import com.test.microblogging.app.exception.ResourceNotFoundException;
import com.test.microblogging.app.repository.TweetRepository;

import lombok.extern.log4j.Log4j2;



@Log4j2
@Service
public class TweetService {

    private final TweetRepository tweetRepository;

    private final UserService userService;
    private final MessagePublisherService messagePublisherService;

    @Autowired
    public TweetService(
        TweetRepository tweetRepository, 
        UserService userService,
        MessagePublisherService messagePublisherService
        ) {
        this.tweetRepository = tweetRepository;
        this.userService = userService;
        this.messagePublisherService = messagePublisherService;
    }

    @Transactional
    public TweetResponseDTO publishTweet(TweetRequestDTO tweetRequestDTO) {
        log.info("Recibe informacion: {}", tweetRequestDTO.toString());

        User user = userService.getUserOrAdd(tweetRequestDTO.getUsername().trim());
        if (Objects.isNull(user)) {
            log.error("Error en usuario: {}", user);
            throw new ResourceNotFoundException("User not found");
        }

        Tweet tweet = tweetRequestDTO.toEntity(tweetRequestDTO, user);
        tweet = tweetRepository.save(tweet);

        if (Objects.isNull(tweet)) {
            log.error("Error en Tweet: {}", tweetRequestDTO.toString());
            throw new ResourceNotFoundException("Tweet not found");
        }

        log.info("Exito termina proceso!");

        messagePublisherService.publishTweet(tweet);
        log.info("Mensaje enviado a RabbitMQ: {}", tweetRequestDTO.toString());
        return new TweetResponseDTO().from(tweet);
    }

    public List<Tweet> getTimeline(String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new InvalidRequestException("User not found");
        }

        User user = userOptional.get();
        List<Tweet> tweets = tweetRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        if (tweets.isEmpty()) {
            log.error("El usuario {} no tiene seguidores", username);
            throw new ResourceNotFoundException("El usuario tiene ninguna publicacion");
        }

        return tweets;
    }
    
}
