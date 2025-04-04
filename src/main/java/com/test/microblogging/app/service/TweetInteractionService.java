package com.test.microblogging.app.service;

import com.test.microblogging.app.dto.TweetRequestDTO;
import com.test.microblogging.app.entity.Tweet;
import com.test.microblogging.app.entity.TweetInteraction;
import com.test.microblogging.app.entity.User;
import com.test.microblogging.app.exception.InvalidRequestException;
import com.test.microblogging.app.exception.ResourceNotFoundException;
import com.test.microblogging.app.repository.TweetInteractionRepository;
import com.test.microblogging.app.repository.TweetRepository;
import com.test.microblogging.utils.Constantes;
import com.test.microblogging.utils.enums.InteractionType;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * TweetInteractionService.java
 * @author Emmanuel Santiz
 * @date 2025-04-01
 */
@Log4j2
@Service
public class TweetInteractionService {

    private final TweetInteractionRepository interactionRepository;
    private final TweetRepository tweetRepository;
    private final UserService userService;

    @Autowired
    public TweetInteractionService(
            TweetInteractionRepository interactionRepository,
            TweetRepository tweetRepository,
            UserService userService) {
        this.interactionRepository = interactionRepository;
        this.tweetRepository = tweetRepository;
        this.userService = userService;
    }

    /**
     * Agregar un comentario a un tweet
     * 
     * @param username Nombre de usuario
     * @param TweetRequestDTO DTO con la información del tweet
     * @return TweetInteraction Respuesta con la interacción del tweet
     * @throws ResourceNotFoundException si el tweet no existe
     * @throws InvalidRequestException Si el usuario no existe o no se pudo crear
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    @Transactional
    //@CacheEvict(value = "MyComments", key = "#tweetId")
    public TweetInteraction addComment(Long tweetId, TweetRequestDTO tweetRequestDTO) {
        log.info("Recibe informacion: [{}}, [{}]", tweetId, tweetRequestDTO);

        // Busca al usuario
        // Si no existe lo crea
        // Si ocurre un error lo lanza
        User user = userService.getUserOrAdd(tweetRequestDTO.getUsername().trim());
        if (Objects.isNull(user)) {
            throw new InvalidRequestException(Constantes.EXCEPTION_USER_NOT_FOUND_OR_CREATED + tweetRequestDTO.getUsername());
        }
        
       // Verifica si el usuario ya comento el tweet
        // Si ya comento lanza una excepcion
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found with ID: " + tweetId));
        
        // Crea el comentario y lo guarda
        log.info("Éxito: Guardamos informacion");
        return interactionRepository.save(tweetRequestDTO.toEntityInteraction(tweetRequestDTO, user, tweet));
    }

    /**
     * Metodo para dar like a un tweet
     * 
     * @param username nombre de usuario
     * @param tweetId ID del tweet
     * @return TweetInteraction Respuesta con la interacción del tweet
     * @throws ResourceNotFoundException Si el tweet no existe
     * @throws InvalidRequestException Si el usuario no existe o ya le dio like
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    @Transactional
    public TweetInteraction likeTweet(String username, Long tweetId) {
        log.info("Recibe informacion: username: [{}], tweetId: [{}]", username, tweetId);

        // Busca al usuario
        // Si no existe lo crea
        // Si ocurre un error lo lanza
        User user = userService.getUserOrAdd(username.trim());
        if (Objects.isNull(user)) {
                throw new InvalidRequestException(Constantes.EXCEPTION_USER_NOT_FOUND_OR_CREATED + username);
        }
        
        // Verifica si el tweet existe
        // Si no existe lanza una excepcion
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro Tweet con ID: " + tweetId));
        
        // Verifica si el usuario ya le dio like al tweet
        // Si ya le dio like lanza una excepcion
        if (interactionRepository.existsByTweetIdAndUserIdAndInteractionType(
                tweetId, user.getId(), InteractionType.LIKE)) {
            throw new InvalidRequestException("Descuida ya le diste like a este tweet");
        }
        
        // Crea el like y lo guarda
        // Si ocurre un error lo lanza
        // Si no se puede guardar lanza una excepcion
        // Si se guarda correctamente regresa el like
        TweetInteraction like = new TweetInteraction();
        like.setInteractionType(InteractionType.LIKE);
        like.setUser(user);
        like.setTweet(tweet);
        
        log.info("Éxito: Guardamos informacion");
        return interactionRepository.save(like);
    }

    /**
     * Metodo para eliminar un like de un tweet
     * 
     * @param username nombre de usuario
     * @param tweetId ID del tweet
     * @return void
     * @throws ResourceNotFoundException si el tweet no existe
     * @throws InvalidRequestException si el usuario no existe o no le dio like
     * @author Emmanuel Santiz
     * @date 2025-04-01 
     */
    @Transactional
    public void unlikeTweet(String username, Long tweetId) {
        log.info("Recibe informacion: username: [{}], tweetId: [{}]", username, tweetId);

        // Busca al usuario
        // Si no existe lanza una excepcion
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.EXCEPTION_USER_NOT_FOUND + username));
        
        // Verifica si el tweet existe
        // Si no existe lanza una excepcion
        // Borra el like si no ocurre un error
        Optional<TweetInteraction> like = interactionRepository.findByTweetIdAndUserIdAndInteractionType(
                tweetId, user.getId(), InteractionType.LIKE);
        
        if (like.isPresent()) {
            interactionRepository.delete(like.get());
        } else {
            throw new ResourceNotFoundException("Descuida ya no tienes tu like a este tweet");
        }
    }

    /**
     * Obtiene todos los comentarios de un tweet
     * 
     * @param tweetId ID del tweet
     * @throws ResourceNotFoundException si el tweet no existe
     * @return List<TweetInteraction> Respuesta con la lista de comentarios
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    //@Cacheable(value = "MyComments", key = "#tweetId")
    public List<TweetInteraction> getComments(Long tweetId) {
        log.info("Recibe informacion para: [{}] con tweetId: {}", "getComments", tweetId);
        // Busca el tweet con relacion a la interaccion
        return interactionRepository.findByTweetIdAndInteractionType(
                tweetId, InteractionType.COMMENT);
    }

    /**
     * Obtiene la cantidad de likes de un tweet
     * 
     * @param tweetId ID del tweet
     * @return long Respues con la cantidad de likes
     * @throws ResourceNotFoundException si el tweet no existe
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    //@Cacheable(value = "myLikeCount", key = "#twxeetId")
    public long getLikeCount(Long tweetId) {
        log.info("Recibe informacion para: [{}] con tweetId: {}", "getLikeCount", tweetId);
        return interactionRepository.countByTweetIdAndInteractionType(
                tweetId, InteractionType.LIKE);
    }

    /**
     * Verifica si un usuario ya le dio like a un tweet
     * 
     * @param username nombre de usuario
     * @param tweetId ID del tweet
     * @return boolean true si el usuario ya le dio like al tweet
     * @throws ResourceNotFoundException si el tweet no existe
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    public boolean hasUserLikedTweet(String username, Long tweetId) {
        log.info("Recibe informacion para: [{}] con tweetId: {}", "hasUserLikedTweet", tweetId);
        // Busca al usuario
        // Si no existe lanza una excepcion
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.EXCEPTION_USER_NOT_FOUND + username));
        
        return interactionRepository.existsByTweetIdAndUserIdAndInteractionType(
                tweetId, user.getId(), InteractionType.LIKE);
    }
}